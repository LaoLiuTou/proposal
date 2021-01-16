package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.DictPetitionContentType;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 内容分类表 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
public interface IDictPetitionContentTypeService extends IService<DictPetitionContentType> {


    IPage<DictPetitionContentType> page(Page page, DictPetitionContentType petitionContentType);


    /**
     * 查询所有正常内容分类信息
     */
    List<DictPetitionContentType> listEffective();

    /**
     * 查询所有内容分类信息
     */
    List<DictPetitionContentType> listAll();


    /**
     * <p>
     * 修改内容分类状态
     * </p>
     *
     * @param id     内容分类 ID
     * @param status 状态
     * @return
     */
    boolean updateStatus(Long id, Integer status);

    DictPetitionContentType getByName(String name);

}
