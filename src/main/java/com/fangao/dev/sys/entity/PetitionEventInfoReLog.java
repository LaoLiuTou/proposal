package com.fangao.dev.sys.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fangao.dev.core.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 信访事件重访历史表
 * </p>
 *
 */

@Data
@Accessors(chain = true)
@TableName("petition_event_info_re_log")
public class PetitionEventInfoReLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键 ID")
    private Long id;

    @ApiModelProperty("重访关系表 ID")
    private Long reId;

    @ApiModelProperty("事件编号")
    private String eventNum;

    @ApiModelProperty("接待人员id")
    private String receptionId;

    @ApiModelProperty("接待处id")
    private Long receptionOrgId;

    @ApiModelProperty("登记处id")
    private Long djcOrgId;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("事件地点ID")
    private Long eventPlaceId;

    @ApiModelProperty("事件内容")
    private String eventContent;

    @ApiModelProperty("是否为重要领域: 1是，2否")
    private int isKeyArea;

    @ApiModelProperty("重要领域编号")
    private Long keyAreaId;

    @ApiModelProperty("主要诉求")
    private String mainDemand;

    @ApiModelProperty("来访人数")
    private int comeNum;

    @ApiModelProperty("涉及人数")
    private String involveNum;

    @ApiModelProperty("是否随访")
    private int isFollow;

    @ApiModelProperty("随访内容")
    private String followContent;

    @ApiModelProperty("是否滞留")
    private int isHoldUp;

    @ApiModelProperty("是否集体上访:大于5时为1")
    private int isGroupVisit;

    @ApiModelProperty("是否重访")
    private int isRepeat;

    @ApiModelProperty("内容类型id")
    private Long contentTypeId;

    @ApiModelProperty("上访地点是否异常:0异常，1正常")
    private int visitPlaceStatus=1;

    @ApiModelProperty("上访行为是否异常:0异常，1正常")
    private int visitActionStatus=1;

    @ApiModelProperty("责任单位编号")
    private String dutyUnitId;

    @ApiModelProperty("工作建议")
    private String workProposal;

    @ApiModelProperty("最新工作进展")
    private String newestProgress;

    @ApiModelProperty("是否属实:0不属实，1属实")
    private Integer isVerified;

    @ApiModelProperty("调查核实情况")
    private String verifyDetail;

    @ApiModelProperty("问题症结")
    private String crux;

    @ApiModelProperty("是否有理:0有理，1部分有理，2无理")
    private Integer isReasonable;

    @ApiModelProperty("无理理由")
    private String unreasonableReason;

    @ApiModelProperty("-1、放弃处理；0、待处理；1、处理中；2、处理完成")
    private int status = 0;

    @ApiModelProperty("是否是最终处理意见")
    private Integer isFinalOpinion;

    @ApiModelProperty("最终处理意见")
    private String finalOpinion;

    @ApiModelProperty("是否领导包干:0否，1是")
    private Integer isLeaderHad;

    @ApiModelProperty("领导id")
    private Long leaderId;

    @ApiModelProperty("主访人身份证号")
    private String idcard;

    @ApiModelProperty("上访日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date visitDate;

    @ApiModelProperty("配合单位ids")
    private String coUnitIds;

    @ApiModelProperty("异常地点ids")
    private String abnormalPlaceIds;

    @ApiModelProperty("异常行为ids")
    private String abnormalActionIds;

    @ApiModelProperty(value = "删除 0、否 1、是")
    private Boolean deleted;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime modifiedTime;

    @ApiModelProperty("稳控单位名称")
    private String wkdwName;

    @ApiModelProperty("办结状态 1 已办结 0 未办结")
    private Integer solveState;
}
