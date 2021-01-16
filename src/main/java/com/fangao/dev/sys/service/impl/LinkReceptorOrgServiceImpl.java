package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.LinkReceptorOrg;
import com.fangao.dev.sys.mapper.LinkReceptorOrgMapper;
import com.fangao.dev.sys.service.ILinkReceptorOrgService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 接待员组织关联表 服务实现类
 * </p>
 *
 */
@Service
public class LinkReceptorOrgServiceImpl extends ServiceImpl<LinkReceptorOrgMapper, LinkReceptorOrg> implements ILinkReceptorOrgService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateByIds(Long receptorId, List<Long> ids) {
        Assert.fail(null == receptorId || CollectionUtils.isEmpty(ids), "所属组织 不能为空");
        return removeByReceptorId(receptorId) && saveBatch(ids.stream().map(id -> {
            LinkReceptorOrg receptorOrg = new LinkReceptorOrg();
            receptorOrg.setReceptorId(receptorId);
            receptorOrg.setOrgId(id);
            return receptorOrg;
        }).collect(Collectors.toList()));
    }

    @Override
    public boolean removeByReceptorId(Serializable receptorId) {
        return super.remove(Wrappers.<LinkReceptorOrg>query().eq("receptor_id", receptorId));
    }
}
