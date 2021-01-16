package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.LinkAbnormalPlace;
import com.fangao.dev.sys.mapper.LinkAbnormalPlaceMapper;
import com.fangao.dev.sys.service.ILinkAbnormalPlaceService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 * 异常地事项关系表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-03-27
 */
@Service
public class LinkAbnormalPlaceServiceImpl extends ServiceImpl<LinkAbnormalPlaceMapper, LinkAbnormalPlace> implements ILinkAbnormalPlaceService {

    @Override
    public boolean checkCanDelete(Serializable id) {
        return  super.count(new QueryWrapper<LinkAbnormalPlace>().eq("place_id",id))<=0;
    }
}
