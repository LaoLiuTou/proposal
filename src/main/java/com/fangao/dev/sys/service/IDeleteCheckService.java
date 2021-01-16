package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.dto.CheckDTO;
import com.fangao.dev.sys.entity.DeleteCheck;

/**
 * <p>
 * 删除审核表
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */
public interface IDeleteCheckService extends IService<DeleteCheck> {
    /*申请删除*/
    boolean applyRemoveById(String id);

    /*查询待审核的删除信息，按时间倒序排列*/
    IPage<CheckDTO> page(Page page, CheckDTO checkDTO);
}
