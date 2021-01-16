package com.fangao.dev.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fangao.dev.common.constant.EventStatus;
import com.fangao.dev.common.constant.booleanStatus;
import com.fangao.dev.common.utils.BigDecimalUtils;
import com.fangao.dev.common.web.LoginHelper;
import com.fangao.dev.sys.dto.*;
import com.fangao.dev.sys.entity.*;
import com.fangao.dev.sys.mapper.OrgMapper;
import com.fangao.dev.sys.mapper.Org_placeMapper;
import com.fangao.dev.sys.service.*;
import com.fangao.dev.sys.utils.DateUtils;
import com.fangao.dev.sys.utils.ExportExcelUtils;
import com.fangao.dev.sys.vo.DatePartVO;
import com.fangao.dev.sys.vo.DrawPetitionLineVO;
import com.fangao.dev.sys.vo.DrawPetitionPieVO;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wdai on 2019-3-26.
 */
@Service
public class JdcServiceImpl implements JdcService {
    @Autowired
    private IPetitionerInfoService petitionerInfoService;
    @Autowired
    private IFollowPersonExtraService followPersonExtraService;
    @Autowired
    private IPetitionEventInfoService petitionEventInfoService;
    @Autowired
    private IPetitionEventInfoReLogService petitionEventInfoReLogService;
    @Autowired
    private IPetitionEventProgressService petitionEventProgressService;
    @Autowired
    private ILinkAbnormalPlaceService linkAbnormalPlaceService;
    @Autowired
    private ILinkAbnormalActionService linkAbnormalActionService;
    @Autowired
    private ILinkCoordinationUnitService linkCoordinationUnitService;
    @Autowired
    private ILinkRePetitionerEventService linkRePetitionerEventService;
    @Autowired
    private DjcService djcService;
    @Autowired
    private ISnapshotPetitionLeaderService snapshotPetitionLeaderService;
    @Autowired
    private IOrgService orgService;
    @Autowired
    private IDictPetitionEventPlaceService dictPetitionEventPlaceService;
    @Autowired
    private IDictPetitionUnitService dictPetitionUnitService;
    @Autowired
    private IParamService paramService;
    @Autowired
    private IFollowPersonService  followPersonService;

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
            petitionEventInfo.setReceptionId(djcDTO.getReceptionId());
            petitionEventInfo.setVisitDate(visitDate);
            petitionEventInfoService.updateById(petitionEventInfo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public IPage<JdcDTO> djdrPage(Page page, JdcDTO jdcDTO) {
        return petitionEventInfoService.djdrPage(page, jdcDTO);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public R<Boolean> addEventInfo(JdcDTO jdcDTO){
        try {
            // 0：不关联，1：所关联的事项为处理中，2：所关联的事项为已处理
            int repeatFlag = jdcDTO.getRepeatFlag();
            PetitionEventInfo petitionEventInfo = new PetitionEventInfo();
            //检查该接待人是否已在登记处登记，如未登记，先登记,否则更新
            PetitionerInfo petitionerInfo = petitionerInfoService.getById(jdcDTO.getIdcard());
            DjcDTO djcDTO =  new DjcDTO();
            BeanUtils.copyProperties(jdcDTO,djcDTO);
            if (petitionerInfo == null){
                // 未登记
                djcDTO.setName(jdcDTO.getPetitionerName());
                PetitionerInfo _petitionerInfo = new PetitionerInfo();
                BeanUtils.copyProperties(djcDTO,_petitionerInfo);
                // 插入登记人信息
                petitionerInfoService.saveOrUpdate(_petitionerInfo);
            }else{
                // 已登记
                // 更新信访人信息
                BeanUtils.copyProperties(jdcDTO,petitionerInfo);
                petitionerInfo.setName(jdcDTO.getPetitionerName());
                petitionerInfoService.updateById(petitionerInfo);
            }

            if(jdcDTO.getId() != null){
                petitionEventInfo = petitionEventInfoService.getById(jdcDTO.getId());
            }else{
                // 插入登记事项
                petitionEventInfo = djcService.addEventInfo(djcDTO);
            }
            // 插入开始状态事项进展
            petitionEventProgressService.save(petitionEventInfo.getId(),"",null);
            // 设置事发地和责任单位
            setDutyUnitIdAndEventPlaceId(jdcDTO);

            PetitionEventInfo new_petitionEventInfo = new PetitionEventInfo();
            BeanUtils.copyProperties(jdcDTO,new_petitionEventInfo);
            new_petitionEventInfo.setReceptionOrgId(petitionEventInfo.getReceptionOrgId());
            new_petitionEventInfo.setDjcOrgId(petitionEventInfo.getDjcOrgId());
            // 来访人数大于等于5，视为集体访问
            int comeNum = jdcDTO.getComeNum();
            if(comeNum>=5){
                new_petitionEventInfo.setIsGroupVisit(booleanStatus.YES.getCode());
            }
            // 更新状态信息
            new_petitionEventInfo.setStatus(EventStatus.IN_HAND.getCode());

            PetitionEventInfo linkInfo = null;
            // 有关联事项时，重访标志设置为1
            if(repeatFlag > 0) {
                new_petitionEventInfo.setIsRepeat(1);
                // 查询所关联的事项
                linkInfo = petitionEventInfoService.getById(jdcDTO.getRepeatId());
            }
            if(repeatFlag == 0 || repeatFlag == 2){
                // 不关联和所关联事项为已处理时，需要更新当前事项
                // 更新补全事项信息
                new_petitionEventInfo.setId(petitionEventInfo.getId());
                new_petitionEventInfo.setEventNum(petitionEventInfo.getEventNum());
            }else if(repeatFlag == 1){
                // 所关联事项为处理中
                // 逻辑删除当前事项
                PetitionEventInfo _petitionEventInfo = new  PetitionEventInfo();
                _petitionEventInfo.setId(petitionEventInfo.getId());
                petitionEventInfoService.removeById(_petitionEventInfo);

                // 更新所关联的事项
                new_petitionEventInfo.setId(linkInfo.getId());
                new_petitionEventInfo.setEventNum(linkInfo.getEventNum());
                new_petitionEventInfo.setIsRepeat(linkInfo.getIsRepeat());
            }
            petitionEventInfoService.updateById(new_petitionEventInfo);
            // 添加事项进展表信息
            petitionEventProgressService.save(new_petitionEventInfo.getId(),"接待处接待"+(repeatFlag==1?"（重访）":""),null);
            // 如果配合单位、异常行为、异常地点存在，添加关系表
            jdcDTO.setId(new_petitionEventInfo.getId());
            doHandleLinkData(jdcDTO);

            // 插入重访关系表
            insertRepeatTableAndLog(petitionEventInfo,linkInfo);
            //插入随访人信息
            insertFollowers(jdcDTO.getFollowerList(),new_petitionEventInfo);

            return R.ok(true);
        } catch (BeansException e) {
            e.printStackTrace();
            return R.ok(false);
        }
    }
    // 设置事发地和责任单位
    private void setDutyUnitIdAndEventPlaceId(JdcDTO jdcDTO){
        if(jdcDTO.getEventPlaceId() == -1){
            DictPetitionEventPlace place = dictPetitionEventPlaceService.getOne(new QueryWrapper<DictPetitionEventPlace>().eq("name",jdcDTO.getEventPlaceName()));
            if(place == null) {
                place = new DictPetitionEventPlace();
                place.setName(jdcDTO.getEventPlaceName());
                place.setStatus(0);
                place.setSort(0);
                dictPetitionEventPlaceService.save(place);
            }
            jdcDTO.setEventPlaceId(place.getId());
        }
        if(jdcDTO.getDutyUnitId() == -1){
            DictPetitionUnit unit = dictPetitionUnitService.getOne(new QueryWrapper<DictPetitionUnit>().eq("name",jdcDTO.getDutyUnitName()));
            if(unit == null) {
                unit = new DictPetitionUnit();
                unit.setName(jdcDTO.getDutyUnitName());
                unit.setMobileNo("");
                unit.setStatus(0);
                unit.setSort(0);
                dictPetitionUnitService.save(unit);
            }
            jdcDTO.setDutyUnitId(unit.getId());
        }
    }
    //插入随访人
    private void insertFollowers(List<Map> followerList,PetitionEventInfo eventInfo){
        if(CollectionUtils.isNotEmpty(followerList)){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < followerList.size(); i++) {
                Map map = followerList.get(i);
                Object idcard = map.get("idcard");
                Object name = map.get("name");
                Object sex = map.get("sex");
                Object nation = map.get("nation");
                Object permanentAddress = map.get("permanentAddress");
                Object currentAddress = map.get("currentAddress");
                Object mobileNo = map.get("mobileNo");
                Object birthDate = map.get("birthDate");
                if(!ObjectUtils.isEmpty(name)){
                    if(!ObjectUtils.isEmpty(idcard)){
                        PetitionerInfo petitionerInfo1 = new PetitionerInfo();
                        petitionerInfo1.setIdcard((String)idcard);
                        petitionerInfo1.setName((String)name);
                        if(!ObjectUtils.isEmpty(sex))
                            petitionerInfo1.setSex(Integer.parseInt((String)sex));
                        if(!ObjectUtils.isEmpty(nation))
                            petitionerInfo1.setNation((String)nation);
                        if(!ObjectUtils.isEmpty(permanentAddress))
                            petitionerInfo1.setPermanentAddress((String)permanentAddress);
                        if(!ObjectUtils.isEmpty(currentAddress))
                            petitionerInfo1.setCurrentAddress((String)currentAddress);
                        if(!ObjectUtils.isEmpty(mobileNo))
                            petitionerInfo1.setMobileNo((String)mobileNo);
                        if(!ObjectUtils.isEmpty(birthDate)){
                            String birthDateStr = (String)birthDate;
                            try {
                                petitionerInfo1.setBirthDate(format.parse(birthDateStr));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        petitionerInfoService.saveOrUpdate(petitionerInfo1);
                        List<FollowPerson> followPersonList = followPersonService.list(new QueryWrapper<FollowPerson>().eq("id",eventInfo.getId()).eq("idcard",(String)map.get("idcard")));
                        if(CollectionUtils.isEmpty(followPersonList)){
                            FollowPerson followPerson = new FollowPerson();
                            followPerson.setId(eventInfo.getId());
                            followPerson.setIdcard((String)map.get("idcard"));
                            followPersonService.save(followPerson);
                        }
                    }else{
                        FollowPersonExtra extra = new FollowPersonExtra();
                        extra.setEventId(eventInfo.getId());
                        extra.setName((String)name);
                        if(!ObjectUtils.isEmpty(sex))
                            extra.setSex(Integer.parseInt((String)sex));
                        if(!ObjectUtils.isEmpty(nation))
                            extra.setNation((String)nation);
                        if(!ObjectUtils.isEmpty(permanentAddress))
                            extra.setPermanentAddress((String)permanentAddress);
                        if(!ObjectUtils.isEmpty(currentAddress))
                            extra.setCurrentAddress((String)currentAddress);
                        if(!ObjectUtils.isEmpty(mobileNo))
                            extra.setMobileNo((String)mobileNo);
                        if(!ObjectUtils.isEmpty(birthDate)){
                            String birthDateStr = (String)birthDate;
                            try {
                                extra.setBirthDate(format.parse(birthDateStr));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        followPersonExtraService.save(extra);
                    }
                }

            }
        }
    }
    // 插入重访关系表
    private void insertRepeatTableAndLog(PetitionEventInfo petitionEventInfo,PetitionEventInfo linkInfo){
        if(linkInfo != null){
            LinkRePetitionerEvent link = new LinkRePetitionerEvent(
                    linkInfo.getId(),
                    petitionEventInfo.getId(),
                    petitionEventInfo.getIdcard(),
                    petitionEventInfo.getVisitDate()
            );
            linkRePetitionerEventService.save(link);

            // 插入重访历史表
            PetitionEventInfoReLog infoLog = new PetitionEventInfoReLog();
            BeanUtils.copyProperties(linkInfo,infoLog);
            infoLog.setReId(link.getId());
            // 配合单位
            List<LinkCoordinationUnit> coUnitList = linkCoordinationUnitService.list(new QueryWrapper<LinkCoordinationUnit>().eq("event_id",linkInfo.getId()));
            List<String> coUnitIds = new ArrayList<>();
            for(LinkCoordinationUnit t:coUnitList){
                coUnitIds.add(t.getUnitId().toString());
            }
            infoLog.setCoUnitIds(String.join(",",coUnitIds));
            // 异常行为
            List<LinkAbnormalAction> linkAbnormalActionList = linkAbnormalActionService.list(new QueryWrapper<LinkAbnormalAction>().eq("event_id",linkInfo.getId()));
            List<String> linkAbnormalActionIds = new ArrayList<>();
            for(LinkAbnormalAction t:linkAbnormalActionList){
                linkAbnormalActionIds.add(t.getActionId().toString());
            }
            infoLog.setAbnormalActionIds(String.join(",",linkAbnormalActionIds));
            // 异常地点
            List<LinkAbnormalPlace> linkAbnormalPlaceList = linkAbnormalPlaceService.list(new QueryWrapper<LinkAbnormalPlace>().eq("event_id",linkInfo.getId()));
            List<String> linkAbnormalPlaceIds = new ArrayList<>();
            for(LinkAbnormalPlace t:linkAbnormalPlaceList){
                linkAbnormalPlaceIds.add(t.getPlaceId().toString());
            }
            infoLog.setAbnormalPlaceIds(String.join(",",linkAbnormalPlaceIds));
            petitionEventInfoReLogService.save(infoLog);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public R<Boolean> editEventInfo(JdcDTO jdcDTO) {
        try {
            PetitionerInfo petitionerInfo = petitionerInfoService.getById(jdcDTO.getIdcard());
            // 更新信访人信息
            BeanUtils.copyProperties(jdcDTO,petitionerInfo);
            petitionerInfo.setName(jdcDTO.getPetitionerName());
            petitionerInfoService.updateById(petitionerInfo);

            PetitionEventInfo petitionEventInfo = petitionEventInfoService.getById(jdcDTO.getId());

            // 设置事发地和责任单位
            setDutyUnitIdAndEventPlaceId(jdcDTO);

            PetitionEventInfo new_petitionEventInfo = new PetitionEventInfo();
            BeanUtils.copyProperties(jdcDTO,new_petitionEventInfo);
            // 来访人数大于等于5，视为集体访问
            int comeNum = jdcDTO.getComeNum();
            if(comeNum>=5){
                new_petitionEventInfo.setIsGroupVisit(booleanStatus.YES.getCode());
            }
            new_petitionEventInfo.setStatus(petitionEventInfo.getStatus());

            int repeatFlag = jdcDTO.getRepeatFlag();
            PetitionEventInfo linkInfo = null;
            // 有关联事项时，重访标志设置为1
            if(repeatFlag > 0) {
                new_petitionEventInfo.setIsRepeat(1);
                // 查询所关联的事项
                linkInfo = petitionEventInfoService.getById(jdcDTO.getRepeatId());
            }
            if(repeatFlag == 0 || repeatFlag == 2){
                // 不关联和所关联事项为已处理时，需要更新当前事项
                // 不做操作
            }else if(repeatFlag == 1){
                // 所关联事项为处理中
                // 逻辑删除当前事项
                PetitionEventInfo _petitionEventInfo = new  PetitionEventInfo();
                _petitionEventInfo.setId(petitionEventInfo.getId());
                petitionEventInfoService.removeById(_petitionEventInfo);

                // 更新所关联的事项
                new_petitionEventInfo.setId(linkInfo.getId());
                new_petitionEventInfo.setEventNum(linkInfo.getEventNum());
                new_petitionEventInfo.setIsRepeat(linkInfo.getIsRepeat());
            }

            petitionEventInfoService.updateById(new_petitionEventInfo);
            //异常行为、地点、配合单位
            doHandleLinkData(jdcDTO);
            // 插入重访关系表
            insertRepeatTableAndLog(petitionEventInfo,linkInfo);
            // 插入随访人信息
            followPersonService.remove(new QueryWrapper<FollowPerson>().eq("id",jdcDTO.getId()));
            followPersonExtraService.remove(new QueryWrapper<FollowPersonExtra>().eq("event_id",jdcDTO.getId()));
            insertFollowers(jdcDTO.getFollowerList(),new_petitionEventInfo);
            return R.ok(true);
        } catch (BeansException e) {
            e.printStackTrace();
            return R.ok(false);
        }
    }

    @Override
    public IPage<JdcDTO> xxglPage(Page page, JdcDTO jdcDTO) {
        return petitionEventInfoService.xxglPage(page, jdcDTO);
    }

    @Override
    public List<Map<String,Object>> xxglPageExport(JdcDTO jdcDTO) {
        return petitionEventInfoService.xxglPageExportQuery(jdcDTO);
    }

    @Override
    public R<Boolean> addProgress(String leaderName,Long levelId,JdcDTO jdcDTO) {

        //新建领导
        if(StringUtils.isNotEmpty(leaderName)&&!leaderName.equals("-1")){
            if(levelId==null||levelId==-1)return R.ok(false);
//            System.out.println(leaderName);
            SnapshotPetitionLeader s1=snapshotPetitionLeaderService.getOne(new QueryWrapper<SnapshotPetitionLeader>().eq("name", leaderName)
                    .eq("leader_level_id", levelId).eq("deleted", 0).eq("org_id", jdcDTO.getReceptionOrgId()));
            if(s1!=null){
                jdcDTO.setLeaderId(s1.getId());
            }else{
                SnapshotPetitionLeader snapshotPetitionLeader=new SnapshotPetitionLeader();
                snapshotPetitionLeader.setName(leaderName);
                snapshotPetitionLeader.setLeaderLevelId(levelId);
                snapshotPetitionLeader.setOrgId(jdcDTO.getReceptionOrgId());
                snapshotPetitionLeaderService.save(snapshotPetitionLeader);
                jdcDTO.setLeaderId(snapshotPetitionLeader.getId());
            }
        }

//        snapshotPetitionLeaderService
        int isFinalOpinion = jdcDTO.getIsFinalOpinion();
        int status = EventStatus.IN_HAND.getCode();
        status = isFinalOpinion == 1?EventStatus.HANDLED.getCode():status;
        Long tmpId=jdcDTO.getId();
        if(tmpId==null){
            PetitionEventInfo p1=petitionEventInfoService.getOne(new QueryWrapper<PetitionEventInfo>().eq("event_num", jdcDTO.getEventNum()));
            tmpId=p1.getId();
            jdcDTO.setId(tmpId);
            jdcDTO.setDutyUnitId(p1.getDutyUnitId());
            jdcDTO.setHandUnitId(p1.getDutyUnitId());
        }
        PetitionEventInfo petitionEventInfo = new PetitionEventInfo();
        petitionEventInfo.setId(jdcDTO.getId());
        petitionEventInfo.setStatus(status);
        petitionEventInfo.setIsFinalOpinion(isFinalOpinion);
        petitionEventInfo.setNewestProgress(jdcDTO.getProgressContent());
        petitionEventInfo.setIsVerified(jdcDTO.getIsVerified());
        petitionEventInfo.setVerifyDetail(jdcDTO.getVerifyDetail());
        petitionEventInfo.setCrux(jdcDTO.getCrux());
        petitionEventInfo.setIsReasonable(jdcDTO.getIsReasonable());
        petitionEventInfo.setUnreasonableReason(jdcDTO.getUnreasonableReason());
        petitionEventInfo.setIsFinalOpinion(jdcDTO.getIsFinalOpinion());
        petitionEventInfo.setFinalOpinion(jdcDTO.getFinalOpinion());
        petitionEventInfo.setIsLeaderHad(jdcDTO.getIsLeaderHad());
        petitionEventInfo.setLeaderId(jdcDTO.getLeaderId());
//System.out.println(petitionEventProgressService.list (new QueryWrapper<PetitionEventProgress>().eq("petition_event_info_id",tmpId).orderByDesc("create_time")));
        List<PetitionEventProgress> petitionEventProgressList=petitionEventProgressService.list(new QueryWrapper<PetitionEventProgress>().eq("petition_event_info_id",tmpId)
                .eq("deleted", 0)
                .orderByDesc("create_time")
        );
        if(petitionEventProgressList.size()>0){
            PetitionEventProgress p2=petitionEventProgressList.get(0);
            int key=0;
            if(jdcDTO.getProgressContent()!=null&&p2.getContent()!=null){
                if(!jdcDTO.getProgressContent().equals(p2.getContent()))key++;
            }else if(jdcDTO.getProgressContent()!=null||p2.getContent()!=null){key++; }
            if(jdcDTO.getIsVerified()!=null||p2.getIsVerified()!=null){
                if(jdcDTO.getIsVerified()!=p2.getIsVerified())  key++;
            }
            if(jdcDTO.getVerifyDetail()!=null&&p2.getVerifyDetail()!=null){
                if(!jdcDTO.getVerifyDetail().equals(p2.getVerifyDetail()))key++;
            }else if(jdcDTO.getVerifyDetail()!=null||p2.getVerifyDetail()!=null){key++;}
            if(jdcDTO.getIsReasonable()!=null||p2.getIsReasonable()!=null){
                if(jdcDTO.getIsReasonable()!=p2.getIsReasonable())key++;
            }
            if(jdcDTO.getUnreasonableReason()!=null&&p2.getUnreasonableReason()!=null){
                if(!jdcDTO.getUnreasonableReason().equals(p2.getUnreasonableReason()))key++;
            }else if(jdcDTO.getUnreasonableReason()!=null||p2.getUnreasonableReason()!=null){key++;}
            if(jdcDTO.getCrux()!=null&&p2.getCrux()!=null){
                if(!jdcDTO.getCrux().equals(p2.getCrux()))key++;
            }else if(jdcDTO.getCrux()!=null||p2.getCrux()!=null){key++;}
            if(jdcDTO.getIsLeaderHad()!=null||p2.getIsLeaderHad()!=null){
                if(jdcDTO.getIsLeaderHad()!=p2.getIsLeaderHad())key++;
            }
            if(jdcDTO.getLeaderId()!=null||p2.getLeaderId()!=null){
                if(jdcDTO.getLeaderId()!=p2.getLeaderId())key++;
            }
            if(jdcDTO.getIsFinalOpinion()!=null||p2.getIsFinalOpinion()!=null){
                if(jdcDTO.getIsFinalOpinion()!=p2.getIsFinalOpinion())key++;
            }
            if(jdcDTO.getFinalOpinion()!=null&&p2.getFinalOpinion()!=null) {
                if(!jdcDTO.getFinalOpinion().equals(p2.getFinalOpinion()))key++;
            }else   if(jdcDTO.getFinalOpinion()!=null||p2.getFinalOpinion()!=null) {key++;}
            if(key==0) {
                return R.ok(true);
            }

        }

        if(petitionEventProgressService.save(jdcDTO)){
            petitionEventInfoService.updateById(petitionEventInfo);
            return R.ok(true);
        }
        return R.ok(false);
    }

    private void doHandleLinkData(JdcDTO jdcDTO) {
        String coUnitIds = jdcDTO.getCoUnitIds();
        String abnormalPlaceIds = jdcDTO.getAbnormalPlaceIds();
        String abnormalActionIds = jdcDTO.getAbnormalActionIds();
        //删除之前该事项的关系表
        linkAbnormalPlaceService.remove(new QueryWrapper<LinkAbnormalPlace>().eq("event_id", jdcDTO.getId()));
        if(StringUtils.isNotBlank(abnormalPlaceIds)){
            String[] split = abnormalPlaceIds.split(",");
            for (int i=0;i<split.length;i++){
                LinkAbnormalPlace linkAbnormalPlace = new LinkAbnormalPlace();
                linkAbnormalPlace.setEventId(jdcDTO.getId());
                linkAbnormalPlace.setPlaceId(Long.parseLong(split[i]));
                linkAbnormalPlaceService.save(linkAbnormalPlace);
            }
        }

        //删除之前该事项的关系表
        linkAbnormalActionService.remove(new QueryWrapper<LinkAbnormalAction>().eq("event_id", jdcDTO.getId()));
        if(StringUtils.isNotBlank(abnormalActionIds)){
            String[] split = abnormalActionIds.split(",");
            for (int i=0;i<split.length;i++){
                LinkAbnormalAction linkAbnormalAction = new LinkAbnormalAction();
                linkAbnormalAction.setEventId(jdcDTO.getId());
                linkAbnormalAction.setActionId(Long.parseLong(split[i]));
                linkAbnormalActionService.save(linkAbnormalAction);
            }
        }

        //删除之前该事项的关系表
        linkCoordinationUnitService.remove(new QueryWrapper<LinkCoordinationUnit>().eq("event_id", jdcDTO.getId()));
        if(StringUtils.isNotBlank(coUnitIds)){
            String[] split = coUnitIds.split(",");
            for (int i=0;i<split.length;i++){
                LinkCoordinationUnit linkCoordinationUnit = new LinkCoordinationUnit();
                linkCoordinationUnit.setEventId(jdcDTO.getId());
                linkCoordinationUnit.setUnitId(Long.parseLong(split[i]));
                linkCoordinationUnitService.save(linkCoordinationUnit);
            }
        }
    }

    /*生成事件编号，编号格式为:为F + 年月日（与来访日期同步）+ 三位顺序号*/
    private String getEventNum(Date visitDate) {
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

    @Override
    public List<JdcDTO>   getRepeatList(JdcDTO jdcDTO) {
        /*return petitionEventInfoService.getRepeatList(jdcDTO);*/
        // 返回数组
        List<JdcDTO> list = new ArrayList<>();
        // 有效的重复项Ids
        List<Long> active_ids = new ArrayList<>();
        // <id,jdcDTO>
        HashMap<Long,JdcDTO> map = new HashMap<>();

        int jdc_repeat_check_size = Integer.valueOf(paramService.getOne(Wrappers.<Param>query().eq("code","petition.repeat.choose.count")).getContent());
        String jdc_repeat_check_url = paramService.getOne(Wrappers.<Param>query().eq("code","petition.repeat.choose.url")).getContent();
        int jdc_repeat_check_threshold = Integer.valueOf(paramService.getOne(Wrappers.<Param>query().eq("code","petition.repeat.choose.threshold")).getContent());

        // 信访人姓名
        String petitionerName = jdcDTO.getPetitionerName();
        // 事项名称
        String name = jdcDTO.getName();
        // 事项发生地ID
        Long eventPlaceId = jdcDTO.getEventPlaceId();
        // 事项反映问题
        String eventContent = jdcDTO.getEventContent();
        // 事项主要诉求
        String mainDemand = jdcDTO.getMainDemand();

        // 信访人和事项名称都为空，返回空数组
        if(StringUtils.isBlank(name) && StringUtils.isBlank(petitionerName)) return list;

        // 查询信访人信息
        List<PetitionerInfo> petitioners = null;
        if(StringUtils.isNotBlank(petitionerName)) petitioners = petitionerInfoService.list(new QueryWrapper<PetitionerInfo>().eq("name",petitionerName));

        /*// 发生地为空，返回空数组
        if(eventPlaceId == null) return list;
        // 名称、反映问题、主要诉求都为空，返回空数组
        if(StringUtils.isBlank(name)
                && StringUtils.isBlank(eventContent)
                && StringUtils.isBlank(mainDemand)) return list;*/
        // 拼接请求体json字符串
        StringBuilder text = new StringBuilder("{" +
                " \"from\": 0, \"size\" : "+jdc_repeat_check_size+", \"_source\":false," +
                " \"query\":{" +
                "  \"bool\":{" +
                "   \"must\":[");
        if(eventPlaceId != null)text.append("{\"match\":{\"event_place_id\":"+eventPlaceId+"}},");
        text.append("{\"match\": {\"deleted\":0}}");
        if(CollectionUtils.isNotEmpty(petitioners)){
            if(petitioners.size() == 1){
                text.append(",{\"match\": {\"idcard\":\""+petitioners.get(0).getIdcard()+"\"}}");
            }else if(petitioners.size()>1){
                text.append(",{\"bool\":{\"should\":[");
                StringBuilder petitionerStrBuilder = new StringBuilder("");
                for(PetitionerInfo p : petitioners){
                    petitionerStrBuilder.append("{\"match\":{\"idcard\":\""+p.getIdcard()+"\"}},");
                }
                String petitionerStr = petitionerStrBuilder.toString();
                text.append(petitionerStr.substring(0,petitionerStr.length()-1)).append("]}}");
            }
        }

        text.append("]," +
                "   \"filter\":{\"range\":{\"status\":{\"gte\":1,\"lt\":3}}}");
        String should = "";
        if(StringUtils.isNotBlank(name)) should += "{\"match\":{\"name\": \""+name+"\"}},";
        if(StringUtils.isNotBlank(eventContent)) should += "{\"match\":{\"event_content\": \""+eventContent+"\"}},";
        if(StringUtils.isNotBlank(mainDemand)) should += "{\"match\":{\"main_demand\": \""+mainDemand+"\"}},";
        if(StringUtils.isNotEmpty(should)){
            text.append("   ,\"should\":[");
            text.append(should.substring(0,should.length()-1)).append("]");
        }
        text.append("}}}");
        // json字符串转对象
        JSONObject _postData = JSONObject.parseObject(text.toString());
        JSONObject json = new JSONObject();
        // 对引擎请求
        try {
            json = new RestTemplate().postForEntity(jdc_repeat_check_url, _postData, JSONObject.class).getBody();
        }catch (ResourceAccessException e){
            Assert.fail("判重引擎未启动或拒绝请求");
        }
        // 判断返回json的中hits是否为空，为空返回空数组
        if(json.get("hits") == null) return list;
        HashMap hits = (HashMap)json.get("hits");
        // 判断hits中hits数组是否为空，为空返回空数组
        if(hits.get("hits") == null) return list;
        ArrayList inner_hits = (ArrayList)hits.get("hits");
        // 遍历数组，存入active_ids
        if(inner_hits != null && inner_hits.size() > 0){
            for(Object o : inner_hits){
                HashMap temp = (HashMap)o;
                if(temp == null) continue;
                if(temp.get("_id") == null) continue;
                String id = String.valueOf(temp.get("_id"));
                if(temp.get("_score") == null) continue;
                Double score = (Double)(temp.get("_score"));
                // >=相似度阀值，作为有效数据
                if(score >= Integer.valueOf(jdc_repeat_check_threshold)){
                    active_ids.add(Long.valueOf(id));
                }
            }
            if(active_ids.size() > 0){
                // 根据ids查询事项
                List<JdcDTO> jdcs = petitionEventInfoService.getRepeatListNew(active_ids);
                if(jdcs != null){
                    for(JdcDTO temp:jdcs){
                        map.put(temp.getId(),temp);
                    }
                }
            }
            if(map.size() > 0){
                // 根据原相似度顺序，放入返回数组
                for(Long id:active_ids){
                    if(map.get(id) == null) continue;
                    list.add(map.get(id));
                }
            }
        }
        return list;
    }

    @Override
    public long statisJDNum(int type) {
        long count = 0L;
        DatePartVO datePartVO = new DatePartVO();
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
                        .ge("status",1)
                        .ge("visit_date",datePartVO.getStart())
                        .le("visit_date",datePartVO.getEnd()));
                break;
            case 4:
                count = petitionEventInfoService.count(new QueryWrapper<PetitionEventInfo>()
                        .ge("status",1)
                        .le("visit_date",datePartVO.getEnd()));
                break;
        }
        return count;
    }

    @Override
    public StatisDTO statisJDCountAndPeople(String start, String end) {
        long batch=0L,people=0L;
        StatisDTO statisDTO = new StatisDTO();
        List<PetitionEventInfo> list = petitionEventInfoService.list(new QueryWrapper<PetitionEventInfo>()
                .isNotNull("reception_id")
                .in("reception_org_id",12,13,14,15)
                .ge("status",1)
                .ge("visit_date",start)
                .le("visit_date",end));
        if(!org.springframework.util.CollectionUtils.isEmpty(list)){
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
    public long statisCFNum(int type) {
        long count = 0L;
        DatePartVO datePartVO = new DatePartVO();
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
                        .eq("status",1)
                        .ge("is_repeat",1)
                        .ge("visit_date",datePartVO.getStart())
                        .le("visit_date",datePartVO.getEnd()));
                break;
            case 4:
                count = petitionEventInfoService.count(new QueryWrapper<PetitionEventInfo>()
                        .eq("status",1)
                        .ge("is_repeat",1)
                        .le("visit_date",datePartVO.getEnd()));
                break;
        }
        return count;
    }

    @Override
    public StatisDTO statisCJCountAndPeople(String start, String end) {
        long batch=0L,people=0L;
        StatisDTO statisDTO = new StatisDTO();
        List<PetitionEventInfo> list = petitionEventInfoService.list(new QueryWrapper<PetitionEventInfo>()
                .isNotNull("reception_id")
                .eq("is_repeat",0)
                .in("reception_org_id",12,13,14,15)
                .ge("status",1)
                .ge("visit_date",start)
                .le("visit_date",end));
        if(!org.springframework.util.CollectionUtils.isEmpty(list)){
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
    public long statisYCLNum(int type) {
        long count = 0L;
        DatePartVO datePartVO = new DatePartVO();
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
                        .eq("status",2)
                        .ge("visit_date",datePartVO.getStart())
                        .le("visit_date",datePartVO.getEnd()));
                break;
            case 4:
                count = petitionEventInfoService.count(new QueryWrapper<PetitionEventInfo>()
                        .eq("status",2)
                        .le("visit_date",datePartVO.getEnd()));
                break;
        }
        return count;
    }

    @Override
    public StatisDTO statisYCLCountAndPeople(String start, String end) {
        long batch=0L,people=0L;
        StatisDTO statisDTO = new StatisDTO();
        List<PetitionEventInfo> list = petitionEventInfoService.list(new QueryWrapper<PetitionEventInfo>()
                .eq("status",2)
                .in("reception_org_id",12,13,14,15)
                .ge("visit_date",start)
                .le("visit_date",end));
        if(!org.springframework.util.CollectionUtils.isEmpty(list)){
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
    public List<DrawPetitionPieVO> statisPetitionPie(int type) {
        List<DrawPetitionPieVO> list = new ArrayList<>();
        long waitCount = 0L, processCount = 0L, finishCount = 0L;
        DatePartVO datePartVO = new DatePartVO();
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
                waitCount = petitionEventInfoService.count(new QueryWrapper<PetitionEventInfo>()
                        .eq("status",0)
                        .ge("visit_date",datePartVO.getStart())
                        .le("visit_date",datePartVO.getEnd()));
                processCount = petitionEventInfoService.count(new QueryWrapper<PetitionEventInfo>()
                        .eq("status",1)
                        .ge("visit_date",datePartVO.getStart())
                        .le("visit_date",datePartVO.getEnd()));
                finishCount = petitionEventInfoService.count(new QueryWrapper<PetitionEventInfo>()
                        .eq("status",2)
                        .ge("visit_date",datePartVO.getStart())
                        .le("visit_date",datePartVO.getEnd()));
                break;
            case 4:
                waitCount = petitionEventInfoService.count(new QueryWrapper<PetitionEventInfo>()
                        .eq("status",0)
                        .le("visit_date",datePartVO.getEnd()));
                processCount = petitionEventInfoService.count(new QueryWrapper<PetitionEventInfo>()
                        .eq("status",1)
                        .le("visit_date",datePartVO.getEnd()));
                finishCount = petitionEventInfoService.count(new QueryWrapper<PetitionEventInfo>()
                        .eq("status",2)
                        .le("visit_date",datePartVO.getEnd()));
                break;
        }

        BigDecimal sum = new BigDecimal(waitCount+processCount+finishCount);
        double wait_p = 0d,process_p=0d,finish_p=0d;
        if(sum.compareTo(BigDecimal.ZERO) > 0 ){
            wait_p = BigDecimalUtils.mul(
                    BigDecimalUtils.div(new BigDecimal(waitCount), sum, 4),
                    new BigDecimal(100)
            ).doubleValue();
            process_p = BigDecimalUtils.mul(
                    BigDecimalUtils.div(new BigDecimal(processCount), sum, 4),
                    new BigDecimal(100)
            ).doubleValue();
            finish_p = BigDecimalUtils.mul(
                    BigDecimalUtils.div(new BigDecimal(finishCount), sum, 4),
                    new BigDecimal(100)
            ).doubleValue();
        }

        list.add(new DrawPetitionPieVO("待接待",waitCount,wait_p));
        list.add(new DrawPetitionPieVO("处理中",processCount,process_p));
        list.add(new DrawPetitionPieVO("已处理",finishCount,finish_p));

        return list;
    }

    @Override
    public List<DrawPetitionLineVO> statisPetitionLine(int type) {
        List<DrawPetitionLineVO> list = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        List<DatePartVO> monthList = new ArrayList<DatePartVO>();
        DatePartVO datePartVO = new DatePartVO();
        HashMap<String,Long> djMap = new HashMap<String,Long>();
        HashMap<String,Long> jdMap = new HashMap<String,Long>();
        SimpleDateFormat sdf_1 = new SimpleDateFormat("MM-dd");
        List<PetitionLineCountDTO> djList = new ArrayList<PetitionLineCountDTO>();
        List<PetitionLineCountDTO> jdList = new ArrayList<PetitionLineCountDTO>();
        try {
            datePartVO = DateUtils.getDatePart(type);
            if(type<=2){
                dateList = DateUtils.getDateList(datePartVO.getStart(),datePartVO.getEnd());
            }else{
                monthList = DateUtils.getMonthListOneYearByEnd(datePartVO.getEnd());
            }
        }catch (ParseException e){
            e.printStackTrace();
            Assert.fail("查询失败");
        }

        switch (type){
            case 0:
            case 1:
            case 2:
                List<PetitionCountDTO> djCount = petitionEventInfoService.countByDatePart(datePartVO.getStart(),datePartVO.getEnd(),0);
                List<PetitionCountDTO> jdCount = petitionEventInfoService.countByDatePart(datePartVO.getStart(),datePartVO.getEnd(),1);
                for(PetitionCountDTO temp :djCount){
                    djMap.put(sdf_1.format(temp.getVisitDate()),temp.getCount());
                }
                for(PetitionCountDTO temp :jdCount){
                    jdMap.put(sdf_1.format(temp.getVisitDate()),temp.getCount());
                }
                for(String _date:dateList){
                    String date = _date.substring(5);
                    double rate = 0d;
                    long dj = 0,jd = 0;
                    if(djMap.get(date) != null){
                        dj = djMap.get(date);jd = (jdMap.get(date) != null)?jdMap.get(date):0L;
                        rate = BigDecimalUtils.mul(
                                BigDecimalUtils.div(new BigDecimal(jd), new BigDecimal(dj), 4),
                                new BigDecimal(100)
                        ).doubleValue();
                    }
                    list.add(new DrawPetitionLineVO(date,dj,"登记数",rate));
                    list.add(new DrawPetitionLineVO(date,jd,"接待数",rate));
                }
                break;
            case 3:
                djList = petitionEventInfoService.countByDatePart2(datePartVO.getStart(),datePartVO.getEnd(),0);
                jdList = petitionEventInfoService.countByDatePart2(datePartVO.getStart(),datePartVO.getEnd(),1);
                list = changeListToLineDTO(monthList,djList,jdList,3);
                break;
            case 4:
                djList = petitionEventInfoService.countByDatePart2("",datePartVO.getEnd(),0);
                jdList = petitionEventInfoService.countByDatePart2("",datePartVO.getEnd(),1);
                list = changeListToLineDTO(monthList,djList,jdList,4);
                break;
        }

        return list;
    }


    private List<DrawPetitionLineVO> changeListToLineDTO(
            List<DatePartVO> monthList,
            List<PetitionLineCountDTO> djList,
            List<PetitionLineCountDTO> jdList,
            int type){
        List<DrawPetitionLineVO> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        HashMap<String,Long> djMap = new HashMap<String,Long>();
        HashMap<String,Long> jdMap = new HashMap<String,Long>();
        String firstMonth = "";
        if (djList.size() >0) firstMonth = sdf.format(djList.get(0).getVisitDate());
        try{
            if(type == 4 && firstMonth.compareTo(monthList.get(0).getStart().substring(0,7))<0)
                monthList = DateUtils.getMonthListByStartToNow(firstMonth +"-01");
        }catch (ParseException e){
            e.printStackTrace();
            Assert.fail("查询失败");
        }
        for(PetitionLineCountDTO dj : djList){
            String month = sdf.format(dj.getVisitDate());
            if(djMap.get(month) != null){
                djMap.put(month,djMap.get(month)+1);
            }else{
                djMap.put(month,1L);
            }
        }
        for(PetitionLineCountDTO jd : jdList){
            String month = sdf.format(jd.getVisitDate());
            if(jdMap.get(month) != null){
                jdMap.put(month,jdMap.get(month)+1);
            }else{
                jdMap.put(month,1L);
            }
        }
        for(DatePartVO date:monthList){
            double rate = 0d;
            long dj = 0,jd = 0;
            String month = date.getStart().substring(0,7);
            if(djMap.get(month) != null){
                dj = djMap.get(month);
                jd = (jdMap.get(month) != null)?jdMap.get(month):0L;
                rate = BigDecimalUtils.mul(
                        BigDecimalUtils.div(new BigDecimal(jd), new BigDecimal(dj), 4),
                        new BigDecimal(100)
                ).doubleValue();
            }

            list.add(new DrawPetitionLineVO(month,dj,"登记数",rate));
            list.add(new DrawPetitionLineVO(month,jd,"接待数",rate));
        }
        return list;
    }
    @Override
    public List<File> jdcExportExcelFileList(long orgId,JdcExportExcelDTO jdcExportExcelDTO){
        boolean isUseDateLimit=jdcExportExcelDTO.getIsUseDateLimit();
        List<Long> checkedUnitIdList=jdcExportExcelDTO.getCheckedUnitIdList();
        List<File> fileList=new ArrayList<>();

        for(Long i:checkedUnitIdList){
            List<JdcDTO> jdcDTOList=orgService.getJdcDtoByOrgIdAndUnitId(orgId,i,jdcExportExcelDTO.getIsUseDateLimit(),jdcExportExcelDTO.getBeginDate(),jdcExportExcelDTO.getEndDate());
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("progress", jdcDTOList);
            //加载excel模板文件
            File file = null;
            String rootPath = null;
            try {
                rootPath = ResourceUtils.getURL("classpath:").getPath();
                file = ResourceUtils.getFile(rootPath+"excel/工作进展模板.xlsx");
            } catch (FileNotFoundException e) {
            }

            //配置下载路径
            String path = rootPath+"excel/tempFiles/";
            ExportExcelUtils.createDir(new File(path));
            String name=Long.toString(i)+"-"+dictPetitionUnitService.getById(i).getName();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(isUseDateLimit){
                name+="("+sdf.format(jdcExportExcelDTO.getBeginDate())+"至"+sdf.format(jdcExportExcelDTO.getEndDate())+")";
            }
            //根据模板生成新的excel
            File excelFile = ExportExcelUtils.createNewFile(model, file, path,name);
            fileList.add(excelFile);
        }

        return fileList;
    }
    @Autowired
    private IDictPetitionContentTypeService dictPetitionContentTypeService;
    @Override
    public File jdcExportWeek_Report_ExcelFileList(long orgId,JdcExportWeekReportExcelDTO jdcExportWeekReportExcelDTO){
        //List<Long> checkedUnitIdList=jdcExportExcelDTO.getCheckedUnitIdList();
        List<File> fileList=new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<JdcWeekReportDTO> res = new ArrayList<>();
        Integer event_place_id;
        List<String> event_places = new ArrayList<>();
        Org org = orgService.getById(orgId);
        if(org.getName().equals("接待一处")||org.getName().equals("接待二处")){
        if(jdcExportWeekReportExcelDTO.getSwitch_status()==false){
            event_places.add("市直部门");
            event_places.add("二道");
            event_places.add("汽");
            event_places.add("经开");
            event_places.add("农安");
            event_places.add("双阳");
            event_places.add("德惠");
            event_places.add("南关");
            event_places.add("宽城");
            event_places.add("绿园");
            event_places.add("九台");
            event_places.add("朝阳");
            event_places.add("榆树");
            event_places.add("莲花山");
            event_places.add("净月");
            event_places.add("新区");
        }
        else{

            if(org!=null){
                String org_name=org.getName();
                event_places=org_placeMapper.getPlace_nameByOrgName(org_name);
            }
        }
        List<String> problems = Arrays.asList("农民工讨薪","农村农业","国土资源","城乡建设","劳动和社会保障","卫生计生","教育文体","民政","社法涉诉","涉警","交通运输","环境保护","组织人事","纪检监察","国资监管");
        for(String event_place:event_places) {
            //for(event_place_id=105;event_place_id<=114;event_place_id++){
            JdcWeekReportDTO temp = new JdcWeekReportDTO();
            //模糊查询列表
            List<DictPetitionEventPlace> eps = dictPetitionEventPlaceService.getByName(event_place);
            Integer sumEvents=0;
            Integer sumComes=0;
            Integer sum_group_visit_event=0;
            Integer sum_group_visit_come=0;
            Integer sum_single_visit_event=0;
            Integer sum_single_visit_come=0;
            Integer sum_first_group_come=0;
            Integer sum_first_group_event=0;
            Integer sum_first_single_come=0;
            Integer sum_first_single_event=0;
            Integer sum_repeat_group_come=0;
            Integer sum_repeat_group_event=0;
            Integer sum_repeat_single_come=0;
            Integer sum_repeat_single_event=0;
            Integer nmgtx_come=0;
            Integer nmgtx_event=0;
            Integer ncny_come=0;
            Integer ncny_event=0;
            Integer gtzy_come=0;
            Integer gtzy_event=0;
            Integer cxjs_come=0;
            Integer cxjs_event=0;
            Integer ldhshbz_come=0;
            Integer ldhshbz_event=0;
            Integer wsjs_come=0;
            Integer wsjs_event=0;
            Integer jywt_come=0;
            Integer jywt_event=0;
            Integer mz_come=0;
            Integer mz_event=0;
            Integer sfss_come=0;
            Integer sfss_event=0;
            Integer sj_come=0;
            Integer sj_event=0;
            Integer jtys_come=0;
            Integer jtys_event=0;
            Integer hjbh_come=0;
            Integer hjbh_event=0;
            Integer zzrs_come=0;
            Integer zzrs_event=0;
            Integer jjjc_come=0;
            Integer jjjc_event=0;
            Integer gzjg_come=0;
            Integer gzjg_event=0;
            Integer problem_event=0;
            Integer problem_come=0;
            //DictPetitionEventPlace ep = dictPetitionEventPlaceService.getById(event_place_id);
            for (DictPetitionEventPlace ep : eps){

                List<PetitionEventInfo> events = petitionEventInfoService.getPetitionEventInfos(orgId, ep.getId(), sdf.format(jdcExportWeekReportExcelDTO.getBeginDate()), sdf.format(jdcExportWeekReportExcelDTO.getEndDate()));
                sumEvents+=events.size();
                for (PetitionEventInfo event : events) {
                    sumComes += event.getComeNum();
                }
                Integer sum_event = 0;
                for (PetitionEventInfo event : events) {
                    if (event.getIsGroupVisit() == 1) {
                        sum_group_visit_come += event.getComeNum();
                        sum_group_visit_event++;
                    }
                }
                for (PetitionEventInfo event : events) {
                    if (event.getIsGroupVisit() == 1 && event.getIsRepeat() == 1) {
                        sum_repeat_group_come += event.getComeNum();
                        sum_repeat_group_event++;
                    }
                }
                for (PetitionEventInfo event : events) {
                    if (event.getIsGroupVisit() == 0 && event.getIsRepeat() == 1) {
                        sum_repeat_single_come += event.getComeNum();
                        sum_repeat_single_event++;
                    }
                }
                for (PetitionEventInfo event : events) {
                    if (event.getIsGroupVisit() == 1 && event.getIsRepeat() == 0) {
                        sum_first_group_come += event.getComeNum();
                        sum_first_group_event++;
                    }
                }
                for (PetitionEventInfo event : events) {
                    if (event.getIsGroupVisit() == 0 && event.getIsRepeat() == 0) {
                        sum_first_single_come += event.getComeNum();
                        sum_first_single_event++;
                    }
                }
                List<ProblemDTO> problemDTOS = new ArrayList<>();
                for (String problem : problems) {
                    ProblemDTO p = new ProblemDTO();
                    p.setName(problem);
                    Integer come_num = 0;
                    Integer event_num = 0;
                    if (dictPetitionContentTypeService.getByName(problem) != null) {
                        Long event_id = dictPetitionContentTypeService.getByName(problem).getId();
                        for (PetitionEventInfo event : events) {
                            if(event.getContentTypeId()!=null) {
                                Long id = event.getContentTypeId();
                                if (id == event_id) {
                                    come_num += event.getComeNum();
                                    event_num++;
                                }
                                while (id != 0) {
                                    DictPetitionContentType dictPetitionContentType = dictPetitionContentTypeService.getById(id);
                                    id = dictPetitionContentType.getPid();
                                    if (dictPetitionContentType.getName().equals(problem)) {
                                        come_num += event.getComeNum();
                                        event_num++;
                                        break;
                                    }
                                }
                            }
                    /*
                    while(dictPetitionContentType.getPid()!=0){
                        id = dictPetitionContentType.getPid();
                        dictPetitionContentType = dictPetitionContentTypeService.getById(id);
                        if(dictPetitionContentType.getName().equals(problem)){
                            come_num+=event.getComeNum();
                            event_num++;
                        }
                    }
                    */
                        }
                    }
                    //List<String> problems = Arrays.asList("农民工讨薪","农村农业","国土资源","城乡建设","劳动和社会保障","卫生计生","教育文体","民政","社法涉诉","涉警","交通运输","环境保护","组织人事","纪检监察","国资监管");
                    switch (problem) {
                        case "农民工讨薪":
                            nmgtx_come+=come_num;
                            nmgtx_event+=event_num;
                            break;
                        case "农村农业":
                            ncny_come+=(come_num);
                            ncny_event+=(event_num);
                            break;
                        case "国土资源":
                            gtzy_come+=(come_num);
                            gtzy_event+=(event_num);
                            break;
                        case "城乡建设":
                            cxjs_come+=(come_num);
                            cxjs_event+=(event_num);
                            break;
                        case "劳动和社会保障":
                            ldhshbz_come+=(come_num);
                            ldhshbz_event+=(event_num);
                            break;
                        case "卫生计生":
                            wsjs_come+=(come_num);
                            wsjs_event+=(event_num);
                            break;
                        case "教育文体":
                            jywt_come+=(come_num);
                            jywt_event+=(event_num);
                            break;
                        case "民政":
                            mz_come+=(come_num);
                            mz_event+=(event_num);
                            break;
                        case "社法涉诉":
                            sfss_come+=(come_num);
                            sfss_event+=(event_num);
                            break;
                        case "涉警":
                            sj_come+=(come_num);
                            sj_event+=(event_num);
                            break;
                        case "交通运输":
                            jtys_come+=(come_num);
                            jtys_event+=(event_num);
                            break;
                        case "环境保护":
                            hjbh_come+=(come_num);
                            hjbh_event+=(event_num);
                            break;
                        case "组织人事":
                            zzrs_come+=(come_num);
                            zzrs_event+=(event_num);
                            break;
                        case "纪检监察":
                            jjjc_come+=(come_num);
                            jjjc_event+=(event_num);
                            break;
                        case "国资监管":
                            gzjg_come+=(come_num);
                            gzjg_event+=(event_num);
                            break;
                    }
                    //p.setCome(come_num);
                    //p.setEvent(event_num);
                    //problemDTOS.add(p);
                    //problem_come += come_num;
                    // problem_event += event_num;
                }
                //temp.setProblems(problemDTOS);
                //temp.setProblem_come(problem_come);
                //temp.setProblem_event(problem_event);
            }
            temp.setEvent_place(event_place);
            if(sumComes!=0)
                temp.setSumComes(sumComes);
            if(sumEvents!=0)
                temp.setSumEvents(sumEvents);
            if(sum_group_visit_come!=0)
                temp.setSum_group_visit_come(sum_group_visit_come);
            if(sum_group_visit_event!=0)
                temp.setSum_group_visit_event(sum_group_visit_event);
            sum_single_visit_come=sumComes-sum_group_visit_come;
            sum_single_visit_event=sumEvents-sum_group_visit_event;
            if(sum_single_visit_come!=0)
                temp.setSum_single_visit_come(sum_single_visit_come);
            if(sum_single_visit_event!=0)
                temp.setSum_single_visit_event(sum_single_visit_event);
            if(sum_first_group_come!=0)
                temp.setSum_first_group_come(sum_first_group_come);
            if(sum_first_group_event!=0)
                temp.setSum_first_group_event(sum_first_group_event);
            if(sum_first_single_come!=0)
                temp.setSum_first_single_come(sum_first_single_come);
            if(sum_first_single_event!=0)
                temp.setSum_first_single_event(sum_first_single_event);
            if(sum_repeat_group_come!=0)
                temp.setSum_repeat_group_come(sum_repeat_group_come);
            if(sum_repeat_group_event!=0)
                temp.setSum_repeat_group_event(sum_repeat_group_event);
            if(sum_repeat_single_come!=0)
                temp.setSum_repeat_single_come(sum_repeat_single_come);
            if(sum_repeat_single_event!=0)
                temp.setSum_repeat_single_event(sum_repeat_single_event);
            if(nmgtx_come!=0)
                temp.setNmgtx_come(nmgtx_come);
            if(nmgtx_event!=0)
                temp.setNmgtx_event(nmgtx_event);
            if(ncny_come!=0)
                temp.setNcny_come(ncny_come);
            if(ncny_event!=0)
                temp.setNmgtx_event(ncny_event);
            if(gtzy_come!=0)
                temp.setGtzy_come(gtzy_come);
            if(gtzy_event!=0)
                temp.setGtzy_event(gtzy_event);
            if(cxjs_come!=0)
                temp.setCxjs_come(cxjs_come);
            if(cxjs_event!=0)
                temp.setCxjs_event(cxjs_event);
            if(ldhshbz_come!=0)
                temp.setLdhshbz_come(ldhshbz_come);
            if(ldhshbz_event!=0)
                temp.setLdhshbz_event(ldhshbz_event);
            if(wsjs_come!=0)
                temp.setWsjs_come(wsjs_come);
            if(wsjs_event!=0)
                temp.setWsjs_event(wsjs_event);
            if(jywt_come!=0)
                temp.setJywt_come(jywt_come);
            if(jywt_event!=0)
                temp.setJywt_event(jywt_event);
            if(mz_come!=0)
                temp.setMz_come(mz_come);
            if(mz_event!=0)
                temp.setMz_event(mz_event);
            if(sfss_come!=0)
                temp.setSfss_come(sfss_come);
            if(sfss_event!=0)
                temp.setSfss_event(sfss_event);
            if(sj_come!=0)
                temp.setSj_come(sj_come);
            if(sj_event!=0)
                temp.setSj_event(sj_event);
            if(jtys_come!=0)
                temp.setJtys_come(jtys_come);
            if(jtys_event!=0)
                temp.setJtys_event(jtys_event);
            if(hjbh_come!=0)
                temp.setHjbh_come(hjbh_come);
            if(hjbh_event!=0)
                temp.setHjbh_event(hjbh_event);
            if(zzrs_come!=0)
                temp.setZzrs_come(zzrs_come);
            if(zzrs_event!=0)
                temp.setZzrs_event(zzrs_event);
            if(jjjc_come!=0)
                temp.setJjjc_come(jjjc_come);
            if(jjjc_event!=0)
                temp.setJjjc_event(jjjc_event);
            if(gzjg_come!=0)
                temp.setGzjg_come(gzjg_come);
            if(gzjg_event!=0)
                temp.setGzjg_event(gzjg_event);
            problem_come =nmgtx_come+ncny_come+gtzy_come+cxjs_come+ldhshbz_come+wsjs_come+jywt_come+mz_come+sfss_come+sj_come+jtys_come+hjbh_come+zzrs_come+jjjc_come+gzjg_come;
            problem_event =nmgtx_event+ncny_event+gtzy_event+cxjs_event+ldhshbz_event+wsjs_event+jywt_event+mz_event+sfss_event+sj_event+jtys_event+hjbh_event+zzrs_event+jjjc_event+gzjg_event;
            if(problem_come!=0)
                temp.setProblem_come(problem_come);
            if(problem_event!=0)
                temp.setProblem_event(problem_event);
            res.add(temp);
        }}
        //return res;
        else{
            JdcWeekReportDTO temp = new JdcWeekReportDTO();
            List<String> problems = Arrays.asList("农民工讨薪","农村农业","国土资源","城乡建设","劳动和社会保障","卫生计生","教育文体","民政","社法涉诉","涉警","交通运输","环境保护","组织人事","纪检监察","国资监管");
            Integer sumEvents=0;
            Integer sumComes=0;
            Integer sum_group_visit_event=0;
            Integer sum_group_visit_come=0;
            Integer sum_single_visit_event=0;
            Integer sum_single_visit_come=0;
            Integer sum_first_group_come=0;
            Integer sum_first_group_event=0;
            Integer sum_first_single_come=0;
            Integer sum_first_single_event=0;
            Integer sum_repeat_group_come=0;
            Integer sum_repeat_group_event=0;
            Integer sum_repeat_single_come=0;
            Integer sum_repeat_single_event=0;
            Integer nmgtx_come=0;
            Integer nmgtx_event=0;
            Integer ncny_come=0;
            Integer ncny_event=0;
            Integer gtzy_come=0;
            Integer gtzy_event=0;
            Integer cxjs_come=0;
            Integer cxjs_event=0;
            Integer ldhshbz_come=0;
            Integer ldhshbz_event=0;
            Integer wsjs_come=0;
            Integer wsjs_event=0;
            Integer jywt_come=0;
            Integer jywt_event=0;
            Integer mz_come=0;
            Integer mz_event=0;
            Integer sfss_come=0;
            Integer sfss_event=0;
            Integer sj_come=0;
            Integer sj_event=0;
            Integer jtys_come=0;
            Integer jtys_event=0;
            Integer hjbh_come=0;
            Integer hjbh_event=0;
            Integer zzrs_come=0;
            Integer zzrs_event=0;
            Integer jjjc_come=0;
            Integer jjjc_event=0;
            Integer gzjg_come=0;
            Integer gzjg_event=0;
            Integer problem_event=0;
            Integer problem_come=0;
            List<PetitionEventInfo> events = petitionEventInfoService.getPetitionEventInfosByOrg(orgId,sdf.format(jdcExportWeekReportExcelDTO.getBeginDate()), sdf.format(jdcExportWeekReportExcelDTO.getEndDate()));
            sumEvents+=events.size();
            for (PetitionEventInfo event : events) {
                sumComes += event.getComeNum();
            }
            Integer sum_event = 0;
            for (PetitionEventInfo event : events) {
                if (event.getIsGroupVisit() == 1) {
                    sum_group_visit_come += event.getComeNum();
                    sum_group_visit_event++;
                }
            }
            for (PetitionEventInfo event : events) {
                if (event.getIsGroupVisit() == 1 && event.getIsRepeat() == 1) {
                    sum_repeat_group_come += event.getComeNum();
                    sum_repeat_group_event++;
                }
            }
            for (PetitionEventInfo event : events) {
                if (event.getIsGroupVisit() == 0 && event.getIsRepeat() == 1) {
                    sum_repeat_single_come += event.getComeNum();
                    sum_repeat_single_event++;
                }
            }
            for (PetitionEventInfo event : events) {
                if (event.getIsGroupVisit() == 1 && event.getIsRepeat() == 0) {
                    sum_first_group_come += event.getComeNum();
                    sum_first_group_event++;
                }
            }
            for (PetitionEventInfo event : events) {
                if (event.getIsGroupVisit() == 0 && event.getIsRepeat() == 0) {
                    sum_first_single_come += event.getComeNum();
                    sum_first_single_event++;
                }
            }
            List<ProblemDTO> problemDTOS = new ArrayList<>();
            for (String problem : problems) {
                ProblemDTO p = new ProblemDTO();
                p.setName(problem);
                Integer come_num = 0;
                Integer event_num = 0;
                if (dictPetitionContentTypeService.getByName(problem) != null) {
                    Long event_id = dictPetitionContentTypeService.getByName(problem).getId();
                    for (PetitionEventInfo event : events) {
                        if(event.getContentTypeId()!=null) {
                            Long id = event.getContentTypeId();
                            if (id == event_id) {
                                come_num += event.getComeNum();
                                event_num++;
                            }
                            while (id != 0) {
                                DictPetitionContentType dictPetitionContentType = dictPetitionContentTypeService.getById(id);
                                id = dictPetitionContentType.getPid();
                                if (dictPetitionContentType.getName().equals(problem)) {
                                    come_num += event.getComeNum();
                                    event_num++;
                                    break;
                                }
                            }
                        }
                    /*
                    while(dictPetitionContentType.getPid()!=0){
                        id = dictPetitionContentType.getPid();
                        dictPetitionContentType = dictPetitionContentTypeService.getById(id);
                        if(dictPetitionContentType.getName().equals(problem)){
                            come_num+=event.getComeNum();
                            event_num++;
                        }
                    }
                    */
                    }
                }
                //List<String> problems = Arrays.asList("农民工讨薪","农村农业","国土资源","城乡建设","劳动和社会保障","卫生计生","教育文体","民政","社法涉诉","涉警","交通运输","环境保护","组织人事","纪检监察","国资监管");
                switch (problem) {
                    case "农民工讨薪":
                        nmgtx_come+=come_num;
                        nmgtx_event+=event_num;
                        break;
                    case "农村农业":
                        ncny_come+=(come_num);
                        ncny_event+=(event_num);
                        break;
                    case "国土资源":
                        gtzy_come+=(come_num);
                        gtzy_event+=(event_num);
                        break;
                    case "城乡建设":
                        cxjs_come+=(come_num);
                        cxjs_event+=(event_num);
                        break;
                    case "劳动和社会保障":
                        ldhshbz_come+=(come_num);
                        ldhshbz_event+=(event_num);
                        break;
                    case "卫生计生":
                        wsjs_come+=(come_num);
                        wsjs_event+=(event_num);
                        break;
                    case "教育文体":
                        jywt_come+=(come_num);
                        jywt_event+=(event_num);
                        break;
                    case "民政":
                        mz_come+=(come_num);
                        mz_event+=(event_num);
                        break;
                    case "社法涉诉":
                        sfss_come+=(come_num);
                        sfss_event+=(event_num);
                        break;
                    case "涉警":
                        sj_come+=(come_num);
                        sj_event+=(event_num);
                        break;
                    case "交通运输":
                        jtys_come+=(come_num);
                        jtys_event+=(event_num);
                        break;
                    case "环境保护":
                        hjbh_come+=(come_num);
                        hjbh_event+=(event_num);
                        break;
                    case "组织人事":
                        zzrs_come+=(come_num);
                        zzrs_event+=(event_num);
                        break;
                    case "纪检监察":
                        jjjc_come+=(come_num);
                        jjjc_event+=(event_num);
                        break;
                    case "国资监管":
                        gzjg_come+=(come_num);
                        gzjg_event+=(event_num);
                        break;
                }
                //p.setCome(come_num);
                //p.setEvent(event_num);
                //problemDTOS.add(p);
                //problem_come += come_num;
                // problem_event += event_num;
            }
            temp.setEvent_place(org.getName());
            if(sumComes!=0)
                temp.setSumComes(sumComes);
            if(sumEvents!=0)
                temp.setSumEvents(sumEvents);
            if(sum_group_visit_come!=0)
                temp.setSum_group_visit_come(sum_group_visit_come);
            if(sum_group_visit_event!=0)
                temp.setSum_group_visit_event(sum_group_visit_event);
            sum_single_visit_come=sumComes-sum_group_visit_come;
            sum_single_visit_event=sumEvents-sum_group_visit_event;
            if(sum_single_visit_come!=0)
                temp.setSum_single_visit_come(sum_single_visit_come);
            if(sum_single_visit_event!=0)
                temp.setSum_single_visit_event(sum_single_visit_event);
            if(sum_first_group_come!=0)
                temp.setSum_first_group_come(sum_first_group_come);
            if(sum_first_group_event!=0)
                temp.setSum_first_group_event(sum_first_group_event);
            if(sum_first_single_come!=0)
                temp.setSum_first_single_come(sum_first_single_come);
            if(sum_first_single_event!=0)
                temp.setSum_first_single_event(sum_first_single_event);
            if(sum_repeat_group_come!=0)
                temp.setSum_repeat_group_come(sum_repeat_group_come);
            if(sum_repeat_group_event!=0)
                temp.setSum_repeat_group_event(sum_repeat_group_event);
            if(sum_repeat_single_come!=0)
                temp.setSum_repeat_single_come(sum_repeat_single_come);
            if(sum_repeat_single_event!=0)
                temp.setSum_repeat_single_event(sum_repeat_single_event);
            if(nmgtx_come!=0)
                temp.setNmgtx_come(nmgtx_come);
            if(nmgtx_event!=0)
                temp.setNmgtx_event(nmgtx_event);
            if(ncny_come!=0)
                temp.setNcny_come(ncny_come);
            if(ncny_event!=0)
                temp.setNmgtx_event(ncny_event);
            if(gtzy_come!=0)
                temp.setGtzy_come(gtzy_come);
            if(gtzy_event!=0)
                temp.setGtzy_event(gtzy_event);
            if(cxjs_come!=0)
                temp.setCxjs_come(cxjs_come);
            if(cxjs_event!=0)
                temp.setCxjs_event(cxjs_event);
            if(ldhshbz_come!=0)
                temp.setLdhshbz_come(ldhshbz_come);
            if(ldhshbz_event!=0)
                temp.setLdhshbz_event(ldhshbz_event);
            if(wsjs_come!=0)
                temp.setWsjs_come(wsjs_come);
            if(wsjs_event!=0)
                temp.setWsjs_event(wsjs_event);
            if(jywt_come!=0)
                temp.setJywt_come(jywt_come);
            if(jywt_event!=0)
                temp.setJywt_event(jywt_event);
            if(mz_come!=0)
                temp.setMz_come(mz_come);
            if(mz_event!=0)
                temp.setMz_event(mz_event);
            if(sfss_come!=0)
                temp.setSfss_come(sfss_come);
            if(sfss_event!=0)
                temp.setSfss_event(sfss_event);
            if(sj_come!=0)
                temp.setSj_come(sj_come);
            if(sj_event!=0)
                temp.setSj_event(sj_event);
            if(jtys_come!=0)
                temp.setJtys_come(jtys_come);
            if(jtys_event!=0)
                temp.setJtys_event(jtys_event);
            if(hjbh_come!=0)
                temp.setHjbh_come(hjbh_come);
            if(hjbh_event!=0)
                temp.setHjbh_event(hjbh_event);
            if(zzrs_come!=0)
                temp.setZzrs_come(zzrs_come);
            if(zzrs_event!=0)
                temp.setZzrs_event(zzrs_event);
            if(jjjc_come!=0)
                temp.setJjjc_come(jjjc_come);
            if(jjjc_event!=0)
                temp.setJjjc_event(jjjc_event);
            if(gzjg_come!=0)
                temp.setGzjg_come(gzjg_come);
            if(gzjg_event!=0)
                temp.setGzjg_event(gzjg_event);
            problem_come =nmgtx_come+ncny_come+gtzy_come+cxjs_come+ldhshbz_come+wsjs_come+jywt_come+mz_come+sfss_come+sj_come+jtys_come+hjbh_come+zzrs_come+jjjc_come+gzjg_come;
            problem_event =nmgtx_event+ncny_event+gtzy_event+cxjs_event+ldhshbz_event+wsjs_event+jywt_event+mz_event+sfss_event+sj_event+jtys_event+hjbh_event+zzrs_event+jjjc_event+gzjg_event;
            if(problem_come!=0)
                temp.setProblem_come(problem_come);
            if(problem_event!=0)
                temp.setProblem_event(problem_event);
            res.add(temp);
        }
        List<JdcWeekReportDTO> jdcWeekReportDTOS = new ArrayList<>();
        jdcWeekReportDTOS=res;
        Map<String, Object> model = new HashMap<String, Object>();
        /*
        List<List<ProblemDTO>> problems = new ArrayList<>();

        for(JdcWeekReportDTO jdcWeekReportDTO:jdcWeekReportDTOS){
            problems.add(jdcWeekReportDTO.getProblems());
        }
        for(List<ProblemDTO> p:problems){
            Integer event = p.get(0).getEvent();
            Integer come = p.get(0).getCome();
        }
        //model.put("problems",problems);

         */
        model.put("progress", jdcWeekReportDTOS);
        model.put("filler",jdcExportWeekReportExcelDTO.getName());
        model.put("remark",jdcExportWeekReportExcelDTO.getRemark());
        model.put("date_begin",sdf.format(jdcExportWeekReportExcelDTO.getBeginDate()));
        model.put("date_end",sdf.format(jdcExportWeekReportExcelDTO.getEndDate()));
        model.put("year",sdf.format(jdcExportWeekReportExcelDTO.getBeginDate()).substring(0,5));
        //加载excel模板文件
        File file = null;
        String rootPath = null;
        try {
            rootPath = ResourceUtils.getURL("classpath:").getPath();
            file = ResourceUtils.getFile(rootPath+"excel/接待处周报模板.xlsx");
        } catch (FileNotFoundException e) {
        }

        //配置下载路径
        String path = rootPath+"excel/tempFiles/";
        ExportExcelUtils.createDir(new File(path));
        String name=orgService.getById(jdcExportWeekReportExcelDTO.getJdc_id()).getName();
        model.put("jdc_name",name);

        name+="("+sdf.format(jdcExportWeekReportExcelDTO.getBeginDate())+"至"+sdf.format(jdcExportWeekReportExcelDTO.getEndDate())+")";
        //根据模板生成新的excel
        File excelFile = ExportExcelUtils.createNewFile(model, file, path,name);
        fileList.add(excelFile);
        return excelFile;
    }
    @Autowired
    private OrgMapper orgMapper;
    @Autowired
    private Org_placeMapper org_placeMapper;
    @Override
    public List<JdcBriefingDTO> jdcExportBriefing(String begin_date,String end_date){
        List<JdcBriefingDTO> jdcBriefingDTOS = new ArrayList<>();
        //List<String> jdc1places = Arrays.asList("市直部门","新区","经开","净月","莲花山","汽");
        List<String> jdc1places = org_placeMapper.getPlace_nameByOrgName("接待一处");
        Org jdc1 = orgMapper.getByName("接待一处");
        for(String jdc1place:jdc1places){
            List<DictPetitionEventPlace> dictPetitionEventPlaces = dictPetitionEventPlaceService.getByName(jdc1place);
            for(DictPetitionEventPlace dictPetitionEventPlace:dictPetitionEventPlaces) {
                Boolean find = false;
                JdcBriefingDTO res = new JdcBriefingDTO();
                if(begin_date!=null&&begin_date.equals("''"))
                    begin_date = null;
                if(end_date!=null&&end_date.equals("''"))
                    end_date = null;
                List<PetitionEventInfo> events = petitionEventInfoService.getPetitionEventInfosByEventPlace(jdc1.getId(),dictPetitionEventPlace.getId(),begin_date,end_date);
                if(events.size()!=0)
                    res.setSum_event(events.size());
                Integer sum_come=0;
                for(PetitionEventInfo event:events){
                    sum_come+=event.getComeNum();
                }
                if(sum_come!=0)
                    res.setSum_come(sum_come);
                Integer sum_event=0;
                sum_come=0;
                for(PetitionEventInfo event:events){
                    if(event.getIsGroupVisit()==1){
                        sum_come+=event.getComeNum();
                        sum_event++;
                    }
                }
                if(sum_event!=0)
                    res.setGroup_event(sum_event);
                if(sum_come!=0)
                    res.setGroup_come(sum_come);
                if(events.size()!=0){
                    if(sum_come!=0)
                        res.setSingle_come(res.getSum_come()-res.getGroup_come());
                    if(sum_event!=0)
                        res.setSingle_event(res.getSum_event()-res.getGroup_event());
                }
                sum_come=0;
                sum_event=0;
                for(PetitionEventInfo event:events){
                    if(event.getIsRepeat()==0){
                        sum_come+=event.getComeNum();
                        sum_event++;
                    }
                }
                if(sum_come!=0)
                    res.setFirst_come(sum_come);
                if(sum_event!=0)
                    res.setFirst_event(sum_event);
                //return res;
                JdcBriefingDTO jdcBriefingDTO = res;
                jdcBriefingDTO.setName(jdc1place);
                jdcBriefingDTO.setJdc_name(jdc1.getName());
                for(JdcBriefingDTO temp:jdcBriefingDTOS){
                    if(temp.getName().equals(jdc1place)){
                        find=true;
                        if(jdcBriefingDTO.getSum_come()!=null)
                            temp.setSum_come(temp.getSum_come()+jdcBriefingDTO.getSum_come());
                        if(jdcBriefingDTO.getSum_event()!=null)
                            temp.setSum_event(temp.getSum_event()+jdcBriefingDTO.getSum_event());
                        if(jdcBriefingDTO.getFirst_event()!=null)
                            temp.setFirst_event(temp.getFirst_event()+jdcBriefingDTO.getFirst_event());
                        if(jdcBriefingDTO.getFirst_come()!=null)
                            temp.setFirst_come(temp.getFirst_come()+jdcBriefingDTO.getFirst_come());
                        if(jdcBriefingDTO.getSingle_come()!=null)
                            temp.setSingle_come(temp.getSingle_come()+jdcBriefingDTO.getSingle_come());
                        if(jdcBriefingDTO.getSingle_event()!=null)
                            temp.setSingle_event(temp.getSingle_event()+jdcBriefingDTO.getSingle_event());
                        if(jdcBriefingDTO.getGroup_come()!=null)
                            temp.setGroup_come(temp.getGroup_come()+jdcBriefingDTO.getGroup_come());
                        if(jdcBriefingDTO.getGroup_event()!=null)
                            temp.setGroup_event(temp.getGroup_event()+jdcBriefingDTO.getGroup_event());
                        break;
                    }
                }
                if(find==false)
                    jdcBriefingDTOS.add(jdcBriefingDTO);
            }
        }
        List<String> jdc2places = org_placeMapper.getPlace_nameByOrgName("接待二处");
        //List<String> jdc2places = Arrays.asList("朝阳","南关","宽城","二道","绿园","双阳","九台","榆树","德惠","农安");
        Org jdc2 = orgMapper.getByName("接待二处");
        for(String jdc2place:jdc2places){
            List<DictPetitionEventPlace> dictPetitionEventPlaces = dictPetitionEventPlaceService.getByName(jdc2place);
            for(DictPetitionEventPlace dictPetitionEventPlace:dictPetitionEventPlaces) {
                if (jdc2 != null && dictPetitionEventPlace != null) {
                    JdcBriefingDTO res = new JdcBriefingDTO();
                    if(begin_date!=null&&begin_date.equals("''"))
                        begin_date = null;
                    if(end_date!=null&&end_date.equals("''"))
                        end_date = null;
                    List<PetitionEventInfo> events = petitionEventInfoService.getPetitionEventInfosByEventPlace(jdc2.getId(),dictPetitionEventPlace.getId(),begin_date,end_date);
                    if(events.size()!=0)
                        res.setSum_event(events.size());
                    Integer sum_come=0;
                    for(PetitionEventInfo event:events){
                        sum_come+=event.getComeNum();
                    }
                    if(sum_come!=0)
                        res.setSum_come(sum_come);
                    Integer sum_event=0;
                    sum_come=0;
                    for(PetitionEventInfo event:events){
                        if(event.getIsGroupVisit()==1){
                            sum_come+=event.getComeNum();
                            sum_event++;
                        }
                    }
                    if(sum_event!=0)
                        res.setGroup_event(sum_event);
                    if(sum_come!=0)
                        res.setGroup_come(sum_come);
                    if(events.size()!=0){
                        if(sum_come!=0)
                            res.setSingle_come(res.getSum_come()-res.getGroup_come());
                        if(sum_event!=0)
                            res.setSingle_event(res.getSum_event()-res.getGroup_event());
                    }
                    sum_come=0;
                    sum_event=0;
                    for(PetitionEventInfo event:events){
                        if(event.getIsRepeat()==0){
                            sum_come+=event.getComeNum();
                            sum_event++;
                        }
                    }
                    if(sum_come!=0)
                        res.setFirst_come(sum_come);
                    if(sum_event!=0)
                        res.setFirst_event(sum_event);
                    //return res;
                    JdcBriefingDTO jdcBriefingDTO = res;
                    jdcBriefingDTO.setName(jdc2place);
                    jdcBriefingDTO.setJdc_name(jdc2.getName());
                    Boolean find = false;
                    for(JdcBriefingDTO temp:jdcBriefingDTOS){
                        if(temp.getName().equals(jdc2place)){
                            find=true;
                            if(jdcBriefingDTO.getSum_come()!=null)
                                temp.setSum_come(temp.getSum_come()+jdcBriefingDTO.getSum_come());
                            if(jdcBriefingDTO.getSum_event()!=null)
                                temp.setSum_event(temp.getSum_event()+jdcBriefingDTO.getSum_event());
                            if(jdcBriefingDTO.getFirst_event()!=null)
                                temp.setFirst_event(temp.getFirst_event()+jdcBriefingDTO.getFirst_event());
                            if(jdcBriefingDTO.getFirst_come()!=null)
                                temp.setFirst_come(temp.getFirst_come()+jdcBriefingDTO.getFirst_come());
                            if(jdcBriefingDTO.getSingle_come()!=null)
                                temp.setSingle_come(temp.getSingle_come()+jdcBriefingDTO.getSingle_come());
                            if(jdcBriefingDTO.getSingle_event()!=null)
                                temp.setSingle_event(temp.getSingle_event()+jdcBriefingDTO.getSingle_event());
                            if(jdcBriefingDTO.getGroup_come()!=null)
                                temp.setGroup_come(temp.getGroup_come()+jdcBriefingDTO.getGroup_come());
                            if(jdcBriefingDTO.getGroup_event()!=null)
                                temp.setGroup_event(temp.getGroup_event()+jdcBriefingDTO.getGroup_event());
                            break;
                        }
                    }
                    if(find==false)
                        jdcBriefingDTOS.add(jdcBriefingDTO);
                }
            }
        }
        List<String> ztdw_names = org_placeMapper.getPlace_nameByOrgName("驻厅单位");
        //List<String> ztdw_names = Arrays.asList("政法委","法院","公安局","司法局","人社局","建委","住房局","卫生局","农委","国资委","妇联");
        for(String ztdw_name:ztdw_names){
            Org ztdw = orgMapper.getByName(ztdw_name);
            if(ztdw!=null){
                JdcBriefingDTO res = new JdcBriefingDTO();
                if(begin_date!=null&&begin_date.equals("''"))
                    begin_date = null;
                if(end_date!=null&&end_date.equals("''"))
                    end_date = null;
                List<PetitionEventInfo> events = petitionEventInfoService.getPetitionEventInfosByOrg(ztdw.getId(),begin_date,end_date);
                if(events.size()!=0)
                    res.setSum_event(events.size());
                Integer sum_come=0;
                for(PetitionEventInfo event:events){
                    sum_come+=event.getComeNum();
                }
                if(sum_come!=0)
                    res.setSum_come(sum_come);
                Integer sum_event=0;
                sum_come=0;
                for(PetitionEventInfo event:events){
                    if(event.getIsGroupVisit()==1){
                        sum_come+=event.getComeNum();
                        sum_event++;
                    }
                }
                if(sum_event!=0)
                    res.setGroup_event(sum_event);
                if(sum_come!=0)
                    res.setGroup_come(sum_come);
                if(events.size()!=0){
                    if(sum_come!=0)
                        res.setSingle_come(res.getSum_come()-res.getGroup_come());
                    if(sum_event!=0)
                        res.setSingle_event(res.getSum_event()-res.getGroup_event());
                }
                sum_come=0;
                sum_event=0;
                for(PetitionEventInfo event:events){
                    if(event.getIsRepeat()==0){
                        sum_come+=event.getComeNum();
                        sum_event++;
                    }
                }
                if(sum_come!=0)
                    res.setFirst_come(sum_come);
                if(sum_event!=0)
                    res.setFirst_event(sum_event);
                //return res;
                JdcBriefingDTO jdcBriefingDTO = res;
                //JdcBriefingDTO jdcBriefingDTO = orgService.getJdcBriefingDTOByOrg(ztdw.getId(),begin_date,end_date);
                jdcBriefingDTO.setName(ztdw_name);
                jdcBriefingDTO.setJdc_name("驻厅单位");
                jdcBriefingDTOS.add(jdcBriefingDTO);
            }
        }
        for(JdcBriefingDTO jdcBriefingDTO:jdcBriefingDTOS){
            if(jdcBriefingDTO.getSum_event()==0)
                jdcBriefingDTO.setSum_event(null);
            if(jdcBriefingDTO.getSum_come()==0)
                jdcBriefingDTO.setSum_come(null);
            if(jdcBriefingDTO.getFirst_event()==0)
                jdcBriefingDTO.setFirst_event(null);
            if(jdcBriefingDTO.getFirst_come()==0)
                jdcBriefingDTO.setFirst_come(null);
            if(jdcBriefingDTO.getGroup_event()==0)
                jdcBriefingDTO.setGroup_event(null);
            if(jdcBriefingDTO.getGroup_come()==0)
                jdcBriefingDTO.setGroup_come(null);
            if(jdcBriefingDTO.getSingle_come()==0)
                jdcBriefingDTO.setSingle_come(null);
            if(jdcBriefingDTO.getSingle_event()==0)
                jdcBriefingDTO.setSingle_event(null);
        }
        return jdcBriefingDTOS;
    }

    @Override
    public File jdcExportBriefing_ExcelFileList(String begin_date,String end_date){
        List<File> fileList=new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<JdcBriefingDTO> jdcBriefingDTOS = new ArrayList<>();

        //List<String> jdc1places = Arrays.asList("市直部门","新区","经开","净月","莲花山","汽");
        List<String> jdc1places = org_placeMapper.getPlace_nameByOrgName("接待一处");
        Org jdc1 = orgMapper.getByName("接待一处");
        for(String jdc1place:jdc1places){
            List<DictPetitionEventPlace> dictPetitionEventPlaces = dictPetitionEventPlaceService.getByName(jdc1place);
            for(DictPetitionEventPlace dictPetitionEventPlace:dictPetitionEventPlaces) {
                JdcBriefingDTO res = new JdcBriefingDTO();
                if(begin_date!=null&&begin_date.equals("''"))
                    begin_date = null;
                if(end_date!=null&&end_date.equals("''"))
                    end_date = null;
                List<PetitionEventInfo> events = petitionEventInfoService.getPetitionEventInfosByEventPlace(jdc1.getId(),dictPetitionEventPlace.getId(),begin_date,end_date);
                if(events.size()!=0)
                    res.setSum_event(events.size());
                Integer sum_come=0;
                for(PetitionEventInfo event:events){
                    sum_come+=event.getComeNum();
                }
                if(sum_come!=0)
                    res.setSum_come(sum_come);
                Integer sum_event=0;
                sum_come=0;
                for(PetitionEventInfo event:events){
                    if(event.getIsGroupVisit()==1){
                        sum_come+=event.getComeNum();
                        sum_event++;
                    }
                }
                if(sum_event!=0)
                    res.setGroup_event(sum_event);
                if(sum_come!=0)
                    res.setGroup_come(sum_come);
                if(events.size()!=0){
                    if(sum_come!=0)
                        res.setSingle_come(res.getSum_come()-res.getGroup_come());
                    if(sum_event!=0)
                        res.setSingle_event(res.getSum_event()-res.getGroup_event());
                }
                sum_come=0;
                sum_event=0;
                for(PetitionEventInfo event:events){
                    if(event.getIsRepeat()==0){
                        sum_come+=event.getComeNum();
                        sum_event++;
                    }
                }
                if(sum_come!=0)
                    res.setFirst_come(sum_come);
                if(sum_event!=0)
                    res.setFirst_event(sum_event);
                //return res;
                JdcBriefingDTO jdcBriefingDTO = res;
                //JdcBriefingDTO jdcBriefingDTO = orgService.getJdcBriefingDTO(jdc1.getId(), dictPetitionEventPlace.getId(), begin_date, end_date);
                jdcBriefingDTO.setName(jdc1place);
                jdcBriefingDTO.setJdc_name(jdc1.getName());
                Boolean find = false;
                for(JdcBriefingDTO temp:jdcBriefingDTOS){
                    if(temp.getName().equals(jdc1place)){
                        find=true;
                        if(jdcBriefingDTO.getSum_come()!=null)
                            temp.setSum_come(temp.getSum_come()+jdcBriefingDTO.getSum_come());
                        if(jdcBriefingDTO.getSum_event()!=null)
                            temp.setSum_event(temp.getSum_event()+jdcBriefingDTO.getSum_event());
                        if(jdcBriefingDTO.getFirst_event()!=null)
                            temp.setFirst_event(temp.getFirst_event()+jdcBriefingDTO.getFirst_event());
                        if(jdcBriefingDTO.getFirst_come()!=null)
                            temp.setFirst_come(temp.getFirst_come()+jdcBriefingDTO.getFirst_come());
                        if(jdcBriefingDTO.getSingle_come()!=null)
                            temp.setSingle_come(temp.getSingle_come()+jdcBriefingDTO.getSingle_come());
                        if(jdcBriefingDTO.getSingle_event()!=null)
                            temp.setSingle_event(temp.getSingle_event()+jdcBriefingDTO.getSingle_event());
                        if(jdcBriefingDTO.getGroup_come()!=null)
                            temp.setGroup_come(temp.getGroup_come()+jdcBriefingDTO.getGroup_come());
                        if(jdcBriefingDTO.getGroup_event()!=null)
                            temp.setGroup_event(temp.getGroup_event()+jdcBriefingDTO.getGroup_event());
                        break;
                    }
                }
                if(find==false)
                    jdcBriefingDTOS.add(jdcBriefingDTO);
            }
        }
        List<String> jdc2places = org_placeMapper.getPlace_nameByOrgName("接待二处");
        //List<String> jdc2places = Arrays.asList("朝阳","南关","宽城","二道","绿园","双阳","九台","榆树","德惠","农安");
        Org jdc2 = orgMapper.getByName("接待二处");
        for(String jdc2place:jdc2places){
            List<DictPetitionEventPlace> dictPetitionEventPlaces = dictPetitionEventPlaceService.getByName(jdc2place);
            for(DictPetitionEventPlace dictPetitionEventPlace:dictPetitionEventPlaces) {
                if (jdc2 != null && dictPetitionEventPlace != null) {
                    JdcBriefingDTO res = new JdcBriefingDTO();
                    if(begin_date!=null&&begin_date.equals("''"))
                        begin_date = null;
                    if(end_date!=null&&end_date.equals("''"))
                        end_date = null;
                    List<PetitionEventInfo> events = petitionEventInfoService.getPetitionEventInfosByEventPlace(jdc2.getId(),dictPetitionEventPlace.getId(),begin_date,end_date);
                    if(events.size()!=0)
                        res.setSum_event(events.size());
                    Integer sum_come=0;
                    for(PetitionEventInfo event:events){
                        sum_come+=event.getComeNum();
                    }
                    if(sum_come!=0)
                        res.setSum_come(sum_come);
                    Integer sum_event=0;
                    sum_come=0;
                    for(PetitionEventInfo event:events){
                        if(event.getIsGroupVisit()==1){
                            sum_come+=event.getComeNum();
                            sum_event++;
                        }
                    }
                    if(sum_event!=0)
                        res.setGroup_event(sum_event);
                    if(sum_come!=0)
                        res.setGroup_come(sum_come);
                    if(events.size()!=0){
                        if(sum_come!=0)
                            res.setSingle_come(res.getSum_come()-res.getGroup_come());
                        if(sum_event!=0)
                            res.setSingle_event(res.getSum_event()-res.getGroup_event());
                    }
                    sum_come=0;
                    sum_event=0;
                    for(PetitionEventInfo event:events){
                        if(event.getIsRepeat()==0){
                            sum_come+=event.getComeNum();
                            sum_event++;
                        }
                    }
                    if(sum_come!=0)
                        res.setFirst_come(sum_come);
                    if(sum_event!=0)
                        res.setFirst_event(sum_event);
                    //return res;
                    JdcBriefingDTO jdcBriefingDTO = res;
                    //JdcBriefingDTO jdcBriefingDTO = orgService.getJdcBriefingDTO(jdc2.getId(), dictPetitionEventPlace.getId(), begin_date, end_date);
                    jdcBriefingDTO.setName(jdc2place);
                    jdcBriefingDTO.setJdc_name("接待二处");
                    Boolean find = false;
                    for(JdcBriefingDTO temp:jdcBriefingDTOS){
                        if(temp.getName().equals(jdc2place)){
                            find=true;
                            if(jdcBriefingDTO.getSum_come()!=null)
                                temp.setSum_come(temp.getSum_come()+jdcBriefingDTO.getSum_come());
                            if(jdcBriefingDTO.getSum_event()!=null)
                                temp.setSum_event(temp.getSum_event()+jdcBriefingDTO.getSum_event());
                            if(jdcBriefingDTO.getFirst_event()!=null)
                                temp.setFirst_event(temp.getFirst_event()+jdcBriefingDTO.getFirst_event());
                            if(jdcBriefingDTO.getFirst_come()!=null)
                                temp.setFirst_come(temp.getFirst_come()+jdcBriefingDTO.getFirst_come());
                            if(jdcBriefingDTO.getSingle_come()!=null)
                                temp.setSingle_come(temp.getSingle_come()+jdcBriefingDTO.getSingle_come());
                            if(jdcBriefingDTO.getSingle_event()!=null)
                                temp.setSingle_event(temp.getSingle_event()+jdcBriefingDTO.getSingle_event());
                            if(jdcBriefingDTO.getGroup_come()!=null)
                                temp.setGroup_come(temp.getGroup_come()+jdcBriefingDTO.getGroup_come());
                            if(jdcBriefingDTO.getGroup_event()!=null)
                                temp.setGroup_event(temp.getGroup_event()+jdcBriefingDTO.getGroup_event());
                            break;
                        }
                    }
                    if(find==false)
                        jdcBriefingDTOS.add(jdcBriefingDTO);
                }
            }
        }
        //市中法-法院，规自局没有，市建委-建委，卫健委-卫生局，农业农村局-农委，医保局没有，社保局没有
        List<String> ztdw_names = org_placeMapper.getPlace_nameByOrgName("驻厅单位");
        //List<String> ztdw_names = Arrays.asList("政法委","法院","公安局","司法局","人社局","建委","住房局","卫生局","农委","国资委","妇联");
        for(String ztdw_name:ztdw_names){
            Org ztdw = orgMapper.getByName(ztdw_name);
            if(ztdw!=null){
                JdcBriefingDTO res = new JdcBriefingDTO();
                if(begin_date!=null&&begin_date.equals("''"))
                    begin_date = null;
                if(end_date!=null&&end_date.equals("''"))
                    end_date = null;
                List<PetitionEventInfo> events = petitionEventInfoService.getPetitionEventInfosByOrg(ztdw.getId(),begin_date,end_date);
                if(events.size()!=0)
                    res.setSum_event(events.size());
                Integer sum_come=0;
                for(PetitionEventInfo event:events){
                    sum_come+=event.getComeNum();
                }
                if(sum_come!=0)
                    res.setSum_come(sum_come);
                Integer sum_event=0;
                sum_come=0;
                for(PetitionEventInfo event:events){
                    if(event.getIsGroupVisit()==1){
                        sum_come+=event.getComeNum();
                        sum_event++;
                    }
                }
                if(sum_event!=0)
                    res.setGroup_event(sum_event);
                if(sum_come!=0)
                    res.setGroup_come(sum_come);
                if(events.size()!=0){
                    if(sum_come!=0)
                        res.setSingle_come(res.getSum_come()-res.getGroup_come());
                    if(sum_event!=0)
                        res.setSingle_event(res.getSum_event()-res.getGroup_event());
                }
                sum_come=0;
                sum_event=0;
                for(PetitionEventInfo event:events){
                    if(event.getIsRepeat()==0){
                        sum_come+=event.getComeNum();
                        sum_event++;
                    }
                }
                if(sum_come!=0)
                    res.setFirst_come(sum_come);
                if(sum_event!=0)
                    res.setFirst_event(sum_event);
                //return res;
                JdcBriefingDTO jdcBriefingDTO = res;
                //JdcBriefingDTO jdcBriefingDTO = orgService.getJdcBriefingDTOByOrg(ztdw.getId(),begin_date,end_date);
                jdcBriefingDTO.setName(ztdw_name);
                jdcBriefingDTO.setJdc_name("驻厅单位");
                jdcBriefingDTOS.add(jdcBriefingDTO);
            }
        }
        JdcBriefingDTO jdcBriefingDTO = new JdcBriefingDTO();
        Integer sum_come=0;
        Integer sum_event=0;
        Integer group_event=0;
        Integer group_come=0;
        Integer single_come=0;
        Integer single_event=0;
        Integer first_event=0;
        Integer first_come=0;
        for(JdcBriefingDTO item:jdcBriefingDTOS){
            if(item.getSum_come()!=null)
                sum_come+=item.getSum_come();
            if(item.getSum_event()!=null)
                sum_event+=item.getSum_event();
            if(item.getGroup_event()!=null)
                group_event+=item.getGroup_event();
            if(item.getGroup_come()!=null)
                group_come+=item.getGroup_come();
            if(item.getSingle_come()!=null)
                single_come+=item.getSingle_come();
            if(item.getSingle_event()!=null)
                single_event+=item.getSingle_event();
            if(item.getFirst_event()!=null)
                first_event+=item.getFirst_event();
            if(item.getFirst_come()!=null)
                first_come+=item.getFirst_come();
        }
        if(sum_come!=0)
            jdcBriefingDTO.setSum_come(sum_come);
        if(sum_event!=0)
            jdcBriefingDTO.setSum_event(sum_event);
        if(group_event!=0)
            jdcBriefingDTO.setGroup_event(group_event);
        if(group_come!=0)
            jdcBriefingDTO.setGroup_come(group_come);
        if(single_come!=0)
            jdcBriefingDTO.setSingle_come(single_come);
        if(single_event!=0)
            jdcBriefingDTO.setSingle_event(single_event);
        if(first_event!=0)
            jdcBriefingDTO.setFirst_event(first_event);
        if(first_come!=0)
            jdcBriefingDTO.setFirst_come(first_come);
        jdcBriefingDTO.setName("总计");
        jdcBriefingDTOS.add(jdcBriefingDTO);
        for(JdcBriefingDTO jdcBriefingDTO1:jdcBriefingDTOS){
            if(jdcBriefingDTO1.getSum_event()==0)
                jdcBriefingDTO1.setSum_event(null);
            if(jdcBriefingDTO1.getSum_come()==0)
                jdcBriefingDTO1.setSum_come(null);
            if(jdcBriefingDTO1.getFirst_event()==0)
                jdcBriefingDTO1.setFirst_event(null);
            if(jdcBriefingDTO1.getFirst_come()==0)
                jdcBriefingDTO1.setFirst_come(null);
            if(jdcBriefingDTO1.getGroup_event()==0)
                jdcBriefingDTO1.setGroup_event(null);
            if(jdcBriefingDTO1.getGroup_come()==0)
                jdcBriefingDTO1.setGroup_come(null);
            if(jdcBriefingDTO1.getSingle_come()==0)
                jdcBriefingDTO1.setSingle_come(null);
            if(jdcBriefingDTO1.getSingle_event()==0)
                jdcBriefingDTO1.setSingle_event(null);
        }
        Map<String, Object> model = new HashMap<String, Object>();

        model.put("progress", jdcBriefingDTOS);
        model.put("date_begin",begin_date);
        model.put("date_end",end_date);
        model.put("jdc1","接待一处");
        //加载excel模板文件
        File file = null;
        String rootPath = null;
        try {
            rootPath = ResourceUtils.getURL("classpath:").getPath();
            file = ResourceUtils.getFile(rootPath+"excel/信访接待中心工作简报模板1.xlsx");
        } catch (FileNotFoundException e) {
        }

        //配置下载路径
        String path = rootPath+"excel/tempFiles/";
        ExportExcelUtils.createDir(new File(path));
        String name="信访接待中心工作简报";
        if(begin_date!=null&&end_date!=null)
        name+="("+begin_date+"至"+end_date+")";
        //根据模板生成新的excel
        File excelFile = ExportExcelUtils.createNewFile(model, file, path,name);

        fileList.add(excelFile);
        return excelFile;
    }
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean addPep(PetitionEventProgress pep){
        pep.setUserId(LoginHelper.getAccount().getId());
        return petitionEventProgressService.save(pep)
                && petitionEventInfoService.update(new UpdateWrapper<PetitionEventInfo>().set("status","2").set("solve_state","1").eq("id",pep.getPetitionEventInfoId()));
    }

    @Override
    public void exportExcelNew(String begin_date,String end_date,HttpServletResponse response) {

        List<File> fileList=new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<JdcBriefingDTO> jdcBriefingDTOS = new ArrayList<>();

        //List<String> jdc1places = Arrays.asList("市直部门","新区","经开","净月","莲花山","汽");
        List<String> jdc1places = org_placeMapper.getPlace_nameByOrgName("接待一处");
        Org jdc1 = orgMapper.getByName("接待一处");
        for(String jdc1place:jdc1places){
            List<DictPetitionEventPlace> dictPetitionEventPlaces = dictPetitionEventPlaceService.getByName(jdc1place);
            for(DictPetitionEventPlace dictPetitionEventPlace:dictPetitionEventPlaces) {
                JdcBriefingDTO res = new JdcBriefingDTO();
                if(begin_date!=null&&begin_date.equals("''"))
                    begin_date = null;
                if(end_date!=null&&end_date.equals("''"))
                    end_date = null;
                List<PetitionEventInfo> events = petitionEventInfoService.getPetitionEventInfosByEventPlace(jdc1.getId(),dictPetitionEventPlace.getId(),begin_date,end_date);
                if(events.size()!=0)
                    res.setSum_event(events.size());
                Integer sum_come=0;
                for(PetitionEventInfo event:events){
                    sum_come+=event.getComeNum();
                }
                if(sum_come!=0)
                    res.setSum_come(sum_come);
                Integer sum_event=0;
                sum_come=0;
                for(PetitionEventInfo event:events){
                    if(event.getIsGroupVisit()==1){
                        sum_come+=event.getComeNum();
                        sum_event++;
                    }
                }
                if(sum_event!=0)
                    res.setGroup_event(sum_event);
                if(sum_come!=0)
                    res.setGroup_come(sum_come);
                if(events.size()!=0){
                    if(sum_come!=0)
                        res.setSingle_come(res.getSum_come()-res.getGroup_come());
                    if(sum_event!=0)
                        res.setSingle_event(res.getSum_event()-res.getGroup_event());
                }
                sum_come=0;
                sum_event=0;
                for(PetitionEventInfo event:events){
                    if(event.getIsRepeat()==0){
                        sum_come+=event.getComeNum();
                        sum_event++;
                    }
                }
                if(sum_come!=0)
                    res.setFirst_come(sum_come);
                if(sum_event!=0)
                    res.setFirst_event(sum_event);
                //return res;
                JdcBriefingDTO jdcBriefingDTO = res;
                //JdcBriefingDTO jdcBriefingDTO = orgService.getJdcBriefingDTO(jdc1.getId(), dictPetitionEventPlace.getId(), begin_date, end_date);
                jdcBriefingDTO.setName(jdc1place);
                jdcBriefingDTO.setJdc_name(jdc1.getName());
                Boolean find = false;
                for(JdcBriefingDTO temp:jdcBriefingDTOS){
                    if(temp.getName().equals(jdc1place)){
                        find=true;
                        if(jdcBriefingDTO.getSum_come()!=null)
                            temp.setSum_come(temp.getSum_come()+jdcBriefingDTO.getSum_come());
                        if(jdcBriefingDTO.getSum_event()!=null)
                            temp.setSum_event(temp.getSum_event()+jdcBriefingDTO.getSum_event());
                        if(jdcBriefingDTO.getFirst_event()!=null)
                            temp.setFirst_event(temp.getFirst_event()+jdcBriefingDTO.getFirst_event());
                        if(jdcBriefingDTO.getFirst_come()!=null)
                            temp.setFirst_come(temp.getFirst_come()+jdcBriefingDTO.getFirst_come());
                        if(jdcBriefingDTO.getSingle_come()!=null)
                            temp.setSingle_come(temp.getSingle_come()+jdcBriefingDTO.getSingle_come());
                        if(jdcBriefingDTO.getSingle_event()!=null)
                            temp.setSingle_event(temp.getSingle_event()+jdcBriefingDTO.getSingle_event());
                        if(jdcBriefingDTO.getGroup_come()!=null)
                            temp.setGroup_come(temp.getGroup_come()+jdcBriefingDTO.getGroup_come());
                        if(jdcBriefingDTO.getGroup_event()!=null)
                            temp.setGroup_event(temp.getGroup_event()+jdcBriefingDTO.getGroup_event());
                        break;
                    }
                }
                if(find==false)
                    jdcBriefingDTOS.add(jdcBriefingDTO);
            }
        }
        List<String> jdc2places = org_placeMapper.getPlace_nameByOrgName("接待二处");
        //List<String> jdc2places = Arrays.asList("朝阳","南关","宽城","二道","绿园","双阳","九台","榆树","德惠","农安");
        Org jdc2 = orgMapper.getByName("接待二处");
        for(String jdc2place:jdc2places){
            List<DictPetitionEventPlace> dictPetitionEventPlaces = dictPetitionEventPlaceService.getByName(jdc2place);
            for(DictPetitionEventPlace dictPetitionEventPlace:dictPetitionEventPlaces) {
                if (jdc2 != null && dictPetitionEventPlace != null) {
                    JdcBriefingDTO res = new JdcBriefingDTO();
                    if(begin_date!=null&&begin_date.equals("''"))
                        begin_date = null;
                    if(end_date!=null&&end_date.equals("''"))
                        end_date = null;
                    List<PetitionEventInfo> events = petitionEventInfoService.getPetitionEventInfosByEventPlace(jdc2.getId(),dictPetitionEventPlace.getId(),begin_date,end_date);
                    if(events.size()!=0)
                        res.setSum_event(events.size());
                    Integer sum_come=0;
                    for(PetitionEventInfo event:events){
                        sum_come+=event.getComeNum();
                    }
                    if(sum_come!=0)
                        res.setSum_come(sum_come);
                    Integer sum_event=0;
                    sum_come=0;
                    for(PetitionEventInfo event:events){
                        if(event.getIsGroupVisit()==1){
                            sum_come+=event.getComeNum();
                            sum_event++;
                        }
                    }
                    if(sum_event!=0)
                        res.setGroup_event(sum_event);
                    if(sum_come!=0)
                        res.setGroup_come(sum_come);
                    if(events.size()!=0){
                        if(sum_come!=0)
                            res.setSingle_come(res.getSum_come()-res.getGroup_come());
                        if(sum_event!=0)
                            res.setSingle_event(res.getSum_event()-res.getGroup_event());
                    }
                    sum_come=0;
                    sum_event=0;
                    for(PetitionEventInfo event:events){
                        if(event.getIsRepeat()==0){
                            sum_come+=event.getComeNum();
                            sum_event++;
                        }
                    }
                    if(sum_come!=0)
                        res.setFirst_come(sum_come);
                    if(sum_event!=0)
                        res.setFirst_event(sum_event);
                    //return res;
                    JdcBriefingDTO jdcBriefingDTO = res;
                    //JdcBriefingDTO jdcBriefingDTO = orgService.getJdcBriefingDTO(jdc2.getId(), dictPetitionEventPlace.getId(), begin_date, end_date);
                    jdcBriefingDTO.setName(jdc2place);
                    jdcBriefingDTO.setJdc_name("接待二处");
                    Boolean find = false;
                    for(JdcBriefingDTO temp:jdcBriefingDTOS){
                        if(temp.getName().equals(jdc2place)){
                            find=true;
                            if(jdcBriefingDTO.getSum_come()!=null)
                                temp.setSum_come(temp.getSum_come()+jdcBriefingDTO.getSum_come());
                            if(jdcBriefingDTO.getSum_event()!=null)
                                temp.setSum_event(temp.getSum_event()+jdcBriefingDTO.getSum_event());
                            if(jdcBriefingDTO.getFirst_event()!=null)
                                temp.setFirst_event(temp.getFirst_event()+jdcBriefingDTO.getFirst_event());
                            if(jdcBriefingDTO.getFirst_come()!=null)
                                temp.setFirst_come(temp.getFirst_come()+jdcBriefingDTO.getFirst_come());
                            if(jdcBriefingDTO.getSingle_come()!=null)
                                temp.setSingle_come(temp.getSingle_come()+jdcBriefingDTO.getSingle_come());
                            if(jdcBriefingDTO.getSingle_event()!=null)
                                temp.setSingle_event(temp.getSingle_event()+jdcBriefingDTO.getSingle_event());
                            if(jdcBriefingDTO.getGroup_come()!=null)
                                temp.setGroup_come(temp.getGroup_come()+jdcBriefingDTO.getGroup_come());
                            if(jdcBriefingDTO.getGroup_event()!=null)
                                temp.setGroup_event(temp.getGroup_event()+jdcBriefingDTO.getGroup_event());
                            break;
                        }
                    }
                    if(find==false)
                        jdcBriefingDTOS.add(jdcBriefingDTO);
                }
            }
        }
        //市中法-法院，规自局没有，市建委-建委，卫健委-卫生局，农业农村局-农委，医保局没有，社保局没有
        List<String> ztdw_names = org_placeMapper.getPlace_nameByOrgName("驻厅单位");
        //List<String> ztdw_names = Arrays.asList("政法委","法院","公安局","司法局","人社局","建委","住房局","卫生局","农委","国资委","妇联");
        for(String ztdw_name:ztdw_names){
            Org ztdw = orgMapper.getByName(ztdw_name);
            if(ztdw!=null){
                JdcBriefingDTO res = new JdcBriefingDTO();
                if(begin_date!=null&&begin_date.equals("''"))
                    begin_date = null;
                if(end_date!=null&&end_date.equals("''"))
                    end_date = null;
                List<PetitionEventInfo> events = petitionEventInfoService.getPetitionEventInfosByOrg(ztdw.getId(),begin_date,end_date);
                if(events.size()!=0)
                    res.setSum_event(events.size());
                Integer sum_come=0;
                for(PetitionEventInfo event:events){
                    sum_come+=event.getComeNum();
                }
                if(sum_come!=0)
                    res.setSum_come(sum_come);
                Integer sum_event=0;
                sum_come=0;
                for(PetitionEventInfo event:events){
                    if(event.getIsGroupVisit()==1){
                        sum_come+=event.getComeNum();
                        sum_event++;
                    }
                }
                if(sum_event!=0)
                    res.setGroup_event(sum_event);
                if(sum_come!=0)
                    res.setGroup_come(sum_come);
                if(events.size()!=0){
                    if(sum_come!=0)
                        res.setSingle_come(res.getSum_come()-res.getGroup_come());
                    if(sum_event!=0)
                        res.setSingle_event(res.getSum_event()-res.getGroup_event());
                }
                sum_come=0;
                sum_event=0;
                for(PetitionEventInfo event:events){
                    if(event.getIsRepeat()==0){
                        sum_come+=event.getComeNum();
                        sum_event++;
                    }
                }
                if(sum_come!=0)
                    res.setFirst_come(sum_come);
                if(sum_event!=0)
                    res.setFirst_event(sum_event);
                //return res;
                JdcBriefingDTO jdcBriefingDTO = res;
                //JdcBriefingDTO jdcBriefingDTO = orgService.getJdcBriefingDTOByOrg(ztdw.getId(),begin_date,end_date);
                jdcBriefingDTO.setName(ztdw_name);
                jdcBriefingDTO.setJdc_name("驻厅单位");
                jdcBriefingDTOS.add(jdcBriefingDTO);
            }
        }
        JdcBriefingDTO jdcBriefingDTO = new JdcBriefingDTO();
        Integer sum_come=0;
        Integer sum_event=0;
        Integer group_event=0;
        Integer group_come=0;
        Integer single_come=0;
        Integer single_event=0;
        Integer first_event=0;
        Integer first_come=0;
        for(JdcBriefingDTO item:jdcBriefingDTOS){
            if(item.getSum_come()!=null)
                sum_come+=item.getSum_come();
            if(item.getSum_event()!=null)
                sum_event+=item.getSum_event();
            if(item.getGroup_event()!=null)
                group_event+=item.getGroup_event();
            if(item.getGroup_come()!=null)
                group_come+=item.getGroup_come();
            if(item.getSingle_come()!=null)
                single_come+=item.getSingle_come();
            if(item.getSingle_event()!=null)
                single_event+=item.getSingle_event();
            if(item.getFirst_event()!=null)
                first_event+=item.getFirst_event();
            if(item.getFirst_come()!=null)
                first_come+=item.getFirst_come();
        }
        if(sum_come!=0)
            jdcBriefingDTO.setSum_come(sum_come);
        if(sum_event!=0)
            jdcBriefingDTO.setSum_event(sum_event);
        if(group_event!=0)
            jdcBriefingDTO.setGroup_event(group_event);
        if(group_come!=0)
            jdcBriefingDTO.setGroup_come(group_come);
        if(single_come!=0)
            jdcBriefingDTO.setSingle_come(single_come);
        if(single_event!=0)
            jdcBriefingDTO.setSingle_event(single_event);
        if(first_event!=0)
            jdcBriefingDTO.setFirst_event(first_event);
        if(first_come!=0)
            jdcBriefingDTO.setFirst_come(first_come);
        jdcBriefingDTO.setName("总计");
        jdcBriefingDTO.setJdc_name("总计");
        jdcBriefingDTOS.add(jdcBriefingDTO);
        for(JdcBriefingDTO jdcBriefingDTO1:jdcBriefingDTOS){
            if(jdcBriefingDTO1.getSum_event()==0)
                jdcBriefingDTO1.setSum_event(null);
            if(jdcBriefingDTO1.getSum_come()==0)
                jdcBriefingDTO1.setSum_come(null);
            if(jdcBriefingDTO1.getFirst_event()==0)
                jdcBriefingDTO1.setFirst_event(null);
            if(jdcBriefingDTO1.getFirst_come()==0)
                jdcBriefingDTO1.setFirst_come(null);
            if(jdcBriefingDTO1.getGroup_event()==0)
                jdcBriefingDTO1.setGroup_event(null);
            if(jdcBriefingDTO1.getGroup_come()==0)
                jdcBriefingDTO1.setGroup_come(null);
            if(jdcBriefingDTO1.getSingle_come()==0)
                jdcBriefingDTO1.setSingle_come(null);
            if(jdcBriefingDTO1.getSingle_event()==0)
                jdcBriefingDTO1.setSingle_event(null);
        }


        /** 第一步，创建一个Workbook，对应一个Excel文件  */
        HSSFWorkbook wb = new HSSFWorkbook();

        /** 第二步，在Workbook中添加一个sheet,对应Excel文件中的sheet  */
        HSSFSheet sheet = wb.createSheet("信访接待中心简报");

        /** 第三步，设置样式以及字体样式*/
        HSSFCellStyle titleStyle = createTitleCellStyle(wb);
        HSSFCellStyle timeStyle = createTimeCellStyle(wb);
        HSSFCellStyle headerStyle = createHeadCellStyle(wb);
        HSSFCellStyle contentStyle = createContentCellStyle(wb);
        //列宽自适应
        for(int i=0;i<10;i++){
            sheet.autoSizeColumn(i,true);
        }
        //第一列和第二列设置宽度
        sheet.setColumnWidth(0,12*256);
        sheet.setColumnWidth(1,12*256);


        /** 第四步，创建标题 ,合并标题单元格 */
        // 行号
        int rowNum = 0;
        // 创建第一页的第一行，索引从0开始
        HSSFRow row0 = sheet.createRow(rowNum++);
        row0.setHeight((short) 800);// 设置行高

        String title = "信访接待中心简报";
        HSSFCell c00 = row0.createCell(0);
        c00.setCellValue(title);
        c00.setCellStyle(titleStyle);
        // 合并单元格，参数依次为起始行，结束行，起始列，结束列 （索引0开始）
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));

        // 第二行
        HSSFRow row1 = sheet.createRow(rowNum++);
        row1.setHeight((short) 500);
        String time = "统计时间：2019-01-01 - 2019-02-28";
        HSSFCell c16 = row1.createCell(6);
        c16.setCellValue(time);
        c16.setCellStyle(timeStyle);
        // 合并
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 9));

        //第三行
        HSSFRow row2 = sheet.createRow(rowNum++);
        row1.setHeight((short) 400);
        String[] row_second = {"单位", "", "总量", "", "集体访", "", "单访", "", "初访", ""};
        for (int i = 0; i < row_second.length; i++) {
            HSSFCell tempCell = row2.createCell(i);
            tempCell.setCellValue(row_second[i]);
            tempCell.setCellStyle(headerStyle);
        }
        // 合并
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 3));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 4, 5));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 7));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 8, 9));

        //第四行
        HSSFRow row3 = sheet.createRow(rowNum++);
        row1.setHeight((short) 500);
        String[] row_third = {"", "", "件次", "人次", "件次", "人次", "件次", "人次", "件次", "人次"};
        for (int i = 0; i < row_third.length; i++) {
            HSSFCell tempCell = row3.createCell(i);
            tempCell.setCellValue(row_third[i]);
            tempCell.setCellStyle(headerStyle);
        }

        //数据行
        //所有数据
        List<JdcBriefingDTO> dtos = new ArrayList<>();
        //接待一处数据
        List<JdcBriefingDTO> dtos1 = new ArrayList<>();
        //接待二处数据
        List<JdcBriefingDTO> dtos2 = new ArrayList<>();
        //驻厅单位数据
        List<JdcBriefingDTO> dtos3 = new ArrayList<>();

        /*//添加测试数据
        JdcBriefingDTO dto1 = new JdcBriefingDTO();
        dto1.setJdc_name("接待一处");
        dto1.setName("市直部门");
        dtos.add(dto1);
        JdcBriefingDTO dto2 = new JdcBriefingDTO();
        dto2.setJdc_name("接待一处");
        dto2.setName("新区");
        dtos.add(dto2);
        //todo 添加其他接待处数据
        */
        //分解数据
        for(JdcBriefingDTO dto:jdcBriefingDTOS){
            String jdcName = dto.getJdc_name();
            if(jdcName.equals("接待一处")){
                dtos1.add(dto);
            }else if(jdcName.equals("接待二处")){
                dtos2.add(dto);
            }else if(jdcName.equals("驻厅单位")){
                dtos3.add(dto);
            }
        }

        //合并行定位
        int jdc1FirstRow = 4;
        int jdc1endRow = jdc1FirstRow+dtos1.size()-1;
        int jdc2FirstRow = jdc1endRow+1;
        int jdc2endRow = jdc2FirstRow+dtos2.size()-1;
        int jdc3FirstRow = jdc2endRow+1;
        int jdc3endRow = jdc3FirstRow+dtos3.size()-1;

        //接待一处
        for(JdcBriefingDTO dto:dtos1){
            HSSFRow row = sheet.createRow(rowNum++);
            row1.setHeight((short) 400);
            if(jdc1FirstRow == rowNum-1){
                HSSFCell c0 = row.createCell(0);
                c0.setCellValue(dto.getJdc_name());
                c0.setCellStyle(contentStyle);
            }
            HSSFCell c1 = row.createCell(1);
            c1.setCellValue(dto.getName());
            c1.setCellStyle(contentStyle);
            HSSFCell c2 = row.createCell(2);
            if(dto.getSum_event()!=null)
                c2.setCellValue(dto.getSum_event());
            c2.setCellStyle(contentStyle);
            HSSFCell c3 = row.createCell(3);
            if(dto.getSum_come()!=null)
                c3.setCellValue(dto.getSum_come());
            c3.setCellStyle(contentStyle);
            HSSFCell c4 = row.createCell(4);
            if(dto.getGroup_event()!=null)
                c4.setCellValue(dto.getGroup_event());
            c4.setCellStyle(contentStyle);
            HSSFCell c5 = row.createCell(5);
            if(dto.getGroup_come()!=null)
                c5.setCellValue(dto.getGroup_come());
            c5.setCellStyle(contentStyle);
            HSSFCell c6 = row.createCell(6);
            if(dto.getSingle_event()!=null)
                c6.setCellValue(dto.getSingle_event());
            c6.setCellStyle(contentStyle);
            HSSFCell c7 = row.createCell(7);
            if(dto.getSingle_come()!=null)
                c7.setCellValue(dto.getSingle_come());
            c7.setCellStyle(contentStyle);
            HSSFCell c8 = row.createCell(8);
            if(dto.getFirst_event()!=null)
                c8.setCellValue(dto.getFirst_event());
            c8.setCellStyle(contentStyle);
            HSSFCell c9 = row.createCell(9);
            if(dto.getFirst_come()!=null)
                c9.setCellValue(dto.getFirst_come());
            c9.setCellStyle(contentStyle);
        }
        //接待二处
        for(JdcBriefingDTO dto:dtos2){
            HSSFRow row = sheet.createRow(rowNum++);
            row1.setHeight((short) 400);
            if(jdc2FirstRow == rowNum-1){
                HSSFCell c0 = row.createCell(0);
                c0.setCellValue(dto.getJdc_name());
                c0.setCellStyle(contentStyle);
            }
            HSSFCell c1 = row.createCell(1);
            c1.setCellValue(dto.getName());
            c1.setCellStyle(contentStyle);
            HSSFCell c2 = row.createCell(2);
            if(dto.getSum_event()!=null)
                c2.setCellValue(dto.getSum_event());
            c2.setCellStyle(contentStyle);
            HSSFCell c3 = row.createCell(3);
            if(dto.getSum_come()!=null)
                c3.setCellValue(dto.getSum_come());
            c3.setCellStyle(contentStyle);
            HSSFCell c4 = row.createCell(4);
            if(dto.getGroup_event()!=null)
                c4.setCellValue(dto.getGroup_event());
            c4.setCellStyle(contentStyle);
            HSSFCell c5 = row.createCell(5);
            if(dto.getGroup_come()!=null)
                c5.setCellValue(dto.getGroup_come());
            c5.setCellStyle(contentStyle);
            HSSFCell c6 = row.createCell(6);
            if(dto.getSingle_event()!=null)
                c6.setCellValue(dto.getSingle_event());
            c6.setCellStyle(contentStyle);
            HSSFCell c7 = row.createCell(7);
            if(dto.getSingle_come()!=null)
                c7.setCellValue(dto.getSingle_come());
            c7.setCellStyle(contentStyle);
            HSSFCell c8 = row.createCell(8);
            if(dto.getFirst_event()!=null)
                c8.setCellValue(dto.getFirst_event());
            c8.setCellStyle(contentStyle);
            HSSFCell c9 = row.createCell(9);
            if(dto.getFirst_come()!=null)
                c9.setCellValue(dto.getFirst_come());
            c9.setCellStyle(contentStyle);
        }
        //驻厅单位
        for(JdcBriefingDTO dto:dtos3){
            HSSFRow row = sheet.createRow(rowNum++);
            row1.setHeight((short) 400);
            if(jdc3FirstRow == rowNum-1){
                HSSFCell c0 = row.createCell(0);
                c0.setCellValue(dto.getJdc_name());
                c0.setCellStyle(contentStyle);
            }
            HSSFCell c1 = row.createCell(1);
            c1.setCellValue(dto.getName());
            c1.setCellStyle(contentStyle);
            HSSFCell c2 = row.createCell(2);
            if(dto.getSum_event()!=null)
                c2.setCellValue(dto.getSum_event());
            c2.setCellStyle(contentStyle);
            HSSFCell c3 = row.createCell(3);
            if(dto.getSum_come()!=null)
                c3.setCellValue(dto.getSum_come());
            c3.setCellStyle(contentStyle);
            HSSFCell c4 = row.createCell(4);
            if(dto.getGroup_event()!=null)
                c4.setCellValue(dto.getGroup_event());
            c4.setCellStyle(contentStyle);
            HSSFCell c5 = row.createCell(5);
            if(dto.getGroup_come()!=null)
                c5.setCellValue(dto.getGroup_come());
            c5.setCellStyle(contentStyle);
            HSSFCell c6 = row.createCell(6);
            if(dto.getSingle_event()!=null)
                c6.setCellValue(dto.getSingle_event());
            c6.setCellStyle(contentStyle);
            HSSFCell c7 = row.createCell(7);
            if(dto.getSingle_come()!=null)
                c7.setCellValue(dto.getSingle_come());
            c7.setCellStyle(contentStyle);
            HSSFCell c8 = row.createCell(8);
            if(dto.getFirst_event()!=null)
                c8.setCellValue(dto.getFirst_event());
            c8.setCellStyle(contentStyle);
            HSSFCell c9 = row.createCell(9);
            if(dto.getFirst_come()!=null)
                c9.setCellValue(dto.getFirst_come());
            c9.setCellStyle(contentStyle);
        }

        // 合并
        if(jdc1FirstRow < jdc1endRow) sheet.addMergedRegion(new CellRangeAddress(jdc1FirstRow, jdc1endRow, 0, 0));
        if(jdc2FirstRow < jdc2endRow) sheet.addMergedRegion(new CellRangeAddress(jdc2FirstRow, jdc2endRow, 0, 0));
        if(jdc3FirstRow < jdc3endRow) sheet.addMergedRegion(new CellRangeAddress(jdc3FirstRow, jdc3endRow, 0, 0));

        //合计
        HSSFRow lastRow = sheet.createRow(rowNum++);
        row1.setHeight((short) 500);
        String[] last_row = {"合计", "", "1", "2", "3", "4", "5", "6", "7", "8"};
        int end = jdcBriefingDTOS.size()-1;
        for(JdcBriefingDTO sum:jdcBriefingDTOS){
            if(sum.getName().equals("总计")){
                last_row[2]=jdcBriefingDTOS.get(end).getSum_event().toString();
                last_row[3]=jdcBriefingDTOS.get(end).getSum_come().toString();
                last_row[4]=jdcBriefingDTOS.get(end).getGroup_event().toString();
                last_row[5]=jdcBriefingDTOS.get(end).getGroup_come().toString();
                last_row[6]=jdcBriefingDTOS.get(end).getSingle_event().toString();
                last_row[7]=jdcBriefingDTOS.get(end).getSingle_come().toString();
                last_row[8]=jdcBriefingDTOS.get(end).getFirst_event().toString();
                last_row[9]=jdcBriefingDTOS.get(end).getFirst_come().toString();
                break;
            }
        }
        for (int i = 0; i < last_row.length; i++) {
            HSSFCell tempCell = lastRow.createCell(i);
            tempCell.setCellValue(last_row[i]);
            tempCell.setCellStyle(contentStyle);
        }
        // 合并
        sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 0, 1));



        //导出
        String fileName = "信访接待中心简报.xls";
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8"); //xls类型
            response.setHeader("Content-disposition", "attachment;filename=" + ExportExcelUtils.encodeToUTF8(fileName));
            response.setHeader("Pragma", "No-cache");
            OutputStream stream = response.getOutputStream();
            if (null != wb && null != stream) {
                wb.write(stream);// 将数据写出去
                wb.close();
                stream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建标题样式
     * @param wb
     * @return
     */
    private HSSFCellStyle createTitleCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直对齐

        HSSFFont headerFont1 = (HSSFFont) wb.createFont(); // 创建字体样式
        headerFont1.setBold(true); //字体加粗
        headerFont1.setFontHeightInPoints((short) 15); // 设置字体大小
        cellStyle.setFont(headerFont1); // 为标题样式设置字体样式

        return cellStyle;
    }
    /**
     * 创建表头样式
     * @param wb
     * @return
     */
    private HSSFCellStyle createTimeCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setWrapText(true);// 设置自动换行
        cellStyle.setAlignment(HorizontalAlignment.RIGHT); //水平居右
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //垂直对齐

        HSSFFont headerFont = (HSSFFont) wb.createFont(); // 创建字体样式
        headerFont.setFontHeightInPoints((short) 10); // 设置字体大小
        cellStyle.setFont(headerFont); // 为标题样式设置字体样式

        return cellStyle;
    }
    /**
     * 创建时间表头样式
     * @param wb
     * @return
     */
    private HSSFCellStyle createHeadCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setWrapText(true);// 设置自动换行
        cellStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //垂直对齐

        HSSFFont headerFont = (HSSFFont) wb.createFont(); // 创建字体样式
        headerFont.setFontHeightInPoints((short) 12); // 设置字体大小
        cellStyle.setFont(headerFont); // 为标题样式设置字体样式

        return cellStyle;
    }
    /**
     * 创建内容样式
     * @param wb
     * @return
     */
    private HSSFCellStyle createContentCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        cellStyle.setWrapText(true);// 设置自动换行
        /*cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
        cellStyle.setBorderRight(BorderStyle.THIN); //右边框
        cellStyle.setBorderTop(BorderStyle.THIN); //上边框*/

        // 生成12号字体
        HSSFFont font = wb.createFont();
        font.setColor((short)8);
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);


        return cellStyle;
    }
}
