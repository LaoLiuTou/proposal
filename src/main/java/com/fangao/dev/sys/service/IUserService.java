package com.fangao.dev.sys.service;

import com.fangao.dev.sys.dto.LoginDTO;
import com.fangao.dev.sys.dto.ResetPswDTO;
import com.fangao.dev.sys.dto.UserDTO;
import com.fangao.dev.sys.dto.UserInfoDTO;
import com.fangao.dev.sys.entity.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-09-16
 */
public interface IUserService extends IService<User> {

    /**
     * <p>
     * 根据用户信息分页
     * </p>
     *
     * @param page 分页对象
     * @param user 用户信息
     * @return
     */
    IPage<User> queryUser(IPage<User> page, User user);


    /**
     * <p>
     * 查询用户账号是否已存在
     * </p>
     * @param id 用户 ID
     * @param username 用户账号
     * @return
     */
    int userNameCount(Long id,String username);


    /**
     * <p>
     * 保存用户角色关系信息
     * </p>
     *
     * @param dto 用户 DTO
     * @return
     */
    boolean saveDto(UserDTO dto);



    /**
     * <p>
     * 修改用户角色关系信息
     * </p>
     *
     * @param dto 用户 DTO
     * @return
     */
    boolean updateDtoById(UserDTO dto);


    /**
     * <p>
     * 登录设置 COOKIE
     * </p>
     *
     * @param request  HTTP 请求
     * @param response HTTP 响应
     * @param dto      用户 DTO
     * @return
     */
    User loginByDto(HttpServletRequest request, HttpServletResponse response, LoginDTO dto);


    /**
     * <p>
     * 更新用户状态
     * </p>
     *
     * @param id     用户 ID
     * @param status 状态
     * @return
     */
    boolean updateStatus(Long id, Integer status);


    /**
     * <p>
     * 解锁用户
     * </p>
     *
     * @param id       用户 ID
     * @param password 明文密码
     * @return
     */
    boolean unlock(Long id, String password);


    /**
     * <p>
     * 重置指定 ID 用户的登录密码
     * </p>
     *
     * @param id 用户 ID
     * @return
     */
    boolean resetPassword(Long id);

    /**
     * <p>
     * 修改 登录密码
     * </p>
     *
     * @param dto
     * @return
     */
    boolean resetPswOwn(ResetPswDTO dto);

    /**
     * 查询所有角色为接待处的人员，带组织名称
     * @return
     */
    List<UserDTO> listReceptionUser();

    /**
     * 查询用户权限
     * @param userId
     * @param resourceId
     * @return
     */
    boolean queryByUserIdAndResourceId(long userId,long resourceId);

    /**
     * 查询个人信息
     * @param id
     * @return
     */
    UserInfoDTO queryUserInfo(long id);

    /**
     * 检查orgId组织下登记员、接待员是否唯一
     * @param orgId  组织ID
     * @param userId 用户ID
     * @param roleIds 逗号分隔，0：管理员，1：登记员，2：接待员，3：领导
     * @param edit 0：新增，1：修改
     * @return 0：不存在，可操作    1：存在登记员    2：存在接待员
     */
    int check_dj_jd(long orgId, long userId, String roleIds, boolean edit);

    List<String> getUsernamesByRoleNameAndOrgId(String role_name, Long org_id);
}
