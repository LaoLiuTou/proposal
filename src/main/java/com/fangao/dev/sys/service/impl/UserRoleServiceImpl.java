package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fangao.dev.sys.entity.UserRole;
import com.fangao.dev.sys.mapper.UserRoleMapper;
import com.fangao.dev.sys.service.IUserRoleService;
import com.fangao.dev.sys.vo.UserRoleSelectedVO;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户角色关联表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-16
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {


    @Override
    public List<UserRoleSelectedVO> listSelectedVO(Long userId) {
        return baseMapper.selectSelectedVO(userId);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateByIds(Long userId, List<Long> ids) {
        Assert.fail(null == userId || CollectionUtils.isEmpty(ids), "用户角色 ID 不能为空");
        return removeByUserId(userId) && saveBatch(ids.stream().map(id -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(id);
            return userRole;
        }).collect(Collectors.toList()));
    }


    @Override
    public boolean removeByUserId(Serializable userId) {
        return super.remove(Wrappers.<UserRole>query().eq("user_id", userId));
    }

    @Override
    public boolean relation(Serializable roleId) {
        return count(new QueryWrapper<UserRole>().eq("role_id",roleId)) > 0;
        /*return count(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId,
                roleId)) > 0;*/
    }
}
