package com.fangao.dev.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.dto.RoleResourceDTO;
import com.fangao.dev.sys.entity.DictPetitionUnit;
import com.fangao.dev.sys.entity.Role;
import com.fangao.dev.sys.service.IDictPetitionUnitService;
import com.fangao.dev.sys.service.IRoleResourceService;
import com.fangao.dev.sys.service.IRoleService;
import com.fangao.dev.sys.service.IUserRoleService;
import com.fangao.dev.sys.vo.ResourceZTreeVO;
import com.fangao.dev.sys.vo.UserRoleSelectedVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 单位字典表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Api(tags = {"字典_单位"})
@RestController
@RequestMapping("/sys/dict/unit")
public class DictPetitionUnitController extends BaseController<IDictPetitionUnitService, DictPetitionUnit> {

    @ApiOperation(value = "分页查询 所有单位")
    @GetMapping("/page")
    public R<IPage<DictPetitionUnit>> page(DictPetitionUnit dictPetitionUnit) {
        return success(baseService.page(getPage(), dictPetitionUnit));
    }

    @ApiOperation(value = "查询 所有正常单位")
    @GetMapping("/list_effective")
    public R<List<DictPetitionUnit>> listEffective() {
        return success(baseService.listEffective());
    }

    @ApiOperation(value = "根据 id 更新状态")
    @PutMapping("/status_{id}")
    public R<Boolean> status(@PathVariable("id") Long id,
                             @RequestParam Integer status) {
        return success(baseService.updateStatus(id, status));
    }

    @ApiOperation(value = "新增单位")
    @PostMapping("")
    public R<Boolean> add(DictPetitionUnit dictPetitionUnit) {
        List<DictPetitionUnit> list = baseService.list(new QueryWrapper<DictPetitionUnit>().eq("status",0).eq("name",dictPetitionUnit.getName()));
        if(CollectionUtils.isNotEmpty(list)) return failed("该单位已存在！");
        dictPetitionUnit.setStatus(0);
        return success(baseService.save(dictPetitionUnit));
    }
}
