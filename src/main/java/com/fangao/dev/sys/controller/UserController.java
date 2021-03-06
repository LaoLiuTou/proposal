package com.fangao.dev.sys.controller;


import com.fangao.dev.common.web.Account;
import com.fangao.dev.common.web.LoginHelper;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.dto.ResetPswDTO;
import com.fangao.dev.sys.dto.UserDTO;
import com.fangao.dev.sys.dto.UserInfoDTO;
import com.fangao.dev.sys.entity.User;
import com.fangao.dev.sys.service.IUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2018-09-16
 */
@Api(tags = {"用户"})
@RestController
@RequestMapping("/sys/user")
public class UserController extends BaseController<IUserService, User> {
    @ApiOperation(value = "查询 个人信息")
    @GetMapping("/info")
    public R<UserInfoDTO> queryUserInfo() {
        long id = -1;
        Account account = LoginHelper.getAccount(false);
        if (null != account) {
            id = account.getId();
        }
        return  success(baseService.queryUserInfo(id));
    }

    @ApiOperation(value = "更新 个人信息")
    @PutMapping("/info/edit")
    public R<Boolean> updateUserInfo(User user) {
        return  success(baseService.updateById(user));
    }

    @ApiOperation(value = "分页查询 所有用户")
    @GetMapping("/page")
    public R<IPage<User>> page(User user) {
        R<IPage<User>> success = success(baseService.queryUser(getPage(), user));
        return  success;
    }

    @ApiOperation(
            value = "查询 用户名username 存在与否",
            notes = "新增时，id默认-1，返回存在数量；更新时，id为用户id，返回除更新用户以外的数量"
    )
    @GetMapping("/verify/{id}/{userName}")
    public R<Integer> userNameNotUnique(@PathVariable("id") Long id,@PathVariable("userName") String userName){
        return success(baseService.userNameCount(id,userName));
    }

    @ApiOperation(value = "检查orgId组织下登记员、接待员是否唯一")

    @GetMapping("/dj_jd/check")
    public R<Integer> check_dj_jd(
            @RequestParam("orgId") long orgId,
            @RequestParam("userId") long userId,
            @RequestParam("roleIds") String roleIds,
            @RequestParam("edit") boolean edit){
        return success(baseService.check_dj_jd(orgId,userId,roleIds,edit));
    }

    @ApiOperation(value = "新增用户")
    @PostMapping("/dto")
    public R<Boolean> save(UserDTO dto) {
        return success(baseService.saveDto(dto));
    }

    @ApiOperation(value = "更新用户")
    @PutMapping("/dto/edit")
    public R<Boolean> edit(UserDTO dto) {
        return success(baseService.updateDtoById(dto));
    }

    @ApiOperation(value = "根据 id 更新状态")
    @PutMapping("/status_{id}")
    public R<Boolean> status(@PathVariable("id") Long id,
                             @RequestParam Integer status) {
        Assert.fail(0L == id, "不允许修改系统管理员状态！");
        return success(baseService.updateStatus(id, status));
    }

    @ApiOperation(value = "当前用户解锁")
    @PostMapping("/unlock")
    public R<Boolean> unlock(@RequestParam String password) {
        return baseService.unlock(getLoginUserId(), password)
                ? success(true) : R.failed("解锁失败");
    }

    @ApiOperation(value = "根据 id 重置密码")
    @PutMapping("/reset_password/{id}")
    public R<Boolean> resetPassword(@PathVariable("id") Long id) {
        Assert.fail(0L == id && getLoginUserId() != 0L, "无权重置系统管理员登录密码！");
        return success(baseService.resetPassword(id));
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("/rset_psw_own")
    public R<Boolean> resetPswOwn(ResetPswDTO resetPswDTO) {
        return success(baseService.resetPswOwn(resetPswDTO));
    }


    @ApiOperation(value = "根据 id 删除用户")
    @Override
    @DeleteMapping("/{id}")
    public R<Boolean> remove(@PathVariable("id") Long id) {
        Assert.fail(0L == id, "不允许删除系统管理员！");
        return super.remove(id);
    }
}
