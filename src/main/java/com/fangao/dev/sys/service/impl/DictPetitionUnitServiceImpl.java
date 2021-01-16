package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.DictPetitionUnit;
import com.fangao.dev.sys.mapper.DictPetitionUnitMapper;
import com.fangao.dev.sys.service.ILinkCoordinationUnitService;
import com.fangao.dev.sys.service.IDictPetitionUnitService;
import com.fangao.dev.sys.service.IPetitionEventInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 单位字典表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Service
public class DictPetitionUnitServiceImpl extends ServiceImpl<DictPetitionUnitMapper, DictPetitionUnit> implements IDictPetitionUnitService {

    @Autowired
    private IPetitionEventInfoService petitionEventInfoService;
    @Autowired
    private ILinkCoordinationUnitService dictCoordinationUnitService;

    @Override
    public IPage<DictPetitionUnit> page(Page page, DictPetitionUnit dictPetitionUnit) {
        QueryWrapper<DictPetitionUnit> qw = new QueryWrapper<>();
        qw.setEntity(dictPetitionUnit);
        return super.page(page, qw);
    }

    @Override
    public List<DictPetitionUnit> listEffective() {
        return super.list(Wrappers.<DictPetitionUnit>query().select("id", "name")
                .eq("status", 0).orderByAsc("CONVERT(name USING gbk)"));
    }

    @Override
    public boolean save(DictPetitionUnit dictPetitionUnit) {
        if (null == dictPetitionUnit) {
            return false;
        }
        return super.save(dictPetitionUnit);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        DictPetitionUnit dictPetitionUnit = new DictPetitionUnit();
        dictPetitionUnit.setId(id);
        dictPetitionUnit.setStatus(status.intValue() > -1 ? 0 : -1);
        return updateById(dictPetitionUnit);
    }

    @Override
    public boolean removeById(Serializable id) {
        ArrayList<Serializable> ids = new ArrayList<Serializable>(){{
            add(id);
        }};
        Assert.fail(!petitionEventInfoService.checkCanDelete("duty_unit_id",ids) || !dictCoordinationUnitService.checkCanDelete(id), "存在关联信访事项操作无效");
        return super.removeById(id);
    }
}
