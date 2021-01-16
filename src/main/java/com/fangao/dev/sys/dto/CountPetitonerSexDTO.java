package com.fangao.dev.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 统计信访人 DTO
 * </p>
 *
 * @author jobob
 * @since 2018-10-05
 */
@Data
@Accessors(chain = true)
public class CountPetitonerSexDTO {

    /**
     * 性别
     */
    @ApiModelProperty(value = "信访人性别")
    private String sex;

    /**
     * 比例
     */
    @ApiModelProperty(value = "信访者性别比例")
    private double sold;

}
