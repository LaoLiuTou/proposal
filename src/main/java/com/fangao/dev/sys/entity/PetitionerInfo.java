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
 * 访问人信息表
 * </p>
 *
 * @author ykb
 * @since 2019-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("petitioner_info")
public class PetitionerInfo extends BaseEntity {

    @ApiModelProperty("主键 ID")
    @TableId(type= IdType.AUTO)
    @TableField(exist=false)
    private Long id;

    @ApiModelProperty(value = "身份证号")
    @TableId()
    private String idcard;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "性别 0、男，1、女")
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
