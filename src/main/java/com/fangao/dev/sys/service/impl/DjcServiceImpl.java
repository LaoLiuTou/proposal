package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fangao.dev.common.ErrorCode;
import com.fangao.dev.common.constant.EventStatus;
import com.fangao.dev.common.web.LoginHelper;
import com.fangao.dev.sys.dto.DjcDTO;
import com.fangao.dev.sys.dto.StatisDTO;
import com.fangao.dev.sys.entity.PetitionEventInfo;
import com.fangao.dev.sys.entity.PetitionEventProgress;
import com.fangao.dev.sys.entity.PetitionerInfo;
import com.fangao.dev.sys.service.*;
import com.fangao.dev.sys.utils.DateUtils;
import com.fangao.dev.sys.vo.DatePartVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wdai on 2019-3-26.
 */
@Service
public class DjcServiceImpl implements DjcService {
    @Autowired
    private IUserOrgService userOrgService;
    @Autowired
    private IPetitionerInfoService petitionerInfoService;
    @Autowired
    private IPetitionEventInfoService petitionEventInfoService;
    @Autowired
    private IPetitionEventProgressService petitionEventProgressService;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public  R<Boolean> addInfo(DjcDTO djcDTO) {
        try {
            PetitionerInfo petitionerInfo = new PetitionerInfo();
            /*String receptionOrgIds = djcDTO.getReceptionOrgIds();
            List<DjcDTO> djcDTOList = new ArrayList<>();
            String[] receptionOrgIdsArr = receptionOrgIds.split(",");
            for(String receptionOrgId : receptionOrgIdsArr){
                DjcDTO newDjcDTO = new DjcDTO();
                BeanUtils.copyProperties(djcDTO,newDjcDTO);
                newDjcDTO.setReceptionOrgId(Long.valueOf(receptionOrgId));
                djcDTOList.add(newDjcDTO);
            }*/
            BeanUtils.copyProperties(djcDTO,petitionerInfo);
            //添加信访人基本信息和事项信息
            return R.ok(petitionerInfoService.saveOrUpdate(petitionerInfo)
                    && addEventInfoAndProcess(djcDTO));
        } catch (BeansException e) {
            e.printStackTrace();
            return R.ok(false);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public String addInfoGetEventNum(DjcDTO djcDTO) {
        PetitionerInfo petitionerInfo = new PetitionerInfo();
        BeanUtils.copyProperties(djcDTO,petitionerInfo);
        PetitionEventInfo eventInfo = addEventInfo(djcDTO);
        return (petitionerInfoService.saveOrUpdate(petitionerInfo)
                && eventInfo.getId() != null
                && petitionEventProgressService.save(eventInfo.getId(),"",null)?eventInfo.getEventNum():"");
    }

    @Override
    public PetitionEventInfo addEventInfo(DjcDTO djcDTO) {
        PetitionEventInfo petitionEventInfo =  new PetitionEventInfo();
        Date visitDate = djcDTO.getVisitDate();
        //获取事件编号
        String eventNum =  getEventNum(visitDate);
        //添加必要信息
        petitionEventInfo.setEventNum(eventNum);
        if(djcDTO.getReceptionOrgId() != null){ petitionEventInfo.setReceptionOrgId(djcDTO.getReceptionOrgId());
        }else{ petitionEventInfo.setReceptionOrgId(userOrgService.getLoginOrgId());}
        petitionEventInfo.setDjcOrgId(userOrgService.getLoginOrgId());
        petitionEventInfo.setIdcard(djcDTO.getIdcard());
        petitionEventInfo.setVisitDate(visitDate);
        petitionEventInfo.setStatus(EventStatus.WAIT_FOR.getCode());
        petitionEventInfoService.save(petitionEventInfo);
        return petitionEventInfo;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean addEventInfoAndProcess(DjcDTO djcDTO){
        PetitionEventInfo petitionEventInfo =  new PetitionEventInfo();
        Date visitDate = djcDTO.getVisitDate();
        //获取事件编号
        String eventNum =  getEventNum(visitDate);
        //添加必要信息
        petitionEventInfo.setEventNum(eventNum);
        if(djcDTO.getReceptionOrgId() != null){ petitionEventInfo.setReceptionOrgId(djcDTO.getReceptionOrgId());
        }else{ petitionEventInfo.setReceptionOrgId(userOrgService.getLoginOrgId());}
        petitionEventInfo.setDjcOrgId(userOrgService.getLoginOrgId());
        petitionEventInfo.setIdcard(djcDTO.getIdcard());
        petitionEventInfo.setVisitDate(visitDate);
        petitionEventInfo.setStatus(EventStatus.WAIT_FOR.getCode());
        return petitionEventInfoService.save(petitionEventInfo)
                && petitionEventProgressService.save(petitionEventInfo.getId(),"",null);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean addEventInfoAndProcess(List<DjcDTO> djcDTOList) {
        boolean flag = true;
        if(!CollectionUtils.isEmpty(djcDTOList)){
            List<String> eventNums = getEventNum(djcDTOList.get(0).getVisitDate(),djcDTOList.size());
            for(int i=0;i<djcDTOList.size();i++){
                DjcDTO djcDTO = djcDTOList.get(i);
                PetitionEventInfo petitionEventInfo =  new PetitionEventInfo();
                //添加必要信息
                petitionEventInfo.setEventNum(eventNums.get(i));
                if(djcDTO.getReceptionOrgId() != null){ petitionEventInfo.setReceptionOrgId(djcDTO.getReceptionOrgId());
                }else{ petitionEventInfo.setReceptionOrgId(userOrgService.getLoginOrgId());}
                petitionEventInfo.setDjcOrgId(userOrgService.getLoginOrgId());
                petitionEventInfo.setIdcard(djcDTO.getIdcard());
                petitionEventInfo.setVisitDate(djcDTO.getVisitDate());
                petitionEventInfo.setStatus(EventStatus.WAIT_FOR.getCode());
                flag = flag && (petitionEventInfoService.save(petitionEventInfo)
                        && petitionEventProgressService.save(petitionEventInfo.getId(),"",null));
            }
        }
        return flag;
    }

    @Override
    public IPage<DjcDTO> page(Page page, DjcDTO djcDTO) {
        return petitionerInfoService.page(page,djcDTO);
    }
    @Override
    public boolean editInfo(DjcDTO djcDTO) {
        try {
            PetitionerInfo petitionerInfo = new PetitionerInfo();
            BeanUtils.copyProperties(djcDTO,petitionerInfo);
            //更新信访人基本信息
            petitionerInfoService.updateForSet(petitionerInfo);
            PetitionEventInfo petitionEventInfo =  new PetitionEventInfo();
            Date visitDate = djcDTO.getVisitDate();
            //添加必要信息
            petitionEventInfo.setId(djcDTO.getEventId());
            petitionEventInfo.setReceptionOrgId(djcDTO.getReceptionOrgId());
            petitionEventInfo.setVisitDate(visitDate);
            petitionEventInfoService.updateById(petitionEventInfo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*生成事件编号，编号格式为:为F + 年月日（与来访日期同步）+ 三位顺序号*/
    public String getEventNum(Date visitDate) {
        if (visitDate == null){
            return null;
        }
        SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyyMMdd");
        String formatDate = dateFormat.format(visitDate); //年月日（与来访日期同步）
        //根据数据库中当天已产生的事项数，产生3位顺序号
        int count = petitionEventInfoService.count(new QueryWrapper<PetitionEventInfo>().ge("visit_date",visitDate));
        int num = count+1;
        String countNum = String.valueOf(num);
        if(num<10){
            countNum = "00"+countNum;
        } else if(num<100){
            countNum = "0"+countNum;
        }
        String eventNum = "F" + formatDate + countNum;
        return eventNum;
    }

    /*生成事件编号，编号格式为:为F + 年月日（与来访日期同步）+ 三位顺序号*/
    public List<String> getEventNum(Date visitDate,int cou) {
        List<String> returnList = new ArrayList<>();
        if (visitDate == null){
            return null;
        }
        SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyyMMdd");
        String formatDate = dateFormat.format(visitDate); //年月日（与来访日期同步）
        //根据数据库中当天已产生的事项数，产生3位顺序号
        int count = petitionEventInfoService.count(new QueryWrapper<PetitionEventInfo>().ge("visit_date",visitDate));
        for(int i=0;i<cou;i++){
            count++;
            String countNum = String.valueOf(count);
            if(count<10){
                countNum = "00"+countNum;
            } else if(count<100){
                countNum = "0"+countNum;
            }
            String eventNum = "F" + formatDate + countNum;
            returnList.add(eventNum);
        }
        return returnList;
    }

    @Override
    public long statisDJNum(int type) {
        long count = 0L;
        DatePartVO  datePartVO = new DatePartVO();
        try {
            datePartVO = DateUtils.getDatePart(type);
        }catch (ParseException e){
            e.printStackTrace();
            Assert.fail("查询失败");
        }
        switch (type){
            case 0:
            case 1:
            case 2:
            case 3:
                count = petitionEventInfoService.count(new QueryWrapper<PetitionEventInfo>()
                        .ge("visit_date",datePartVO.getStart())
                        .le("visit_date",datePartVO.getEnd()));
                break;
            case 4:
                count = petitionEventInfoService.count(new QueryWrapper<PetitionEventInfo>()
                        .le("visit_date",datePartVO.getEnd()));
                break;
        }
        return count;
    }

    @Override
    public StatisDTO statisDJCountAndPeople(String start, String end) {
        long batch=0L,people=0L;
        StatisDTO statisDTO = new StatisDTO();
        List<PetitionEventInfo> list = petitionEventInfoService.list(new QueryWrapper<PetitionEventInfo>()
                .eq("djc_org_id",11)
                .in("reception_org_id",12,13,14,15)
                .ge("visit_date",start)
                .le("visit_date",end));
        if(!CollectionUtils.isEmpty(list)){
            for(PetitionEventInfo p :list){
                batch++;
                people+=p.getComeNum();
            }
        }
        statisDTO.setBatch(batch);
        statisDTO.setPeople(people);
        return statisDTO;
    }

    @Override
    public List<StatisDTO> statisEveryDayCountAndPeople(String start, String end) {
        List<StatisDTO> result = new ArrayList<>();
        List<StatisDTO> list = petitionEventInfoService.statisticsQueryEveryDay(start, end);
        HashMap<String,StatisDTO> listMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!CollectionUtils.isEmpty(list)){
            for(StatisDTO p :list){
                listMap.put(sdf.format(p.getDate()),p);
            }
        }
        List<String> allDate = DateUtils.getDateBetween(start,end);
        for(String date:allDate){
            StatisDTO dto = listMap.get(date);
            if(dto == null){
                dto = new StatisDTO(0,0,0,0,0,0);
            }
            dto.setYear(date.substring(0,4));
            dto.setDay(date.substring(5));
            result.add(dto);
        }
        return result;
    }
}
