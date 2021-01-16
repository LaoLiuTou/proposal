package com.fangao.dev.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class JdcBriefingGetDTO {
    @ApiModelProperty("开始时间")
    Date BeginDate;

    @ApiModelProperty("结束时间")
    Date endDate;
}
