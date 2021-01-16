package com.fangao.dev.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.entity.DictPetitionLeaderLevel;
import com.fangao.dev.sys.entity.DictPetitionSolve;
import com.fangao.dev.sys.service.IDictPetitionLeaderLevelService;
import com.fangao.dev.sys.service.IDictPetitionSolveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 化解方式字典表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Api(tags = {"字典_化解方式"})
@RestController
@RequestMapping("/sys/dict/solve")
public class DictPetitionSolveController extends BaseController<IDictPetitionSolveService, DictPetitionSolve> {

    @ApiOperation(value = "分页查询 所有化解方式")
    @GetMapping("/page")
    public R<IPage<DictPetitionSolve>> page(DictPetitionSolve dictPetitionSolve) {
        return success(baseService.page(getPage(), dictPetitionSolve));
    }

    @ApiOperation(value = "查询 所有正常化解方式")
    @GetMapping("/list_effective")
    public R<List<DictPetitionSolve>> listEffective() {
        return success(baseService.listEffective());
    }

    @ApiOperation(value = "根据 id 更新状态")
    @PutMapping("/status_{id}")
    public R<Boolean> status(@PathVariable("id") Long id,
                             @RequestParam Integer status) {
        return success(baseService.updateStatus(id, status));
    }

    @ApiOperation(value = "新增")
    @PostMapping("")
    public R<Boolean> add(DictPetitionSolve dictPetitionSolve) {
        dictPetitionSolve.setStatus(0);
        return success(baseService.save(dictPetitionSolve));
    }
}
