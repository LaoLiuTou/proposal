package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.DictAbnormalPlace;
import com.fangao.dev.sys.entity.DictPetitionEventPlace;

import java.util.List;

/**
 * <p>
 * 异常地字典表 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
public interface IDictAbnormalPlaceService extends IService<DictAbnormalPlace> {


    IPage<DictAbnormalPlace> page(Page page, DictAbnormalPlace dictAbnormalPlace);


    /**
     * 查询所有正常信息
     */
    List<DictAbnormalPlace> listEffective();


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
