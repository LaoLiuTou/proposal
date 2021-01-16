package com.fangao.dev.sys.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fangao.dev.core.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("delete_check")
public class DeleteCheck extends BaseEntity {

    @ApiModelProperty("事项id")
    private Long petitionEventInfoId;

    @ApiModelProperty("添加人ID")
    private Long userId;

    @ApiModelProperty("审核状态:0表示待审核，1、审核通过，2、审核不通过")
    private int status = 0;

}
