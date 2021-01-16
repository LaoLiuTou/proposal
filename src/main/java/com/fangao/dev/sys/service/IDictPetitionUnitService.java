package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.DictPetitionUnit;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
public interface IDictPetitionUnitService extends IService<DictPetitionUnit> {


    IPage<DictPetitionUnit> page(Page page, DictPetitionUnit dictPetitionUnit);


    /**
     * 查询所有正常单位信息
     */
    List<DictPetitionUnit> listEffective();


    /**
     * <p>
     * 修改单位状态
     * </p>
     *
     * @param id     单位 ID
     * @param status 状态
     * @return
     */
    boolean updateStatus(Long id, Integer status);

}
