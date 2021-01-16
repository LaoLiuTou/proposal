package com.fangao.dev.sys.dto;


import com.fangao.dev.sys.entity.PetitionEventInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 审核信息
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */

@Data
@Accessors(chain = true)
public class CheckDTO extends PetitionEventInfo {

    @ApiModelProperty("事项id")
    private Long petitionEventInfoId;

    @ApiModelProperty("添加人ID")
    private Long userId;

    @ApiModelProperty("审核状态:0表示待审核，1、审核通过，2、审核不通过")
    private int status = 0;

    @ApiModelProperty("申请删除人真实姓名")
    private String userRealName;

    @ApiModelProperty(value = "负责单位名称，用于展示")
    private String dutyUnitName;

    @ApiModelProperty(value = "内容分类名称，用于展示")
    private String contentTypeName;

    @ApiModelProperty("搜索框 删除提交开始日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date beginSubmitDate;

    @ApiModelProperty("搜索框 删除提交结束日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date endSubmitDate;

}
