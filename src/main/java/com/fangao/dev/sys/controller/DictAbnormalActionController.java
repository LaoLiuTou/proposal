package com.fangao.dev.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.entity.DictAbnormalAction;
import com.fangao.dev.sys.service.IDictAbnormalActionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 异常行为字典表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Api(tags = {"字典_异常行为"})
@RestController
@RequestMapping("/sys/dict/abnormal_action")
public class DictAbnormalActionController extends BaseController<IDictAbnormalActionService, DictAbnormalAction> {

    @ApiOperation(value = "分页查询 所有异常行为")
    @GetMapping("/page")
    public R<IPage<DictAbnormalAction>> page(DictAbnormalAction dictAbnormalAction) {
        return success(baseService.page(getPage(), dictAbnormalAction));
    }

    @ApiOperation(value = "查询 所有启用状态的异常行为")
    @GetMapping("/list_effective")
    public R<List<DictAbnormalAction>> listEffective() {
        return success(baseService.listEffective());
    }

    @ApiOperation(value = "根据 id 更新状态")
    @PutMapping("/status_{id}")
    public R<Boolean> status(@PathVariable("id") Long id,
                             @RequestParam Integer status) {
        return success(baseService.updateStatus(id, status));
    }

    @ApiOperation(value = "新增异常行为")
    @PostMapping("")
    public R<Boolean> add(DictAbnormalAction dictAbnormalAction) {
        dictAbnormalAction.setStatus(0);
        return success(baseService.save(dictAbnormalAction));
    }
}
