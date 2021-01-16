package com.fangao.dev.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.common.ErrorCode;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.entity.DictPetitionContentType;
import com.fangao.dev.sys.entity.DictPetitionUnit;
import com.fangao.dev.sys.service.IDictPetitionContentTypeService;
import com.fangao.dev.sys.service.IDictPetitionUnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 内容分类字典表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Api(tags = {"字典_内容分类"})
@RestController
@RequestMapping("/sys/dict/content_type")
public class DictPetitionContentTypeController extends BaseController<IDictPetitionContentTypeService, DictPetitionContentType> {

    @ApiOperation(value = "分页查询 所有内容分类")
    @GetMapping("/page")
    public R<IPage<DictPetitionContentType>> page(DictPetitionContentType dictPetitionContentType) {
        return success(baseService.page(getPage(), dictPetitionContentType));
    }

    @ApiOperation(value = "查询 所有正常内容分类")
    @GetMapping("/list_effective")
    public R<List<DictPetitionContentType>> listEffective() {
        return success(baseService.listEffective());
    }

    @ApiOperation(value = "查询 所有资源")
    @GetMapping("/list")
    public R<List<DictPetitionContentType>> listAll() {
        return success(baseService.listAll());
    }

    @ApiOperation(value = "根据 id 更新状态")
    @PutMapping("/status_{id}")
    public R<Boolean> status(@PathVariable("id") Long id,
                             @RequestParam Integer status) {
        return success(baseService.updateStatus(id, status));
    }

    @ApiOperation(value = "新增内容分类")
    @PostMapping("")
    public R<Boolean> add(DictPetitionContentType dictPetitionContentType) {
        dictPetitionContentType.setStatus(0);
        boolean returnFalg=true,topFlag = false;
        if(dictPetitionContentType.getPid() == null || dictPetitionContentType.getPid() == 0) topFlag = true;
        returnFalg = returnFalg && baseService.save(dictPetitionContentType);
        if(returnFalg){
            if(topFlag) {
                dictPetitionContentType.setRid(dictPetitionContentType.getId());
            }else{
                DictPetitionContentType parent = new DictPetitionContentType();
                parent.setId(dictPetitionContentType.getPid());
                parent = baseService.getById(parent);
                dictPetitionContentType.setRid(parent.getRid());
            }
            returnFalg = returnFalg && baseService.updateById(dictPetitionContentType);
        }
        return success(returnFalg);
    }

    @ApiOperation(value = "修改内容分类")
    @PutMapping("")
    public R<Boolean> edit(DictPetitionContentType dictPetitionContentType) {
        Assert.fail(null == dictPetitionContentType.getId(), ErrorCode.ID_REQUIRED);
        if(dictPetitionContentType.getPid() != 0){
            DictPetitionContentType parent = new DictPetitionContentType();
            parent.setId(dictPetitionContentType.getPid());
            dictPetitionContentType.setRid(baseService.getById(parent).getRid());
        }else{
            dictPetitionContentType.setRid(dictPetitionContentType.getId());
        }
        return this.success(this.baseService.updateById(dictPetitionContentType));
    }
}
