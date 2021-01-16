package com.fangao.dev.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fangao.dev.core.bean.BeanConverter;
import com.fangao.dev.core.bean.SuperEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("data_province")
public class DataProvince extends SuperEntity {

    @TableField(exist=false)
    private Long id;

    @ApiModelProperty("主键 信访件编号")
    @TableId(type= IdType.INPUT)
    private String code;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("信访人数")
    private Integer number;

    @ApiModelProperty("住址")
    private String address;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("问题属地ID")
    private Long eventPlaceId;

    @ApiModelProperty("信访目的ID")
    private Long purposeId;

    @ApiModelProperty("内容分类ID")
    private Long contentTypeId;

    @ApiModelProperty("信访日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date visitDate;

    @ApiModelProperty("登记日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date checkDate;

    @ApiModelProperty("限办日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date deadlineDate;

    @ApiModelProperty("信访形式ID")
    private Long formatId;

    @ApiModelProperty("概况")
    private String description;

    @ApiModelProperty("是否办结：0、否  1、是")
    private Integer solve;

    @ApiModelProperty("是否全国初次信访：0、否 1、是")
    private Integer firstTime;

    @ApiModelProperty("联名信或集体访：0、否 1、是")
    private Integer isGroup;

    @ApiModelProperty("信访件状态ID")
    private Long statusId;

    @ApiModelProperty("登记部门ID")
    private Long orgId;

    @ApiModelProperty("登记人ID")
    private Long operatorId;

    @ApiModelProperty("问题属地")
    @TableField(exist=false)
    private String eventPlace;

    @ApiModelProperty("信访目的")
    @TableField(exist=false)
    private String purpose;

    @ApiModelProperty("内容分类")
    @TableField(exist=false)
    private String contentType;

    @ApiModelProperty("信访形式")
    @TableField(exist=false)
    private String format;

    @ApiModelProperty("信访件状态")
    @TableField(exist=false)
    private String status;

    @ApiModelProperty("登记部门")
    @TableField(exist=false)
    private String org;

    @ApiModelProperty("登记人")
    @TableField(exist=false)
    private String operator;
}
