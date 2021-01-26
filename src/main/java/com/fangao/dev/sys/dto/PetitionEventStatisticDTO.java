package com.fangao.dev.sys.dto;

import com.fangao.dev.core.bean.SuperEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
public class PetitionEventStatisticDTO extends SuperEntity {
    @ApiModelProperty(value = "信访件编号")
    private String eventNum;
    @ApiModelProperty(value = "来访日期")
    private String visitDate;
    @ApiModelProperty(value = "姓名")
    private String petitioner;
    @ApiModelProperty(value = "接待处")
    private String receptionOrg;
    @ApiModelProperty(value = "接待人员")
    private String receptor;
    @ApiModelProperty(value = "事项名称")
    private String name;
    @ApiModelProperty(value = "一级问题分类")
    private String firstContentType;
    @ApiModelProperty(value = "反映问题")
    private String eventContent;
    @ApiModelProperty(value = "事项状态，用于展示")
    private String statusName;

}
