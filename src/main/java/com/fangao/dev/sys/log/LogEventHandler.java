package com.fangao.dev.sys.log;

import com.fangao.dev.common.spring.SpringHelper;
import com.fangao.dev.sys.service.ILogService;
import com.lmax.disruptor.EventHandler;

/**
 * <p>
 * lmax disruptor EventHandler
 * </p>
 *
 * @author jobob
 * @since 2018-10-07
 */
public class LogEventHandler implements EventHandler<LogEvent> {

    @Override
    public void onEvent(LogEvent event, long sequence, boolean endOfBatch) throws Exception {
        ILogService logService = SpringHelper.getBean(ILogService.class);
        logService.save(event.getLog());
    }
}
