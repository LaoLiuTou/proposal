package com.fangao.dev.sys.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

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
public class CountPetitonerNumDTO {

    /**
     * 横轴 日期
     */
    @ApiModelProperty(value = "来访日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date visitDate;

    /**
     * 纵轴 数量
     */
    @ApiModelProperty(value = "信访者数量")
    private int number;

}
