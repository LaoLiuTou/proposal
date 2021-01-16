package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.DictPetitionKeyArea;
import com.fangao.dev.sys.mapper.DictPetitionKeyAreaMapper;
import com.fangao.dev.sys.service.IDictPetitionKeyAreaService;
import com.fangao.dev.sys.service.IPetitionEventInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 重点领域分类字典表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Service
public class DictPetitionKeyAreaServiceImpl extends ServiceImpl<DictPetitionKeyAreaMapper, DictPetitionKeyArea> implements IDictPetitionKeyAreaService {

    @Autowired
    private IPetitionEventInfoService petitionEventInfoService;

    @Override
    public IPage<DictPetitionKeyArea> page(Page page, DictPetitionKeyArea dictPetitionKeyArea) {
        QueryWrapper<DictPetitionKeyArea> qw = new QueryWrapper<>();
        qw.setEntity(dictPetitionKeyArea);
        return super.page(page, qw);
    }

    @Override
    public List<DictPetitionKeyArea> listEffective() {
        return super.list(Wrappers.<DictPetitionKeyArea>query().select("id", "name")
                .eq("status", 0).orderByDesc("sort"));
    }

    @Override
    public boolean save(DictPetitionKeyArea dictPetitionKeyArea) {
        if (null == dictPetitionKeyArea) {
            return false;
        }
        return super.save(dictPetitionKeyArea);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        DictPetitionKeyArea dictPetitionKeyArea = new DictPetitionKeyArea();
        dictPetitionKeyArea.setId(id);
        dictPetitionKeyArea.setStatus(status.intValue() > -1 ? 0 : -1);
        return updateById(dictPetitionKeyArea);
    }

    @Override
    public boolean removeById(Serializable id) {
        ArrayList<Serializable> ids = new ArrayList<Serializable>(){{
            add(id);
        }};
        Assert.fail(!petitionEventInfoService.checkCanDelete("key_area_id",ids), "存在关联信访事项操作无效");
        return super.removeById(id);
    }
}
