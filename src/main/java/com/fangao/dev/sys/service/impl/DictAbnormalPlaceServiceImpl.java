package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.DictAbnormalPlace;
import com.fangao.dev.sys.mapper.DictAbnormalPlaceMapper;
import com.fangao.dev.sys.service.IDictAbnormalPlaceService;
import com.fangao.dev.sys.service.ILinkAbnormalPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 异常地字典表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Service
public class DictAbnormalPlaceServiceImpl extends ServiceImpl<DictAbnormalPlaceMapper, DictAbnormalPlace> implements IDictAbnormalPlaceService {

    @Autowired
    private ILinkAbnormalPlaceService dictRelAbnormalPlaceService;

    @Override
    public IPage<DictAbnormalPlace> page(Page page, DictAbnormalPlace dictAbnormalPlace) {
        QueryWrapper<DictAbnormalPlace> qw = new QueryWrapper<>();
        qw.setEntity(dictAbnormalPlace);
        return super.page(page, qw);
    }

    @Override
    public List<DictAbnormalPlace> listEffective() {
        return super.list(Wrappers.<DictAbnormalPlace>query().select("id", "name")
                .eq("status", 0).orderByDesc("sort"));
    }

    @Override
    public boolean save(DictAbnormalPlace dictAbnormalPlace) {
        if (null == dictAbnormalPlace) {
            return false;
        }
        return super.save(dictAbnormalPlace);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        DictAbnormalPlace dictAbnormalPlace = new DictAbnormalPlace();
        dictAbnormalPlace.setId(id);
        dictAbnormalPlace.setStatus(status.intValue() > -1 ? 0 : -1);
        return updateById(dictAbnormalPlace);
    }

    @Override
    public boolean removeById(Serializable id) {
        Assert.fail(!dictRelAbnormalPlaceService.checkCanDelete(id), "存在关联信访事项操作无效");
        return super.removeById(id);
    }
}
