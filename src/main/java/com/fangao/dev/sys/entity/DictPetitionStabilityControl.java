package com.fangao.dev.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fangao.dev.core.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *     稳控单位字典表
 * </p>
 *
 * @author 49030
 * @date 2019/5/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("dict_petition_stability_control")
public class DictPetitionStabilityControl extends BaseEntity{
    @ApiModelProperty(value = "单位名称")
    private String name;

    @ApiModelProperty(value = "单位电话")
    private String mobileNo;

    @ApiModelProperty(value = "状态  -1、禁用 0、正常")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
