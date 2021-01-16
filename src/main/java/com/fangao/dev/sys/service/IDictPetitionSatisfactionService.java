package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.DictPetitionSatisfaction;
import com.fangao.dev.sys.entity.DictPetitionSolve;

import java.util.List;

/**
 * <p>
 * 满意度字典表 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
public interface IDictPetitionSatisfactionService extends IService<DictPetitionSatisfaction> {


    IPage<DictPetitionSatisfaction> page(Page page, DictPetitionSatisfaction dictPetitionSatisfaction);


    /**
     * 查询所有正常满意度信息
     */
    List<DictPetitionSatisfaction> listEffective();


    /**
     * <p>
     * 修改状态
     * </p>
     *
     * @param id     ID
     * @param status 状态
     * @return
     */
    boolean updateStatus(Long id, Integer status);

}
