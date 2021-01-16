package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.LinkCoordinationUnit;
import com.fangao.dev.sys.mapper.LinkCoordinationUnitMapper;
import com.fangao.dev.sys.service.ILinkCoordinationUnitService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 * 配合单位事项关系表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-03-27
 */
@Service
public class LinkCoordinationUnitServiceImpl extends ServiceImpl<LinkCoordinationUnitMapper, LinkCoordinationUnit> implements ILinkCoordinationUnitService {

    @Override
    public boolean checkCanDelete(Serializable id) {
        return  super.count(new QueryWrapper<LinkCoordinationUnit>().eq("unit_id",id))<=0;
    }
}
