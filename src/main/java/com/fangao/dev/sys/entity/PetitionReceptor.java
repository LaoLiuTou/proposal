package com.fangao.dev.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fangao.dev.core.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 接待员信息表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("petition_receptor")
public class PetitionReceptor extends BaseEntity {
    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "身份证号")
    private String idcard;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "状态  -1、禁用 0、正常")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "所属组织ID 用于展示")
    @TableField(exist=false)
    private String orgs;

    @ApiModelProperty(value = "所属组织名称 用于展示")
    @TableField(exist=false)
    private String orgsName;
}