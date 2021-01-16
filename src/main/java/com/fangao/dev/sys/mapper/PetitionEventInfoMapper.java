package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fangao.dev.sys.dto.*;
import com.fangao.dev.sys.entity.PetitionEventInfo;
import com.fangao.dev.sys.vo.DrawPetitionPieVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 信访事项表 Mapper 接口
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */

@Mapper
public interface PetitionEventInfoMapper extends BaseMapper<PetitionEventInfo> {

    List<JdcDTO> selectWaiterByReceptionOrgId(IPage<JdcDTO> page, @Param(value = "jdcDTO") JdcDTO jdcDTO);

    Long selectAllEventTotal(@Param(value = "jdcDTO") JdcDTO jdcDTO);

    List<JdcDTO> selectAllEvent(IPage<JdcDTO> page, @Param(value = "jdcDTO") JdcDTO jdcDTO);

    List<Map<String,Object>> selectAllEventExport(@Param(value = "jdcDTO") JdcDTO jdcDTO);

    PetitionEventShowPrintDTO queryEventInfoShow(@Param(value = "id") long id);

    List<JdcDTO> getRepeatList(@Param(value = "jdcDTO") JdcDTO jdcDTO);

    List<JdcDTO> getRepeatListNew(@Param(value = "ids") List<Long> ids);

    JdcDTO getEventEditData(@Param(value = "id") Long id);

    List<PetitionCountDTO> countByDatePart(@Param("start_date") String start_date, @Param("end_date") String end_date, @Param("status") int status);

    List<PetitionLineCountDTO> countByDatePart2(@Param("start_date") String start_date, @Param("end_date") String end_date, @Param("status") int status);

    List<DrawPetitionPieVO> countJDPie();

    List<DrawPetitionPieVO> countJDPlacePie();

    List<DrawPetitionPieVO> countJDUnitPie();

    List<LeafContentTypeDTO> queryLeafContentType();

    List<ContentTypeDTO> queryContentType(@Param("ids")Set ids);

    List<StatisDTO> statisticsQueryEveryDay(@Param("start")String start,@Param("end")String end);

    List<StatisDTO> statisticsQueryReceptionOrg(@Param("start")String start,@Param("end")String end);

    List<StatisDTO> statisticsQueryEventPlace(@Param("start")String start,@Param("end")String end);

    List<StatisDTO> statisticsQueryDutyUnit(@Param("start")String start,@Param("end")String end);

    List<StatisDTO> statisticsQueryDutyCityUnit(@Param("start")String start,@Param("end")String end);

    List<StatisDTO> statisticsQueryContentTypeFirst(@Param("start")String start,@Param("end")String end);

    List<StatisDTO> statisticsQueryContentTypeSecond(@Param("start")String start,@Param("end")String end,@Param("firstId")Long firstId);

    String statisticsQueryContentTypeIdsById(@Param("id")Long id);

    List<PetitionEventStatisticDTO> statisticsQueryEventList(@Param("statisQueryEventDTO")StatisQueryEventDTO dto);

    List<PetitionEventStatisticDTO> statisticsQueryRepeatEventList(@Param("statisQueryEventDTO")StatisQueryEventDTO dto);

    List<PetitionEventInfo> getPetitionEventInfos(@Param("org_Id")long orgId, @Param("event_place_id")long event_place, @Param("start_date")String begin_date, @Param("end_date")String end_date);
    List<PetitionEventInfo> getPetitionEventInfosByEventPlace(@Param("org_id")long org_id, @Param("event_place_id")long event_place_id, @Param("begin_date")String begin, @Param("end_date")String end);
    List<PetitionEventInfo> getPetitionEventInfosByOrg(@Param("org_id")long org_id, @Param("begin_date")String begin, @Param("end_date")String end);
}
