package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.DictPetitionStabilityControl;
import com.fangao.dev.sys.mapper.DictPetitionStabilityControlMapper;
import com.fangao.dev.sys.service.ILinkCoordinationUnitService;
import com.fangao.dev.sys.service.IDictPetitionStabilityControlService;
import com.fangao.dev.sys.service.IPetitionEventInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * <p>
 *     稳控单位字典表 服务实现类
 * </p>
 *
 * @author 49030
 * @date 2019/5/14
 */
@Service
public class DictPetitionPetitionStabilityControlServiceImpl extends ServiceImpl<DictPetitionStabilityControlMapper, DictPetitionStabilityControl> implements IDictPetitionStabilityControlService {

    @Autowired
    private IPetitionEventInfoService petitionEventInfoService;
    @Autowired
    private ILinkCoordinationUnitService dictCoordinationUnitService;

    @Override
    public IPage<DictPetitionStabilityControl> page(Page page, DictPetitionStabilityControl dictPetitionStabilityControl) {
        QueryWrapper<DictPetitionStabilityControl> qw = new QueryWrapper<>();
        qw.setEntity(dictPetitionStabilityControl);
        return super.page(page, qw);
    }

    @Override
    public List<DictPetitionStabilityControl> listEffective() {
        return super.list(Wrappers.<DictPetitionStabilityControl>query().select("id", "name")
                .eq("status", 0).orderByDesc("sort"));
    }

    @Override
    public boolean save(DictPetitionStabilityControl dictPetitionStabilityControl) {
        if (null == dictPetitionStabilityControl) {
            return false;
        }
        return super.save(dictPetitionStabilityControl);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        DictPetitionStabilityControl dictPetitionStabilityControl = new DictPetitionStabilityControl();
        dictPetitionStabilityControl.setId(id);
        dictPetitionStabilityControl.setStatus(status.intValue() > -1 ? 0 : -1);
        return updateById(dictPetitionStabilityControl);
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
