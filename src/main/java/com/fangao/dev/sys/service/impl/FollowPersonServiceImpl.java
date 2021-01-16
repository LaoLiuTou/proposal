package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.dto.FollowPersonDTO;
import com.fangao.dev.sys.entity.FollowPerson;
import com.fangao.dev.sys.mapper.FollowPersonMapper;
import com.fangao.dev.sys.service.IFollowPersonService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 随访人信息表
 * </p>
 *
 * @author ykb
 * @since 2019-03-24
 */
@Service
public class FollowPersonServiceImpl extends ServiceImpl<FollowPersonMapper, FollowPerson> implements IFollowPersonService {

    @Override
    public List<FollowPersonDTO> getFollowPersonListByEventId(long id) {
        return baseMapper.getFollowPersonListByEventId(id);
    }
}
