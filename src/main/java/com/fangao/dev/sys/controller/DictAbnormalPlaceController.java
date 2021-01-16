package com.fangao.dev.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.entity.DictAbnormalPlace;
import com.fangao.dev.sys.service.IDictAbnormalPlaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 异常地字典表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Api(tags = {"字典_异常行为"})
@RestController
@RequestMapping("/sys/dict/abnormal_place")
public class DictAbnormalPlaceController extends BaseController<IDictAbnormalPlaceService, DictAbnormalPlace> {

    @ApiOperation(value = "分页查询 所有异常地")
    @GetMapping("/page")
    public R<IPage<DictAbnormalPlace>> page(DictAbnormalPlace dictAbnormalPlace) {
        return success(baseService.page(getPage(), dictAbnormalPlace));
    }

    @ApiOperation(value = "查询 所有启用状态的异常地")
    @GetMapping("/list_effective")
    public R<List<DictAbnormalPlace>> listEffective() {
        return success(baseService.listEffective());
    }

    @ApiOperation(value = "根据 id 更新状态")
    @PutMapping("/status_{id}")
    public R<Boolean> status(@PathVariable("id") Long id,
                             @RequestParam Integer status) {
        return success(baseService.updateStatus(id, status));
    }

    @ApiOperation(value = "新增异常地")
    @PostMapping("")
    public R<Boolean> add(DictAbnormalPlace dictAbnormalPlace) {
        dictAbnormalPlace.setStatus(0);
        return success(baseService.save(dictAbnormalPlace));
    }
}
