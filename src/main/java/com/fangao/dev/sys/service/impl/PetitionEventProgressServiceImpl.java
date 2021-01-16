package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.common.constant.CommonStatus;
import com.fangao.dev.common.web.LoginHelper;
import com.fangao.dev.sys.dto.JdcDTO;
import com.fangao.dev.sys.dto.PetitionEventProgressDTO;
import com.fangao.dev.sys.entity.PetitionEventProgress;
import com.fangao.dev.sys.mapper.PetitionEventProgressMapper;
import com.fangao.dev.sys.service.IPetitionEventProgressService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 信访事件表
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */
@Service
public class PetitionEventProgressServiceImpl extends ServiceImpl<PetitionEventProgressMapper, PetitionEventProgress> implements IPetitionEventProgressService {

    @Override
    public boolean save(Long petitionEventInfoId, String content, Long unitId) {
        PetitionEventProgress petitionEventProgress = new PetitionEventProgress();
        petitionEventProgress.setPetitionEventInfoId(petitionEventInfoId) ;
        petitionEventProgress.setContent(content);
        petitionEventProgress.setUserId(LoginHelper.getAccount().getId());
        petitionEventProgress.setUnitId(unitId);
        petitionEventProgress.setStatus(CommonStatus.NORMAL.getCode());
        return save(petitionEventProgress);
    }
    @Override
    public boolean save(JdcDTO jdcDTO) {
        PetitionEventProgress petitionEventProgress=jdcDTO.convert(PetitionEventProgress.class);
        petitionEventProgress.setPetitionEventInfoId(jdcDTO.getId());
        petitionEventProgress.setContent(jdcDTO.getProgressContent());
        petitionEventProgress.setUnitId(jdcDTO.getHandUnitId());
        petitionEventProgress.setUserId(LoginHelper.getAccount().getId());
        petitionEventProgress.setStatus(CommonStatus.NORMAL.getCode());
        petitionEventProgress.setId(null);
        return save(petitionEventProgress);
    }

    @Override
    public List<PetitionEventProgressDTO> listProgressByEventId(Long eventId) {
        return baseMapper.listProgressByEventId(eventId);
    }
}
