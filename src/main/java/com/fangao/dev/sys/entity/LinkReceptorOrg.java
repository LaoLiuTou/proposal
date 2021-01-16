package com.fangao.dev.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fangao.dev.core.bean.SuperEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 接待员组织关系表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("link_receptor_org")
public class LinkReceptorOrg extends SuperEntity {

    @ApiModelProperty("接待员 ID")
    private Long receptorId;

    @ApiModelProperty("组织 ID")
    private Long orgId;
}