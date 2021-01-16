package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.common.constant.DeleteCheckStatus;
import com.fangao.dev.common.web.LoginHelper;
import com.fangao.dev.sys.dto.CheckDTO;
import com.fangao.dev.sys.entity.DeleteCheck;
import com.fangao.dev.sys.mapper.DeleteCheckMapper;
import com.fangao.dev.sys.service.IDeleteCheckService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 删除审核表
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */
@Service
public class DeleteCheckServiceImpl extends ServiceImpl<DeleteCheckMapper, DeleteCheck> implements IDeleteCheckService {

    @Override
    public boolean applyRemoveById(String id) {
        try {
            DeleteCheck cur = baseMapper.selectOne(new QueryWrapper<DeleteCheck>().eq("petition_event_info_id", Long.parseLong(id)).orderByDesc("id"));
            if(cur == null || cur.getStatus()==2){
                DeleteCheck deleteCheck = new DeleteCheck();
                deleteCheck.setPetitionEventInfoId(Long.parseLong(id));
                deleteCheck.setUserId(LoginHelper.getAccount().getId());
                deleteCheck.setStatus(DeleteCheckStatus.WAIT_CHECK.getCode());
                saveOrUpdate(deleteCheck);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public IPage<CheckDTO> page(Page page, CheckDTO checkDTO) {
        List<CheckDTO> list =  baseMapper.selectDeleteCheck(page, checkDTO);
        page.setRecords(list);
        return page;
    }
}
