package com.fangao.dev.sys.entity;

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
 * 上级表扬记录表
 * </p>
 *
 * @author jobob
 * @since 2018-10-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("petition_event_extra_praise")
public class PetitionEventExtraPraise extends BaseEntity {

    @ApiModelProperty(value = "记录")
    private String content;

    @ApiModelProperty("日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date contentDate;

    @ApiModelProperty("事项id")
    private Long eventId;

    @ApiModelProperty(value = "状态  -1、禁用 0、正常")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
