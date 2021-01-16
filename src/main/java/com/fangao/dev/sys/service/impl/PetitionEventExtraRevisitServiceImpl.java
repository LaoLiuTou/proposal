package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.PetitionEventExtraRevisit;
import com.fangao.dev.sys.mapper.PetitionEventExtraRevisitMapper;
import com.fangao.dev.sys.service.IPetitionEventExtraRevisitService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 回访情况记录表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Service
public class PetitionEventExtraRevisitServiceImpl extends ServiceImpl<PetitionEventExtraRevisitMapper, PetitionEventExtraRevisit> implements IPetitionEventExtraRevisitService {

    @Override
    public boolean save(PetitionEventExtraRevisit petitionEventExtraRevisit) {
        if (null == petitionEventExtraRevisit) {
            return false;
        }
        return super.save(petitionEventExtraRevisit);
    }
}
