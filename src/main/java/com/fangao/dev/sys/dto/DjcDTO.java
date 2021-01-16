package com.fangao.dev.sys.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fangao.dev.sys.entity.PetitionerInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Created by wdai on 2019-3-26.
 */
@Data
@Accessors(chain = true)
public class DjcDTO extends PetitionerInfo {
    @ApiModelProperty(value = "来访日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date visitDate;

    @ApiModelProperty(value = "接待人ID")
    private String receptionId;

    @ApiModelProperty(value = "接待处ID")
    private Long receptionOrgId;

    @ApiModelProperty(value = "接待处IDs")
    private String receptionOrgIds;

    @ApiModelProperty(value = "登记处ID")
    private Long djcOrgId;

    @ApiModelProperty("事件编号")
    private String eventNum;

    @ApiModelProperty("事件ID")
    private long eventId;

    @ApiModelProperty("-1、放弃处理；0、待处理；1、处理中；2、处理完成")
    private int status;

    @ApiModelProperty("事件状态对应描述信息")
    private String statusMsg;

    @ApiModelProperty(value = "组织名称 用于展示")
    private String showOrgs;

    @ApiModelProperty(value = "组织地址")
    private String orgAddress;

    @ApiModelProperty(value = "事项状态名称")
    private String statusName;

    @ApiModelProperty(value = "删除审核状态，0：待审核，1：通过，2：驳回")
    private int deleteCheckStatus=-1;

    }
