package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.DictPetitionSolve;

import java.util.List;

/**
 * <p>
 * 化解方式字典表 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
public interface IDictPetitionSolveService extends IService<DictPetitionSolve> {


    IPage<DictPetitionSolve> page(Page page, DictPetitionSolve dictPetitionSolve);


    /**
     * 查询所有正常化解方式信息
     */
    List<DictPetitionSolve> listEffective();


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
