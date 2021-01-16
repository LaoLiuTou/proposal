package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.PetitionEventExtraHandle;
import com.fangao.dev.sys.mapper.PetitionEventExtraHandleMapper;
import com.fangao.dev.sys.service.IPetitionEventExtraHandleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 督办记录表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Service
public class PetitionEventExtraHandleServiceImpl extends ServiceImpl<PetitionEventExtraHandleMapper, PetitionEventExtraHandle> implements IPetitionEventExtraHandleService {

    @Override
    public boolean save(PetitionEventExtraHandle petitionEventExtraHandle) {
        if (null == petitionEventExtraHandle) {
            return false;
        }
        return super.save(petitionEventExtraHandle);
    }
}
