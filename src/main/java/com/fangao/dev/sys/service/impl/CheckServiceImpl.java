package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fangao.dev.sys.dto.CheckDTO;
import com.fangao.dev.sys.service.ICheckService;
import com.fangao.dev.sys.service.IDeleteCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 删除审核表
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */
@Service
public class CheckServiceImpl implements ICheckService {
    @Autowired
    private IDeleteCheckService deleteCheckService;

    @Override
    public IPage<CheckDTO> page(Page page, CheckDTO checkDTO) {
        IPage<CheckDTO> ipage = deleteCheckService.page(page, checkDTO);
        return ipage;
    }
}
