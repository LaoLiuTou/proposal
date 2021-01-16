package com.fangao.dev.sys.service.impl;

import com.fangao.dev.sys.entity.Log;
import com.fangao.dev.sys.mapper.LogMapper;
import com.fangao.dev.sys.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-10-06
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {
    @Override
    public boolean add(Log log) {
        if (null == log) {
            return false;
        }
        log.setCreateTime(LocalDateTime.now());
        return super.save(log);
    }
}
