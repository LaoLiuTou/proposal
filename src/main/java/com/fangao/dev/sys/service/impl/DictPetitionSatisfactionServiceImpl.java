package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.DictPetitionSatisfaction;
import com.fangao.dev.sys.entity.DictPetitionSolve;
import com.fangao.dev.sys.mapper.DictPetitionSatisfactionMapper;
import com.fangao.dev.sys.service.IDictPetitionSatisfactionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 满意度字典表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Service
public class DictPetitionSatisfactionServiceImpl extends ServiceImpl<DictPetitionSatisfactionMapper, DictPetitionSatisfaction> implements IDictPetitionSatisfactionService {

    @Override
    public IPage<DictPetitionSatisfaction> page(Page page, DictPetitionSatisfaction dictPetitionSatisfaction) {
        QueryWrapper<DictPetitionSatisfaction> qw = new QueryWrapper<>();
        qw.setEntity(dictPetitionSatisfaction);
        return super.page(page, qw);
    }



    @Override
    public List<DictPetitionSatisfaction> listEffective() {
        return super.list(Wrappers.<DictPetitionSatisfaction>query().select("id", "name")
                .eq("status", 0).orderByDesc("sort"));
    }

    @Override
    public boolean save(DictPetitionSatisfaction dictPetitionSatisfaction) {
        if (null == dictPetitionSatisfaction) {
            return false;
        }
        return super.save(dictPetitionSatisfaction);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        DictPetitionSatisfaction dictPetitionSatisfaction = new DictPetitionSatisfaction();
        dictPetitionSatisfaction.setId(id);
        dictPetitionSatisfaction.setStatus(status.intValue() > -1 ? 0 : -1);
        return updateById(dictPetitionSatisfaction);
    }
}
