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
 * 领导字典表
 * </p>
 *
 * @author jobob
 * @since 2018-10-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("snapshot_petition_leader")
public class SnapshotPetitionLeader extends BaseEntity {

    @ApiModelProperty(value = "领导层级id")
    private Long leaderLevelId;

    @ApiModelProperty(value = "领导层级名称 用于展示")
    @TableField(exist = false)
    private String leaderLevelName;

    @ApiModelProperty(value = "领导名称")
    private String name;

    @ApiModelProperty(value = "领导电话")
    private String mobileNo;

    @ApiModelProperty(value = "状态  -1、禁用 0、正常")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "接待处id")
    private Long orgId;
}
