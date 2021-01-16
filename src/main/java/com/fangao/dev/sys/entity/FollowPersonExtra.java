package com.fangao.dev.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fangao.dev.core.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 随访人信息额外表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("follow_person_extra")
public class FollowPersonExtra extends BaseEntity {

    @ApiModelProperty(value = "事件id")
    private Long eventId;

    @ApiModelProperty(value = "身份证号")
    private String idcard;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "性别 1、男，0、女")
    private Integer sex;

    @ApiModelProperty(value = "民族")
    private String nation;

    @ApiModelProperty(value = "出生日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date birthDate;

    @ApiModelProperty(value = "户籍地址")
    private String permanentAddress;

    @ApiModelProperty(value = "现住地址")
    private String currentAddress;

    @ApiModelProperty(value = "手机号")
    private String mobileNo;

}
