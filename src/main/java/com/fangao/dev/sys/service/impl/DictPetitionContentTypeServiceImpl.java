package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.DictPetitionContentType;
import com.fangao.dev.sys.mapper.DictPetitionContentTypeMapper;
import com.fangao.dev.sys.service.IDictPetitionContentTypeService;
import com.fangao.dev.sys.service.IPetitionEventInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 内容分类字典表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Service
public class DictPetitionContentTypeServiceImpl extends ServiceImpl<DictPetitionContentTypeMapper, DictPetitionContentType> implements IDictPetitionContentTypeService {

    @Autowired
    private IPetitionEventInfoService petitionEventInfoService;

    @Override
    public IPage<DictPetitionContentType> page(Page page, DictPetitionContentType dictPetitionContentType) {
        QueryWrapper<DictPetitionContentType> qw = new QueryWrapper<>();
        qw.setEntity(dictPetitionContentType);
        return super.page(page, qw);
    }

    @Override
    public List<DictPetitionContentType> listEffective() {
        return super.list(Wrappers.<DictPetitionContentType>query().select("id","pid","name")
                .eq("status", 0).orderByDesc("sort").orderByAsc("id"));
    }

    @Override
    public List<DictPetitionContentType> listAll() {
        return super.list(Wrappers.<DictPetitionContentType>query()
                .eq("deleted", 0).orderByDesc("sort").orderByAsc("id"));
    }

    @Override
    public boolean save(DictPetitionContentType dictPetitionContentType) {
        if (null == dictPetitionContentType) {
            return false;
        }
        return super.save(dictPetitionContentType);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        DictPetitionContentType dictPetitionContentType = new DictPetitionContentType();
        dictPetitionContentType.setId(id);
        dictPetitionContentType.setStatus(status.intValue() > -1 ? 0 : -1);
        if(dictPetitionContentType.getStatus() > -1){
            return updateById(dictPetitionContentType);
        }else{
            List<DictPetitionContentType> childrenIds = this.baseMapper.queryChildrenIds(id);
            for(DictPetitionContentType r: childrenIds){
                r.setStatus(-1);
            }
            return updateBatchById(childrenIds);
        }
    }

    @Override
    public boolean removeById(Serializable id) {
        List<DictPetitionContentType> childrenIds = this.baseMapper.queryChildrenIds(id);
        ArrayList<Serializable> ids = new ArrayList<Serializable>();
        for(DictPetitionContentType childrenId:childrenIds){
            ids.add(childrenId.getId());
        }
        Assert.fail(!petitionEventInfoService.checkCanDelete("content_type_id",ids), "存在关联信访事项操作无效");
        return super.removeByIds(ids);
    }
    @Override
    public DictPetitionContentType getByName(String name){
        return super.baseMapper.getByName(name);
    }
}
