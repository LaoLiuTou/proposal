package com.fangao.dev.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.entity.DictPetitionStabilityControl;
import com.fangao.dev.sys.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *     稳控单位字典表 前端控制器
 * </p>
 *
 * @author 49030
 * @date 2019/5/14
 */
@Api(tags = {"字典_稳控单位"})
@RestController
@RequestMapping("/sys/dict/stability")
public class DictPetitionStabilityControlController extends BaseController<IDictPetitionStabilityControlService, DictPetitionStabilityControl>{
    @ApiOperation(value = "分页查询 所有单位")
    @GetMapping("/page")
    public R<IPage<DictPetitionStabilityControl>> page(DictPetitionStabilityControl dictPetitionStabilityControl) {
        return success(baseService.page(getPage(), dictPetitionStabilityControl));
    }

    @ApiOperation(value = "查询 所有正常单位")
    @GetMapping("/list_effective")
    public R<List<DictPetitionStabilityControl>> listEffective() {
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
    public R<Boolean> add(DictPetitionStabilityControl dictPetitionStabilityControl) {
        dictPetitionStabilityControl.setStatus(0);
        return success(baseService.save(dictPetitionStabilityControl));
    }
}
