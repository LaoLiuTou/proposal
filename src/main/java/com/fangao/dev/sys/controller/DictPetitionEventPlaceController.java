package com.fangao.dev.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.entity.DictPetitionEventPlace;
import com.fangao.dev.sys.entity.DictPetitionUnit;
import com.fangao.dev.sys.entity.Org;
import com.fangao.dev.sys.entity.Role;
import com.fangao.dev.sys.mapper.RoleMapper;
import com.fangao.dev.sys.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 事发地字典表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Api(tags = {"字典_事发地"})
@RestController
@RequestMapping("/sys/dict/event_place")
public class DictPetitionEventPlaceController extends BaseController<IDictPetitionEventPlaceService, DictPetitionEventPlace> {


    @ApiOperation(value = "分页查询 所有事发地")
    @GetMapping("/page")
    public R<IPage<DictPetitionEventPlace>> page(DictPetitionEventPlace dictPetitionEventPlace) {
        return success(baseService.page(getPage(), dictPetitionEventPlace));
    }

    @ApiOperation(value = "查询 所有正常事发地")
    @GetMapping("/list_effective")
    public R<List<DictPetitionEventPlace>> listEffective() {
        return success(baseService.listEffective());
    }

    @ApiOperation(value = "根据接待处 查询 正常事发地")
    @GetMapping("/list_effective_by_jdc")
    public R<List<DictPetitionEventPlace>> listEffectiveByJDC() {
        Long loginUserId = getLoginUserId();
        return success(baseService.listEffectiveByUserId(loginUserId));
    }

    @ApiOperation(value = "根据信访件接待单位 查询 正常事发地")
    @GetMapping("/list_effective_by_reception_org_id/{receptionOrgId}")
    public R<List<DictPetitionEventPlace>> listEffectiveByReceptionOrgId(@PathVariable("receptionOrgId") Long receptionOrgId) {
        return success(baseService.listEffectiveByReceptionOrgId(receptionOrgId));
    }

    @ApiOperation(value = "根据 id 更新状态")
    @PutMapping("/status_{id}")
    public R<Boolean> status(@PathVariable("id") Long id,
                             @RequestParam Integer status) {
        return success(baseService.updateStatus(id, status));
    }

    @ApiOperation(value = "新增事发地")
    @PostMapping("")
    public R<Boolean> add(DictPetitionEventPlace dictPetitionEventPlace) {
        dictPetitionEventPlace.setStatus(0);
        return success(baseService.save(dictPetitionEventPlace));
    }
}
