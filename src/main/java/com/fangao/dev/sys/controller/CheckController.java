package com.fangao.dev.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.common.ErrorCode;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.dto.CheckDTO;
import com.fangao.dev.sys.entity.DeleteCheck;
import com.fangao.dev.sys.service.ICheckService;
import com.fangao.dev.sys.service.IDeleteCheckService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * <p>
 * 登记处管理 前端控制器
 * </p>
 *
 * @author ykb
 * @since 2019-03-24
 */
@Api(tags = {"审核"})
@RestController
@RequestMapping("/check")
public class CheckController extends BaseController<IDeleteCheckService, DeleteCheck> {
    @Autowired
    private ICheckService checkService;

    @GetMapping("/delete/page")
    public R<IPage<CheckDTO>> page(CheckDTO checkDTO,String submitDate) throws ParseException {
        if(StringUtils.isNotBlank(submitDate)){
            String[] split = submitDate.split(" - ");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            checkDTO.setBeginSubmitDate(simpleDateFormat.parse(split[0]));
            checkDTO.setEndSubmitDate(simpleDateFormat.parse(split[1]));
        }
        R<IPage<CheckDTO>> iPageR = success(checkService.page(getPage(),checkDTO));
        return iPageR;
    }

    @PutMapping("/delete/changeStaus")
    public R<Boolean> changeStatus(CheckDTO checkDTO) {
        Assert.fail(null == checkDTO.getId(), ErrorCode.ID_REQUIRED);
        DeleteCheck deleteCheck = new DeleteCheck();
        BeanUtils.copyProperties(checkDTO,deleteCheck);
        return this.success(this.baseService.updateById(deleteCheck));
    }

}
