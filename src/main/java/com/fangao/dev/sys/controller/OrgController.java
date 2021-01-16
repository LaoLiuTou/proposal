package com.fangao.dev.sys.controller;


import com.baomidou.mybatisplus.extension.api.IErrorCode;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.entity.Org;
import com.fangao.dev.sys.service.IOrgService;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.sys.service.IUserOrgService;
import com.fangao.dev.sys.vo.UserOrgSelectedVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统组织表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2018-11-07
 */
@Api(tags = {"组织"})
@RestController
@RequestMapping("/sys/org")
public class OrgController extends BaseController<IOrgService, Org> {

    @Autowired
    private IUserOrgService userOrgService;

    @ApiOperation(value = "根据 顶级组织id 查询其下所有组织")
    @GetMapping("/list_{topOrgId}")
    public R<List<Org>> selectTopList(@PathVariable("topOrgId") Long topOrgId) {
        return success(baseService.selectTopList(topOrgId));
    }

    @ApiOperation(value = "查询所有顶级组织")
    @GetMapping("/top/list")
    public R<List<Org>> listTop() {
        return success(baseService.listTopOrg());
    }

    @ApiOperation(value = "查询所有组织")
    @GetMapping("/all/list")
    public R<List<Org>> list() {
        return success(baseService.listAll(true));
    }

    @ApiOperation(value = "根据 用户id 查询其所属组织")
    @GetMapping("/all/list_{userId}")
    public R<List<UserOrgSelectedVO>> listSelected(@PathVariable("userId") Long userId) {
        return success(userOrgService.listSelectedVO(userId));
    }

    @ApiOperation(value = "新增组织")
    @PostMapping("")
    public R<Boolean> add(Org org) {
        org.setSort(0);
        return success(baseService.save(org));
    }

    @ApiOperation(value = "新增顶级组织")
    @PostMapping("/addtop")
    public R<Long> addBackId(Org org) {
        org.setSort(0);
        R<Boolean> r = this.success(this.baseService.addTopOrg(org));
        return r.getData()?R.ok(org.getId()):R.failed("添加失败");
    }
}
