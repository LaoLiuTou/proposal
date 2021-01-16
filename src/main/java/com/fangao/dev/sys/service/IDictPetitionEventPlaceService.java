package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.DictPetitionEventPlace;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 事发地字典表 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
public interface IDictPetitionEventPlaceService extends IService<DictPetitionEventPlace> {


    IPage<DictPetitionEventPlace> page(Page page, DictPetitionEventPlace dictPetitionEventPlace);


    /**
     * 查询所有正常事发地信息
     */
    List<DictPetitionEventPlace> listEffective();

    /**
     * 根据当前登录的接待处用户，查询对应管理的事发地
     * @return
     */
    List<DictPetitionEventPlace> listEffectiveByUserId(Long user_id);

    /**
     * 根据信访件接待处id，查询对应管理的事发地
     * @return
     */
    List<DictPetitionEventPlace> listEffectiveByReceptionOrgId(Long reception_org_id);
    /**
     * <p>
     * 修改事发地状态
     * </p>
     *
     * @param id     事发地 ID
     * @param status 状态
     * @return
     */
    boolean updateStatus(Long id, Integer status);

    List<DictPetitionEventPlace> getByName(String name);

}
