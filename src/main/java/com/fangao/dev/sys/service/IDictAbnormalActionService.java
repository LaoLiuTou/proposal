package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.DictAbnormalAction;
import com.fangao.dev.sys.entity.DictAbnormalPlace;

import java.util.List;

/**
 * <p>
 * 异常行为字典表 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
public interface IDictAbnormalActionService extends IService<DictAbnormalAction> {


    IPage<DictAbnormalAction> page(Page page, DictAbnormalAction dictAbnormalAction);


    /**
     * 查询所有正常信息
     */
    List<DictAbnormalAction> listEffective();


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
