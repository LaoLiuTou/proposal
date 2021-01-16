package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.PetitionEventExtraMeeting;
import com.fangao.dev.sys.mapper.PetitionEventExtraMeetingMapper;
import com.fangao.dev.sys.service.IPetitionEventExtraMeetingService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协调会记录表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Service
public class PetitionEventExtraMeetingServiceImpl extends ServiceImpl<PetitionEventExtraMeetingMapper, PetitionEventExtraMeeting> implements IPetitionEventExtraMeetingService {

    @Override
    public boolean save(PetitionEventExtraMeeting petitionEventExtraMeeting) {
        if (null == petitionEventExtraMeeting) {
            return false;
        }
        return super.save(petitionEventExtraMeeting);
    }
}
