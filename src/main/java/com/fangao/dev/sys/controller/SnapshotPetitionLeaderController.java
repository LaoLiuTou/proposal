package com.fangao.dev.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.entity.PetitionReceptor;
import com.fangao.dev.sys.entity.SnapshotPetitionLeader;
import com.fangao.dev.sys.service.ISnapshotPetitionLeaderService;
import com.fangao.dev.sys.service.IUserOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 领导字典表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Api(tags = {"字典_领导"})
@RestController
@RequestMapping("/sys/snapshot/leader")
public class SnapshotPetitionLeaderController extends BaseController<ISnapshotPetitionLeaderService, SnapshotPetitionLeader> {
    @Autowired
    protected IUserOrgService userOrgService;
    @ApiOperation(value = "分页查询 所有领导")
    @GetMapping("/page")
    public R<IPage<SnapshotPetitionLeader>> page(SnapshotPetitionLeader SnapshotPetitionLeader) {
        return success(baseService.queryLeaderPage(getPage(), SnapshotPetitionLeader));
    }

    @ApiOperation(value = "查询 所有正常领导")
    @GetMapping("/list_effective")
    public R<List<SnapshotPetitionLeader>> listEffective() {
        return success(baseService.listEffective());
    }

    @ApiOperation(value = "根据层级查询及自身接待处id 所有正常领导")
    @GetMapping("/list_effective_by/{levelid}")
    public R<List<SnapshotPetitionLeader>> listEffectiveByOrgIdAndLevelId(@PathVariable("levelid") Long levelid) {
        Long loginUserId = getLoginUserId();

        return success(baseService.listEffectiveByOrgIdAndLevelId((userOrgService.getOrgIdByUserId(loginUserId)),levelid));
    }

    @ApiOperation(value = "根据 id 更新状态")
    @PutMapping("/status_{id}")
    public R<Boolean> status(@PathVariable("id") Long id,
                             @RequestParam Integer status) {
        return success(baseService.updateStatus(id, status));
    }

    @ApiOperation(value = "新增领导")
    @PostMapping("")
    public R<Boolean> add(SnapshotPetitionLeader SnapshotPetitionLeader) {
        SnapshotPetitionLeader.setStatus(0);
        return success(baseService.save(SnapshotPetitionLeader));
    }
}
