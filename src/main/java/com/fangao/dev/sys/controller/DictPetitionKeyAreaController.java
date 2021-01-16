package com.fangao.dev.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.entity.DictPetitionKeyArea;
import com.fangao.dev.sys.entity.DictPetitionUnit;
import com.fangao.dev.sys.service.IDictPetitionKeyAreaService;
import com.fangao.dev.sys.service.IDictPetitionUnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 重点领域分类字典表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Api(tags = {"字典_重点领域分类"})
@RestController
@RequestMapping("/sys/dict/key_area")
public class DictPetitionKeyAreaController extends BaseController<IDictPetitionKeyAreaService, DictPetitionKeyArea> {

    @ApiOperation(value = "分页查询 所有重点领域分类")
    @GetMapping("/page")
    public R<IPage<DictPetitionKeyArea>> page(DictPetitionKeyArea dictPetitionKeyArea) {
        return success(baseService.page(getPage(), dictPetitionKeyArea));
    }

    @ApiOperation(value = "查询 所有正常重点领域分类")
    @GetMapping("/list_effective")
    public R<List<DictPetitionKeyArea>> listEffective() {
        return success(baseService.listEffective());
    }

    @ApiOperation(value = "根据 id 更新状态")
    @PutMapping("/status_{id}")
    public R<Boolean> status(@PathVariable("id") Long id,
                             @RequestParam Integer status) {
        return success(baseService.updateStatus(id, status));
    }

    @ApiOperation(value = "新增重点领域分类")
    @PostMapping("")
    public R<Boolean> add(DictPetitionKeyArea dictPetitionKeyArea) {
        dictPetitionKeyArea.setStatus(0);
        return success(baseService.save(dictPetitionKeyArea));
    }
}
