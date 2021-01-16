package com.fangao.dev.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 异常行为事项关系表
 * </p>
 *
 * @author jobob
 * @since 2018-10-21
 */
@Data
@Accessors(chain = true)
@TableName("link_abnormal_action")
public class LinkAbnormalAction {

    @ApiModelProperty("事项 ID")
    private Long eventId;

    @ApiModelProperty("异常行为 ID")
    private Long actionId;
}
