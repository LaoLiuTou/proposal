package com.fangao.dev.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fangao.dev.core.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 随访人信息表
 * </p>
 *
 * @author ykb
 * @since 2019-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("follow_person")
public class FollowPerson extends BaseEntity {

    @ApiModelProperty(value = "身份证号")
    @TableId()
    private String idcard;

}
