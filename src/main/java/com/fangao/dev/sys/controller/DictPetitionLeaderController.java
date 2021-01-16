package com.fangao.dev.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.entity.DictPetitionLeader;
import com.fangao.dev.sys.entity.DictPetitionUnit;
import com.fangao.dev.sys.service.IDictPetitionLeaderService;
import com.fangao.dev.sys.service.IDictPetitionUnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/sys/dict/leader")
public class DictPetitionLeaderController extends BaseController<IDictPetitionLeaderService, DictPetitionLeader> {

    @ApiOperation(value = "分页查询 所有领导")
    @GetMapping("/page")
    public R<IPage<DictPetitionLeader>> page(DictPetitionLeader dictPetitionLeader) {
        return success(baseService.queryLeaderPage(getPage(), dictPetitionLeader));
    }

    @ApiOperation(value = "查询 所有正常领导")
    @GetMapping("/list_effective")
    public R<List<DictPetitionLeader>> listEffective() {
        return success(baseService.listEffective());
    }


    @ApiOperation(value = "根据 id 更新状态")
    @PutMapping("/status_{id}")
    public R<Boolean> status(@PathVariable("id") Long id,
                             @RequestParam Integer status) {
        return success(baseService.updateStatus(id, status));
    }

    @ApiOperation(value = "新增领导")
    @PostMapping("")
    public R<Boolean> add(DictPetitionLeader dictPetitionLeader) {
        dictPetitionLeader.setStatus(0);
        return success(baseService.save(dictPetitionLeader));
    }
}
