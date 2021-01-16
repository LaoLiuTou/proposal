package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.dto.JdcDTO;
import com.fangao.dev.sys.entity.LinkRePetitionerEvent;
import com.fangao.dev.sys.entity.Log;
import com.fangao.dev.sys.mapper.LinkRePetitionerEventMapper;
import com.fangao.dev.sys.mapper.LogMapper;
import com.fangao.dev.sys.service.ILinkRePetitionerEventService;
import com.fangao.dev.sys.service.ILogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 重访关系表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-10-06
 */
@Service
public class LinkRePetitionerEventServiceImpl extends ServiceImpl<LinkRePetitionerEventMapper, LinkRePetitionerEvent> implements ILinkRePetitionerEventService {

}
