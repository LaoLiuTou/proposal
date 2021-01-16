package com.fangao.dev.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//接待处导出Excel
@Data
@Accessors(chain = true)
public class JdcExportExcelDTO {
    @ApiModelProperty("选中的责任单位id列表")
    List<Long> checkedUnitIdList;

    @ApiModelProperty("是否启用时间限制")
    Boolean isUseDateLimit;

    @ApiModelProperty("开始时间")
    Date BeginDate;

    @ApiModelProperty("结束时间")
    Date endDate;
}
