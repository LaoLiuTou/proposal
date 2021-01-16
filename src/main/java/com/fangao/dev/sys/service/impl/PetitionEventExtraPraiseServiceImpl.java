package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.PetitionEventExtraPraise;
import com.fangao.dev.sys.mapper.PetitionEventExtraPraiseMapper;
import com.fangao.dev.sys.service.IPetitionEventExtraPraiseService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 上级表扬记录表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Service
public class PetitionEventExtraPraiseServiceImpl extends ServiceImpl<PetitionEventExtraPraiseMapper, PetitionEventExtraPraise> implements IPetitionEventExtraPraiseService {

    @Override
    public boolean save(PetitionEventExtraPraise petitionEventExtraPraise) {
        if (null == petitionEventExtraPraise) {
            return false;
        }
        return super.save(petitionEventExtraPraise);
    }
}
