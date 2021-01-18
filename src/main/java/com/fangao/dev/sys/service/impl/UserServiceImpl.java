package com.fangao.dev.sys.service.impl;

import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.captcha.ImageCaptcha;
import com.baomidou.kisso.common.encrypt.MD5Salt;
import com.baomidou.kisso.common.util.RandomType;
import com.baomidou.kisso.common.util.RandomUtil;
import com.baomidou.kisso.security.token.SSOToken;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.common.ErrorCode;
import com.fangao.dev.common.FangaoConstant;
import com.fangao.dev.common.utils.JacksonUtils;
import com.fangao.dev.common.utils.RegexUtils;
import com.fangao.dev.sys.controller.DrawController;
import com.fangao.dev.sys.dto.*;
import com.fangao.dev.sys.entity.*;
import com.fangao.dev.sys.mapper.UserMapper;
import com.fangao.dev.sys.service.*;
import com.fangao.dev.sys.vo.UserRoleSelectedVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService{

    @Autowired
    private IOrgService orgService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IUserOrgService userOrgService;
    @Autowired
    private ILogService logService;
    @Autowired
    private IParamService paramService;

    @Override
    public List<String> getUsernamesByRoleNameAndOrgId(String role_name, Long org_id) {
        return baseMapper.getUsernamesByRoleNameAndOrgId(role_name,org_id);
    }

    @Override
    public IPage<User> queryUser(IPage<User> page, User user) {
        if (RegexUtils.isEnglish(user.getRealName())) {
            user.setInitial(user.getRealName());
            user.setRealName(null);
        }
        return page.setRecords(this.baseMapper.queryUser(page,user));
    }

    @Override
    public int userNameCount(Long id, String username) {
        if(id != -1){
            return super.count(Wrappers.<User>query().select("id").ne("id",id).eq("userName",username));
        }else{
            return super.count(Wrappers.<User>query().select("id").eq("userName",username));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveDto(UserDTO dto) {
        User user = dto.convert(User.class);
        user.setSalt(RandomUtil.getText(RandomType.MIX, 8));
        user.setPassword(MD5Salt.md5SaltEncode(user.getUsername() + user.getSalt(), dto.getPassword()));
        return this.save(user) && userRoleService.saveBatch(dto.getRoleIds().stream().map(id -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(id);
            return userRole;
        }).collect(Collectors.toList()))
                && userOrgService.saveBatch(dto.getOrgIds().stream().map(id -> {
            UserOrg userOrg = new UserOrg();
            userOrg.setUserId(user.getId());
            userOrg.setOrgId(id);
            return userOrg;
        }).collect(Collectors.toList()));
    }

    @Override
    public boolean updateDtoById(UserDTO dto) {
        Assert.fail(null == dto.getId(), ErrorCode.ID_REQUIRED);
        User dbUser = super.getById(dto.getId());
        Assert.fail(null == dbUser, "修改用户不存在");
        /*user.setPassword(MD5Salt.md5SaltEncode(dbUser.getUsername() + dbUser.getSalt(), dto.getPassword()));*/
        return super.updateById(dto.convert(User.class)) && userRoleService.updateByIds(dto.getId(), dto.getRoleIds())
                && userOrgService.updateByIds(dto.getId(), dto.getOrgIds());
    }

    @Override
    public User loginByDto(HttpServletRequest request, HttpServletResponse response, LoginDTO dto) {
        if(dto.getCodeFlag())
            Assert.fail(!ImageCaptcha.getInstance().verification(request,
                    DrawController.TICKET, dto.getCode()), "验证码错误");


        Assert.fail(StringUtils.isEmpty(dto.getUsername())
                || StringUtils.isEmpty(dto.getPassword()), "用户名密码不能为空");
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper=queryWrapper.eq("username", dto.getUsername());
        List<User> userList = list(queryWrapper);
        Assert.fail(null == userList || userList.size() != 1, "用户不存在或异常数据");

        User user = userList.get(0);
        Assert.fail(user.getStatus() == -1,"该账号已被禁用");
        Assert.fail(!MD5Salt.md5SaltValid(user.getUsername() + user.getSalt()
                , user.getPassword(), dto.getPassword()), "登录密码错误");

        // Todo 单点登录，踢出前一个相同账号

        // 设置登录 COOKIE
        SSOToken st = new SSOToken();
        st.setId(user.getId());
        st.setIssuer(user.getUsername());
        st.setUserAgent(request);
        SSOHelper.setCookie(request, response, st, true);

        //插入访问日志
        try{
            Log log = new Log();
            log.setUserId(user.getId());
            log.setUsername(user.getUsername());
            log.setUri(request.getRequestURI());
            log.setIp(request.getRemoteAddr());
            log.setParams("login:"+ JacksonUtils.toJSONString(request.getParameterMap()));
            log.setRemark("登录");
            logService.add(log);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        // 返回临时对象页面显示
        User tempUser = new User();
        tempUser.setUsername(user.getUsername());
        tempUser.setNickName(user.getNickName());
        tempUser.setAvatar(user.getAvatar());
        tempUser.setOrgId(userOrgService.getOrgIdByUserId(user.getId())+"");

        List<UserRoleSelectedVO> roleList = userRoleService.listSelectedVO(user.getId());
        if(roleList != null && roleList.size() > 0){
            for(UserRoleSelectedVO userRoleSelectedVO: roleList){
                if(userRoleSelectedVO.getSelected() != null && userRoleSelectedVO.getSelected()){
                    tempUser.setIndexPage(userRoleSelectedVO.getIndexPage());
                    tempUser.setShowRoles(tempUser.getShowRoles()==null?userRoleSelectedVO.getName():tempUser.getShowRoles()+"，"+userRoleSelectedVO.getName());
                    //break;
                }
            }
        }
        return tempUser;
    }

    @Override
    public boolean updateById(User user) {
        Assert.fail(null == user.getId(), ErrorCode.ID_REQUIRED);
        return super.updateById(user);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status.intValue() > -1 ? 0 : -1);
        return updateById(user);
    }

    @Override
    public User getById(Serializable id) {
        User user = baseMapper.selectById(id);
        Assert.fail(null == user, ErrorCode.ID_NOT_FOUND);
        return user;
    }

    @Override
    public boolean save(User user) {
        if (null == user) {
            return false;
        }
        user.setPhoneVerified(0);
        user.setEmailVerified(0);
        user.setStatus(0);
        return super.save(user.initialName());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id) && userRoleService.removeByUserId(id) && userOrgService.removeByUserId(id);
    }


    @Override
    public boolean unlock(Long id, String password) {
        User user = getById(id);
        return MD5Salt.md5SaltValid(user.getUsername() + user.getSalt()
                , user.getPassword(), password);
    }

    @Override
    public boolean resetPassword(Long id) {
        User user = getById(id);
        Param sysDefaultPassword = paramService.getOne(Wrappers.<Param>query().eq("code","sys.default.password"));
        String password = (sysDefaultPassword!=null?sysDefaultPassword.getContent():FangaoConstant.DEFAULT_PASSWORD);
        User temp = new User();
        temp.setId(id);
        temp.setPassword(MD5Salt.md5SaltEncode(user.getUsername() + user.getSalt(), password));
        return super.updateById(temp);
    }

    @Override
    public boolean resetPswOwn(ResetPswDTO dto) {
        Assert.fail(StringUtils.isEmpty(dto.getNewPsw())
                || StringUtils.isEmpty(dto.getOldPsw()), "密码不能为空");

        List<User> userList = list(Wrappers.<User>query().eq("username", dto.getUsername()));
        Assert.fail(null == userList || userList.size() > 1, "用户不存或异常数据");

        User user = userList.get(0);
        Assert.fail(!MD5Salt.md5SaltValid(user.getUsername() + user.getSalt()
                , user.getPassword(), dto.getOldPsw()), "原密码错误");
        User temp = new User();
        temp.setId(user.getId());
        temp.setPassword(MD5Salt.md5SaltEncode(user.getUsername() + user.getSalt(), dto.getNewPsw()));
        return super.updateById(temp);
    }

    @Override
    public List<UserDTO> listReceptionUser() {
        return baseMapper.listReceptionUser();
    }

    @Override
    public boolean queryByUserIdAndResourceId(long userId, long resourceId) {
        List<UserResourceDTO> list = baseMapper.queryByUserIdAndResourceId(userId,resourceId);
        return (list!=null && list.size()==1);
    }

    @Override
    public UserInfoDTO queryUserInfo(long id) {
        List<UserInfoDTO> list = baseMapper.queryUserInfo(id);
        return list.size()==1?list.get(0):new UserInfoDTO();
    }

    @Override
    public int check_dj_jd(long orgId, long userId, String roleIds, boolean edit) {
        String [] _roleIds = roleIds.split(",");
        if(_roleIds.length <= 0) return 0;
        boolean djFlag = false;
        boolean jdFlag = false;
        for(String roleId:_roleIds){
            if(("1").equals(roleId)) djFlag = true;
            if(("2").equals(roleId)) jdFlag = true;
        }
        if(!djFlag && !jdFlag) return 0;
        List<UserInfoDTO> dj_users = new ArrayList<>();
        List<UserInfoDTO> jd_users = new ArrayList<>();
        if(djFlag){
            dj_users = orgService.getUserByOrgIdAndRoleId(orgId,1L);
            if(dj_users.size()>0 && edit){
                for(UserInfoDTO dj:dj_users)
                    if(dj.getId() == userId) {dj_users.remove(dj);break;}
            }
        }
        if(jdFlag){
            jd_users = orgService.getUserByOrgIdAndRoleId(orgId,2L);
            if(jd_users.size()>0 && edit){
                for(UserInfoDTO jd:jd_users)
                    if(jd.getId() == userId) {jd_users.remove(jd);break;}
            }
        }
        if(dj_users.size()>0) return 1;
        if(jd_users.size()>0) return 2;
        return 0;
    }
}
