package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.DictPetitionStabilityControl;

import java.util.List;

/**
 * <p>
 *     稳控单位字典表 服务类
 * </p>
 *
 * @author 49030
 * @date 2019/5/14
 */

public interface IDictPetitionStabilityControlService extends IService<DictPetitionStabilityControl>{
    IPage<DictPetitionStabilityControl> page(Page page, DictPetitionStabilityControl dictPetitionStabilityControl);


    /**
     * 查询所有正常单位信息
     */
    List<DictPetitionStabilityControl> listEffective();


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
