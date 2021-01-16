package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.LinkReceptorOrg;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 接待员组织关联表 服务类
 * </p>
 *
 */
public interface ILinkReceptorOrgService extends IService<LinkReceptorOrg> {


    /**
     * <p>
     * 更新接待员组织关联信息
     * </p>
     *
     * @param receptorId 接待员 ID
     * @param ids    组织 ID 集合
     * @return
     */
    boolean updateByIds(Long receptorId, List<Long> ids);


    /**
     * <p>
     * 删除接待员组织关系
     * </p>
     *
     * @param receptorId 接待员 ID
     * @return
     */
    boolean removeByReceptorId(Serializable receptorId);
}
