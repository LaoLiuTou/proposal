package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.dto.*;
import com.fangao.dev.sys.entity.*;
import com.fangao.dev.sys.mapper.PetitionEventInfoMapper;
import com.fangao.dev.sys.service.*;
import com.fangao.dev.sys.vo.DrawPetitionCatIntervalVO;
import com.fangao.dev.sys.vo.DrawPetitionPieVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 信访事件表
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */
@Service
public class PetitionEventInfoServiceImpl extends ServiceImpl<PetitionEventInfoMapper, PetitionEventInfo> implements IPetitionEventInfoService {

    @Autowired
    private IFollowPersonService followPersonService;
    @Autowired
    private IPetitionEventExtraPraiseService petitionEventExtraPraiseService;
    @Autowired
    private IPetitionEventExtraMeetingService petitionEventExtraMeetingService;
    @Autowired
    private IPetitionEventExtraRevisitService petitionEventExtraRevisitService;
    @Autowired
    private IPetitionEventExtraHandleService petitionEventExtraHandleService;
    @Override
    public boolean checkCanDelete(String fieldName, List<Serializable> listValue) {
        int count = count(new QueryWrapper<PetitionEventInfo>().in(fieldName, listValue));
        if(count>0){
            return false;
        }
        return true;
    }

    @Override
    public IPage<JdcDTO> djdrPage(Page page, JdcDTO jdcDTO) {
        List<JdcDTO> list = baseMapper.selectWaiterByReceptionOrgId(page,jdcDTO);
        page.setRecords(list);
        return page;
    }

    @Override
    public Long selectAllEventTotal(JdcDTO jdcDTO) {
        return baseMapper.selectAllEventTotal(jdcDTO);
    }

    @Override
    public IPage<JdcDTO> xxglPage(Page page, JdcDTO jdcDTO) {
        /*page.setTotal(selectAllEventTotal(jdcDTO));*/
        List<JdcDTO> list = baseMapper.selectAllEvent(page,jdcDTO);
        page.setRecords(list);
        return page;
    }
    @Override
    public IPage<JdcDTO> statisPetitionEventInfos(Page page, JdcDTO jdcDTO) {
        /*page.setTotal(selectAllEventTotal(jdcDTO));*/
        List<JdcDTO> list = baseMapper.statisPetitionEventInfos(page,jdcDTO);
        page.setRecords(list);
        return page;
    }


    @Override
    public List<Map<String,Object>> xxglPageExportQuery(JdcDTO jdcDTO) {
        return baseMapper.selectAllEventExport(jdcDTO);
    }
    @Override
    public List<Map<String,Object>> statisExportQuery(JdcDTO jdcDTO) {
        return baseMapper.exportStatisPetitionEventInfos(jdcDTO);
    }

    @Override
    public List<JdcDTO> getRepeatList(JdcDTO jdcDTO) {
        return (jdcDTO.getName() == null
                && jdcDTO.getEventPlaceId() == null
                && jdcDTO.getIdcard() == null)?new ArrayList<>():baseMapper.getRepeatList(jdcDTO);
    }

    @Override
    public List<JdcDTO> getRepeatListNew(List<Long> ids) {
        return baseMapper.getRepeatListNew(ids);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public JdcDTO getEventEditData(Long id) {
        JdcDTO dto = baseMapper.getEventEditData(id);
        dto.setFollowers(followPersonService.getFollowPersonListByEventId(id));
        return dto;
    }

    @Override
    public List<PetitionCountDTO> countByDatePart(String start_date, String end_date,int status) {
        return baseMapper.countByDatePart(start_date,end_date,status);
    }

    @Override
    public List<PetitionLineCountDTO> countByDatePart2(String start_date, String end_date, int status) {
        return baseMapper.countByDatePart2(start_date,end_date,status);
    }

    @Override
    public List<DrawPetitionPieVO> countJDPie(){
        return baseMapper.countJDPie();
    }

    @Override
    public List<DrawPetitionPieVO> countJDPlacePie() {
        return baseMapper.countJDPlacePie();
    }

    @Override
    public List<DrawPetitionPieVO> countJDUnitPie() {
        return baseMapper.countJDUnitPie();
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public List<DrawPetitionCatIntervalVO> countContentTypeInterval() {
        List<DrawPetitionCatIntervalVO> list = new ArrayList<>();
        List<LeafContentTypeDTO> contentTypeDTOS = baseMapper.queryLeafContentType();
        HashMap<Long,Boolean> idMap = new HashMap<>();
        HashMap<Long,Long> leafCount = new HashMap<>();
        for(LeafContentTypeDTO c:contentTypeDTOS){
            leafCount.put(c.getId(),c.getCount());
            String[] id_arr = c.getIds().split(",");
            for(String id:id_arr){
                idMap.put(Long.valueOf(id),true);
            }
        }
        List<ContentTypeDTO> items = baseMapper.queryContentType(idMap.keySet());
        if(items != null && items.size() > 0){
            HashMap<Long,ContentTypeDTO> itemMap = new HashMap<>();
            HashMap<Long,ContentTypeDTO> topMap = new HashMap<>();
            for(ContentTypeDTO item:items){
                if(leafCount.get(item.getId()) != null){
                    item.setCount(leafCount.get(item.getId()));
                    if(item.getPid() == 0){
                        item.setS_name(item.getName());
                    }
                }
                itemMap.put(item.getId(),item);
            }
            for(Long id : itemMap.keySet()){
                ContentTypeDTO temp = itemMap.get(id);
                sumCount(temp,itemMap,topMap);
            }
            HashMap<Long,Boolean> topFlag = new HashMap<>();
            List<ContentTypeDTO> tempList = new ArrayList<>();
            for(Long id : itemMap.keySet()){
                ContentTypeDTO temp = itemMap.get(id);
                ContentTypeDTO p_temp = itemMap.get(temp.getPid());
                if(p_temp != null && p_temp.getPid() == 0){
                    topFlag.put(p_temp.getId(),false);
                    temp.setS_name(p_temp.getName());
                    tempList.add(temp);
                }
            }
            for(Long id : topMap.keySet()){
                ContentTypeDTO temp = topMap.get(id);
                if(topFlag.get(temp.getId()) == null){
                    temp.setS_name(temp.getName());
                    tempList.add(temp);
                }
            }
            Collections.sort(tempList);
            for(ContentTypeDTO item:tempList){
                list.add(new DrawPetitionCatIntervalVO(item.getName(),item.getCount(),item.getS_name()));
            }
        }
        return list;
    }

    @Override
    public PetitionEventShowPrintDTO getEventInfoShow(long id) {
        PetitionEventShowPrintDTO temp = baseMapper.queryEventInfoShow(id);
        List<PetitionEventExtraPraise> extraPraise = petitionEventExtraPraiseService.list(new QueryWrapper<PetitionEventExtraPraise>().eq("status",0).eq("event_id",id).orderByAsc("content_date"));
        List<PetitionEventExtraHandle> extraHandle = petitionEventExtraHandleService.list(new QueryWrapper<PetitionEventExtraHandle>().eq("status",0).eq("event_id",id).orderByAsc("content_date"));
        List<PetitionEventExtraMeeting> extraMeeting = petitionEventExtraMeetingService.list(new QueryWrapper<PetitionEventExtraMeeting>().eq("status",0).eq("event_id",id).orderByAsc("content_date"));
        List<PetitionEventExtraRevisit> extraRevisit = petitionEventExtraRevisitService.list(new QueryWrapper<PetitionEventExtraRevisit>().eq("status",0).eq("event_id",id).orderByAsc("content_date"));
        temp.setExtraPraise(CollectionUtils.isNotEmpty(extraPraise)?extraPraise:new ArrayList<>());
        temp.setExtraHandle(CollectionUtils.isNotEmpty(extraHandle)?extraHandle:new ArrayList<>());
        temp.setExtraMeeting(CollectionUtils.isNotEmpty(extraMeeting)?extraMeeting:new ArrayList<>());
        temp.setExtraRevisit(CollectionUtils.isNotEmpty(extraRevisit)?extraRevisit:new ArrayList<>());
        return temp;
    }

    private void sumCount(ContentTypeDTO temp, HashMap<Long,ContentTypeDTO> itemMap, HashMap<Long,ContentTypeDTO> topMap){
        if(temp.getPid() != 0){
            ContentTypeDTO p = itemMap.get(temp.getPid());
            long count = p.getCount()+temp.getCount();
            p.setCount(count);
            itemMap.put(p.getId(),p);
            sumCount(p,itemMap,topMap);
        }else{
            topMap.put(temp.getId(),temp);
        }
    }

    @Override
    public List<StatisDTO> statisticsQueryEveryDay(String start, String end) {
        return baseMapper.statisticsQueryEveryDay(start,end);
    }

    @Override
    public List<StatisDTO> statisticsQueryReceptionOrg(String start, String end) {
        return baseMapper.statisticsQueryReceptionOrg(start,end);
    }

    @Override
    public List<StatisDTO> statisticsQueryEventPlace(String start, String end) {
        return baseMapper.statisticsQueryEventPlace(start,end);
    }

    @Override
    public List<StatisDTO> statisticsQueryDutyUnit(String start, String end) {
        return baseMapper.statisticsQueryDutyUnit(start,end);
    }

    @Override
    public List<StatisDTO> statisticsQueryDutyCityUnit(String start, String end) {
        return baseMapper.statisticsQueryDutyCityUnit(start,end);
    }

    @Override
    public List<StatisDTO> statisticsQueryContentTypeFirst(String start, String end) {
        return baseMapper.statisticsQueryContentTypeFirst(start,end);
    }

    @Override
    public List<StatisDTO> statisticsQueryContentTypeSecond(String start, String end, Long firstId) {
        return baseMapper.statisticsQueryContentTypeSecond(start,end,firstId);
    }

    @Override
    public String statisticsQueryContentTypeIdsById(Long id) {
        return baseMapper.statisticsQueryContentTypeIdsById(id);
    }

    @Override
    public List<PetitionEventStatisticDTO> statisticsQueryEventList(StatisQueryEventDTO dto) {
        if(dto.getContentTypeId() != null){
            String [] ids = statisticsQueryContentTypeIdsById(dto.getContentTypeId()).split(",");
            List<Long> idsList = new ArrayList<>();
            for(String id:ids){
                idsList.add(Long.valueOf(id));
            }
            dto.setContentTypeIds(idsList);
        }
        return baseMapper.statisticsQueryEventList(dto);
    }


    @Override
    public List<PetitionEventStatisticDTO> statisticsQueryRepeatEventList(StatisQueryEventDTO dto) {
        return baseMapper.statisticsQueryRepeatEventList(dto);
    }
    @Override
    public List<PetitionEventInfo> getPetitionEventInfos(long orgId, long event_place, String begin,String end){
        return baseMapper.getPetitionEventInfos(orgId,event_place,begin, end);
    }
    @Override
    public List<PetitionEventInfo> getPetitionEventInfosByEventPlace(long org_id,long event_place_id,String begin_Date,String end_Date){
        return baseMapper.getPetitionEventInfosByEventPlace(org_id,event_place_id,begin_Date,end_Date);
    }
    @Override
    public List<PetitionEventInfo> getPetitionEventInfosByOrg(long org_id,String begin_Date,String end_Date){
        return baseMapper.getPetitionEventInfosByOrg(org_id,begin_Date,end_Date);
    }
}
