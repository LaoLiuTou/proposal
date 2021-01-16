package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.LinkAbnormalAction;
import com.fangao.dev.sys.mapper.LinkAbnormalActionMapper;
import com.fangao.dev.sys.service.ILinkAbnormalActionService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 * 异常行为事项关系表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-03-27
 */
@Service
public class LinkAbnormalActionServiceImpl extends ServiceImpl<LinkAbnormalActionMapper, LinkAbnormalAction> implements ILinkAbnormalActionService {

    @Override
    public boolean checkCanDelete(Serializable id) {
        return  super.count(new QueryWrapper<LinkAbnormalAction>().eq("action_id",id))<=0;
    }
}
