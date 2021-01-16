package com.fangao.dev.sys.dto;

import com.fangao.dev.sys.entity.PetitionEventInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ykb on 2019-3-27.
 */
@Data
@Accessors(chain = true)
public class JdcWeekReportDTO extends PetitionEventInfo {
    private String event_place;
   @ApiModelProperty("总量")
    private Integer sumEvents;
   @ApiModelProperty("总人数")
    private Integer sumComes;
   private Integer sum_group_visit_event;
    private Integer sum_group_visit_come;
   private Integer sum_single_visit_event;
    private Integer sum_single_visit_come;
    private Integer sum_first_group_come;
    private Integer sum_first_group_event;
    private Integer sum_first_single_come;
    private Integer sum_first_single_event;
    private Integer sum_repeat_group_come;
    private Integer sum_repeat_group_event;
    private Integer sum_repeat_single_come;
    private Integer sum_repeat_single_event;

    private Integer nmgtx_come;
    private Integer nmgtx_event;
    private Integer ncny_come;
    private Integer ncny_event;
    private Integer gtzy_come;
    private Integer gtzy_event;
    private Integer cxjs_come;
    private Integer cxjs_event;
    private Integer ldhshbz_come;
    private Integer ldhshbz_event;
    private Integer wsjs_come;
    private Integer wsjs_event;
    private Integer jywt_come;
    private Integer jywt_event;
    private Integer mz_come;
    private Integer mz_event;
    private Integer sfss_come;
    private Integer sfss_event;
    private Integer sj_come;
    private Integer sj_event;
    private Integer jtys_come;
    private Integer jtys_event;
    private Integer hjbh_come;
    private Integer hjbh_event;
    private Integer zzrs_come;
    private Integer zzrs_event;
    private Integer jjjc_come;
    private Integer jjjc_event;
    private Integer gzjg_come;
    private Integer gzjg_event;
    private Integer problem_event;
    private Integer problem_come;
    private List<ProblemDTO> problems;
}
