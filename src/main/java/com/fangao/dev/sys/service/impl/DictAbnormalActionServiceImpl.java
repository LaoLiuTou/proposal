package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.DictAbnormalAction;
import com.fangao.dev.sys.mapper.DictAbnormalActionMapper;
import com.fangao.dev.sys.service.IDictAbnormalActionService;
import com.fangao.dev.sys.service.ILinkAbnormalActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 异常行为字典表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Service
public class DictAbnormalActionServiceImpl extends ServiceImpl<DictAbnormalActionMapper, DictAbnormalAction> implements IDictAbnormalActionService {

    @Autowired
    private ILinkAbnormalActionService dictRelAbnormalActionService;

    @Override
    public IPage<DictAbnormalAction> page(Page page, DictAbnormalAction dictAbnormalAction) {
        QueryWrapper<DictAbnormalAction> qw = new QueryWrapper<>();
        qw.setEntity(dictAbnormalAction);
        return super.page(page, qw);
    }

    @Override
    public List<DictAbnormalAction> listEffective() {
        return super.list(Wrappers.<DictAbnormalAction>query().select("id", "name")
                .eq("status", 0).orderByDesc("sort"));
    }

    @Override
    public boolean save(DictAbnormalAction dictAbnormalAction) {
        if (null == dictAbnormalAction) {
            return false;
        }
        return super.save(dictAbnormalAction);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        DictAbnormalAction dictAbnormalAction = new DictAbnormalAction();
        dictAbnormalAction.setId(id);
        dictAbnormalAction.setStatus(status.intValue() > -1 ? 0 : -1);
        return updateById(dictAbnormalAction);
    }

    @Override
    public boolean removeById(Serializable id) {
        Assert.fail(!dictRelAbnormalActionService.checkCanDelete(id), "存在关联信访事项操作无效");
        return super.removeById(id);
    }
}
