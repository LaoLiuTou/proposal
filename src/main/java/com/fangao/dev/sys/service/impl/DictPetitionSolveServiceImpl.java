package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.DictPetitionLeaderLevel;
import com.fangao.dev.sys.entity.DictPetitionSolve;
import com.fangao.dev.sys.mapper.DictPetitionSolveMapper;
import com.fangao.dev.sys.service.IDictPetitionLeaderService;
import com.fangao.dev.sys.service.IDictPetitionSolveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 化解方式字典表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Service
public class DictPetitionSolveServiceImpl extends ServiceImpl<DictPetitionSolveMapper, DictPetitionSolve> implements IDictPetitionSolveService {

    @Override
    public IPage<DictPetitionSolve> page(Page page, DictPetitionSolve dictPetitionSolve) {
        QueryWrapper<DictPetitionSolve> qw = new QueryWrapper<>();
        qw.setEntity(dictPetitionSolve);
        return super.page(page, qw);
    }



    @Override
    public List<DictPetitionSolve> listEffective() {
        return super.list(Wrappers.<DictPetitionSolve>query().select("id", "name")
                .eq("status", 0).orderByDesc("sort"));
    }

    @Override
    public boolean save(DictPetitionSolve dictPetitionSolve) {
        if (null == dictPetitionSolve) {
            return false;
        }
        return super.save(dictPetitionSolve);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        DictPetitionSolve dictPetitionSolve = new DictPetitionSolve();
        dictPetitionSolve.setId(id);
        dictPetitionSolve.setStatus(status.intValue() > -1 ? 0 : -1);
        return updateById(dictPetitionSolve);
    }
}
