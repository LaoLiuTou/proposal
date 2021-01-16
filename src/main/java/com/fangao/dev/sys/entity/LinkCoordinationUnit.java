package com.fangao.dev.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配合单位事项关系表
 * </p>
 *
 * @author jobob
 * @since 2018-10-21
 */
@Data
@Accessors(chain = true)
@TableName("link_coordination_unit")
public class LinkCoordinationUnit {

    @ApiModelProperty("事项 ID")
    private Long eventId;

    @ApiModelProperty("配合单位 ID")
    private Long unitId;
}
