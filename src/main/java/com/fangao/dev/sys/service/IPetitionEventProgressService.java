package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.dto.JdcDTO;
import com.fangao.dev.sys.dto.PetitionEventProgressDTO;
import com.fangao.dev.sys.entity.PetitionEventProgress;

import java.util.List;

/**
 * <p>
 * 信访事项表
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */
public interface IPetitionEventProgressService extends IService<PetitionEventProgress> {

    boolean save(Long petitionEventInfoId, String content, Long unitId);
    boolean save(JdcDTO jdcDTO);
    List<PetitionEventProgressDTO> listProgressByEventId(Long eventId);
}
