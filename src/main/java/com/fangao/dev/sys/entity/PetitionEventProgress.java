package com.fangao.dev.sys.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fangao.dev.core.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 事项进展表
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@TableName("petition_event_progress")
public class PetitionEventProgress extends BaseEntity {

    @ApiModelProperty("事项id")
    private Long petitionEventInfoId;

    @ApiModelProperty("进展内容")
    private String content;

    @ApiModelProperty("进展状态:1表示有效，0、无效")
    private int status = 0;

    @ApiModelProperty("添加人ID")
    private Long userId;

    @ApiModelProperty("责任单位编号")
    private Long unitId;

    @ApiModelProperty("是否属实:0不属实，1属实")
    private Integer isVerified;

    @ApiModelProperty("调查核实情况")
    private String verifyDetail;

    @ApiModelProperty("是否有理:0有理，1部分有理，2无理")
    private Integer isReasonable;

    @ApiModelProperty("无理理由")
    private String unreasonableReason;

    @ApiModelProperty("问题症结")
    private String crux;

    @ApiModelProperty("是否领导包干:0否，1是")
    private Integer isLeaderHad;

    @ApiModelProperty("领导id")
    private Long leaderId;

    @ApiModelProperty("是否是最终处理意见")
    private Integer isFinalOpinion;

    @ApiModelProperty("最终处理意见")
    private String finalOpinion;
}
