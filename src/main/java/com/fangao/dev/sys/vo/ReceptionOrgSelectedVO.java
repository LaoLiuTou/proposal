package com.fangao.dev.sys.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 登记处指定接待处 VO
 * </p>
 *
 * @author jobob
 * @since 2018-10-09
 */
@Data
@Accessors(chain = true)
public class ReceptionOrgSelectedVO {

    /**
     * 组织 ID
     */
    private Long id;
    /**
     * 组织名称
     */
    private String name;
    /**
     * 组织位置
     */
    private String address;
}
