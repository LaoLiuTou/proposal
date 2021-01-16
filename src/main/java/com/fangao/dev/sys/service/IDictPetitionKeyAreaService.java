package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.DictPetitionKeyArea;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 重点领域分类字典表 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
public interface IDictPetitionKeyAreaService extends IService<DictPetitionKeyArea> {


    IPage<DictPetitionKeyArea> page(Page page, DictPetitionKeyArea dictPetitionKeyArea);


    /**
     * 查询所有正常重点领域分类信息
     */
    List<DictPetitionKeyArea> listEffective();


    /**
     * <p>
     * 修改重点领域分类状态
     * </p>
     *
     * @param id     重点领域分类 ID
     * @param status 状态
     * @return
     */
    boolean updateStatus(Long id, Integer status);

}
