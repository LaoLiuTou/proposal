package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.DictPetitionLeaderLevel;
import com.fangao.dev.sys.mapper.DictPetitionLeaderLevelMapper;
import com.fangao.dev.sys.service.IDictPetitionLeaderLevelService;
import com.fangao.dev.sys.service.IDictPetitionLeaderService;
import com.fangao.dev.sys.service.IPetitionEventInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 领导层级字典表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Service
public class DictPetitionLeaderLevelServiceImpl extends ServiceImpl<DictPetitionLeaderLevelMapper, DictPetitionLeaderLevel> implements IDictPetitionLeaderLevelService {

    @Autowired
    private IDictPetitionLeaderService dictPetitionLeaderService;

    @Override
    public IPage<DictPetitionLeaderLevel> page(Page page, DictPetitionLeaderLevel dictPetitionLeaderLevel) {
        QueryWrapper<DictPetitionLeaderLevel> qw = new QueryWrapper<>();
        qw.setEntity(dictPetitionLeaderLevel);
        return super.page(page, qw);
    }



    @Override
    public List<DictPetitionLeaderLevel> listEffective() {
        return super.list(Wrappers.<DictPetitionLeaderLevel>query().select("id", "name")
                .eq("status", 0).orderByDesc("sort"));
    }

    @Override
    public boolean save(DictPetitionLeaderLevel dictPetitionLeaderLevel) {
        if (null == dictPetitionLeaderLevel) {
            return false;
        }
        return super.save(dictPetitionLeaderLevel);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        DictPetitionLeaderLevel dictPetitionLeaderLevel = new DictPetitionLeaderLevel();
        dictPetitionLeaderLevel.setId(id);
        dictPetitionLeaderLevel.setStatus(status.intValue() > -1 ? 0 : -1);
        return updateById(dictPetitionLeaderLevel);
    }

    @Override
    public boolean removeById(Serializable id) {
        Assert.fail(!dictPetitionLeaderService.checkCanDelete(id), "存在关联领导操作无效");
        return super.removeById(id);
    }
}
