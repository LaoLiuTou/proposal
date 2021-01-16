package com.fangao.dev.sys.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
@Data
@Accessors(chain = true)
public class JdcBriefingDTO {
    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "总计人数")
    private Integer sum_come=0;

    @ApiModelProperty(value = "总计件数")
    private Integer sum_event=0;

    @ApiModelProperty(value = "群访件数")
    private Integer group_event=0;

    @ApiModelProperty(value = "群访人数")
    private Integer group_come=0;

    @ApiModelProperty(value = "单访人数")
    private Integer single_come=0;

    @ApiModelProperty(value = "单访件数")
    private Integer single_event=0;

    @ApiModelProperty(value = "初访件数")
    private Integer first_event=0;

    @ApiModelProperty(value = "初访人数")
    private Integer first_come=0;

    @ApiModelProperty(value = "接待处")
    private String jdc_name;
}
