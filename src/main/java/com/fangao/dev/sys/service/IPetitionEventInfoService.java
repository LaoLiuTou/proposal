package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.dto.*;
import com.fangao.dev.sys.entity.PetitionEventInfo;
import com.fangao.dev.sys.vo.DrawPetitionCatIntervalVO;
import com.fangao.dev.sys.vo.DrawPetitionPieVO;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 信访事项表
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */
public interface IPetitionEventInfoService extends IService<PetitionEventInfo> {
     /*检查是否可以删除某个字典，检查主表该字段是否存在值*/
     public boolean checkCanDelete(String fieldName,List<Serializable> listValue);

     /*本接待处待接待人分页查询，按日期正序排列*/
     IPage<JdcDTO> djdrPage(Page page, JdcDTO jdcDTO);

     Long selectAllEventTotal(JdcDTO jdcDTO);

     /*接待处所有接待信息分页查询，按创建日期倒叙排列*/
     IPage<JdcDTO> xxglPage(Page page, JdcDTO jdcDTO);

     /*信访件信息导出查询*/
     List<Map<String,Object>> xxglPageExportQuery(JdcDTO jdcDTO);

     List<JdcDTO> getRepeatList(JdcDTO jdcDTO);

     List<JdcDTO> getRepeatListNew(List<Long> ids);

     JdcDTO getEventEditData(Long id);

     List<PetitionCountDTO> countByDatePart(String start_date, String end_date,int status);

     List<PetitionLineCountDTO> countByDatePart2(String start_date, String end_date, int status);

     List<DrawPetitionPieVO> countJDPie();

     List<DrawPetitionPieVO> countJDPlacePie();

     List<DrawPetitionPieVO> countJDUnitPie();

     List<DrawPetitionCatIntervalVO> countContentTypeInterval();

     PetitionEventShowPrintDTO getEventInfoShow(long id);

     List<StatisDTO> statisticsQueryEveryDay(String start,String end);

     List<StatisDTO> statisticsQueryReceptionOrg(String start,String end);

     List<StatisDTO> statisticsQueryEventPlace(String start,String end);

     List<StatisDTO> statisticsQueryDutyUnit(String start,String end);

     List<StatisDTO> statisticsQueryDutyCityUnit(String start,String end);

     List<StatisDTO> statisticsQueryContentTypeFirst(String start,String end);

     List<StatisDTO> statisticsQueryContentTypeSecond(String start,String end,Long firstId);

     String statisticsQueryContentTypeIdsById(Long id);

     List<PetitionEventStatisticDTO> statisticsQueryEventList(StatisQueryEventDTO dto);

     List<PetitionEventStatisticDTO> statisticsQueryRepeatEventList(StatisQueryEventDTO dto);
     List<PetitionEventInfo> getPetitionEventInfos(long orgId, long event_place, String begin,String end);
     List<PetitionEventInfo> getPetitionEventInfosByEventPlace(long org_id,long event_place_id,String beginDate,String  endDate);
     List<PetitionEventInfo> getPetitionEventInfosByOrg(long org_id,String beginDate,String  endDate);
}
