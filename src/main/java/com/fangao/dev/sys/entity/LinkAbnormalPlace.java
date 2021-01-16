package com.fangao.dev.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 异常地事项关系表
 * </p>
 *
 * @author jobob
 * @since 2018-10-21
 */
@Data
@Accessors(chain = true)
@TableName("link_abnormal_place")
public class LinkAbnormalPlace {

    @ApiModelProperty("事项 ID")
    private Long eventId;

    @ApiModelProperty("异常地点 ID")
    private Long placeId;
}
