package com.fangao.dev.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//接待处导出周报Excel
@Data
@Accessors(chain = true)
public class JdcExportWeekReportExcelDTO {
    @ApiModelProperty("选中的接待处id")
    Long Jdc_id;

    @ApiModelProperty("填表人名字")
    String name;

    @ApiModelProperty("备注")
    String remark;

    @ApiModelProperty("开始时间")
    Date BeginDate;

    @ApiModelProperty("结束时间")
    Date endDate;

    @ApiModelProperty("开关状态")
    Boolean switch_status;
}
