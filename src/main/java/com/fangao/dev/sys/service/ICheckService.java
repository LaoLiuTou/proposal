package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fangao.dev.sys.dto.CheckDTO;

/**
 * <p>
 * 删除审核表
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */
public interface ICheckService {
        IPage<CheckDTO> page(Page page, CheckDTO checkDTO);
}
