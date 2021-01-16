package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.DictPetitionLeaderLevel;
import com.fangao.dev.sys.entity.Resource;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 领导层级字典表 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
public interface IDictPetitionLeaderLevelService extends IService<DictPetitionLeaderLevel> {


    IPage<DictPetitionLeaderLevel> page(Page page, DictPetitionLeaderLevel dictPetitionLeaderLevel);


    /**
     * 查询所有正常领导层级信息
     */
    List<DictPetitionLeaderLevel> listEffective();


    /**
     * <p>
     * 修改领导层级状态
     * </p>
     *
     * @param id     领导层级 ID
     * @param status 状态
     * @return
     */
    boolean updateStatus(Long id, Integer status);

}
