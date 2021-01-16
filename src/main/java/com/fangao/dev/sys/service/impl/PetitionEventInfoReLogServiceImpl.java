package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.PetitionEventInfoReLog;
import com.fangao.dev.sys.mapper.PetitionEventInfoReLogMapper;
import com.fangao.dev.sys.service.IPetitionEventInfoReLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 信访事件重访历史表
 * </p>
 *
 */
@Service
public class PetitionEventInfoReLogServiceImpl extends ServiceImpl<PetitionEventInfoReLogMapper, PetitionEventInfoReLog> implements IPetitionEventInfoReLogService{
}
