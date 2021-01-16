package com.fangao.dev.sys.service;

import com.fangao.dev.sys.entity.Log;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统日志表 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-10-06
 */
public interface ILogService extends IService<Log> {
    boolean add(Log log);
}
