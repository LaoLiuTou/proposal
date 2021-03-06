package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.DictPetitionLeader;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 领导字典表 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
public interface IDictPetitionLeaderService extends IService<DictPetitionLeader> {

    /**
     * <p>
     * 领导字典分页
     * </p>
     *
     * @param page 分页对象
     * @param dictPetitionLeader 领导信息
     * @return
     */
    IPage<DictPetitionLeader> queryLeaderPage(IPage<DictPetitionLeader> page, DictPetitionLeader dictPetitionLeader);


    /**
     * 查询所有正常领导信息
     */
    List<DictPetitionLeader> listEffective();
    /**
     * 根据接待处id和层级查询所有正常领导信息
     */
    List<DictPetitionLeader> listEffectiveByOrgIdAndLevelId(Long orgId,Long LevelId);
    /**
     * 根据 leaderLevelId 查询是否可删除
     */
    boolean checkCanDelete(Serializable leaderLevelId);

    /**
     * <p>
     * 修改领导状态
     * </p>
     *
     * @param id     领导 ID
     * @param status 状态
     * @return
     */
    boolean updateStatus(Long id, Integer status);

}
