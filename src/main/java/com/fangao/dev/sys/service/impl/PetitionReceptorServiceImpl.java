package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.common.ErrorCode;
import com.fangao.dev.sys.dto.PetitionReceptorDTO;
import com.fangao.dev.sys.entity.LinkReceptorOrg;
import com.fangao.dev.sys.entity.PetitionReceptor;
import com.fangao.dev.sys.mapper.PetitionReceptorMapper;
import com.fangao.dev.sys.service.ILinkReceptorOrgService;
import com.fangao.dev.sys.service.IPetitionReceptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 接待员表 服务实现类
 * </p>
 *
 */
@Service
public class PetitionReceptorServiceImpl extends ServiceImpl<PetitionReceptorMapper, PetitionReceptor> implements IPetitionReceptorService{

    @Autowired
    private ILinkReceptorOrgService linkReceptorOrgService;

    @Override
    public IPage<PetitionReceptor> queryPetitionReceptor(IPage<PetitionReceptor> page, PetitionReceptor receptor) {
        return page.setRecords(this.baseMapper.queryPetitionReceptor(page,receptor));
    }

    @Override
    public List<PetitionReceptor> queryPetitionReceptorByOrgs(PetitionReceptor receptor) {
        return this.baseMapper.queryPetitionReceptor(receptor);
    }

    @Override
    public int receptorNameCount(Long id, String idcard) {
        if(id != -1){
            return super.count(Wrappers.<PetitionReceptor>query().select("id").ne("id",id).eq("idcard",idcard));
        }else{
            return super.count(Wrappers.<PetitionReceptor>query().select("id").eq("idcard",idcard));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveDto(PetitionReceptorDTO dto) {
        PetitionReceptor receptor = dto.convert(PetitionReceptor.class);
        return this.save(receptor)&& linkReceptorOrgService.saveBatch(dto.getOrgIds().stream().map(id -> {
            LinkReceptorOrg receptorOrg = new LinkReceptorOrg();
            receptorOrg.setReceptorId(receptor.getId());
            receptorOrg.setOrgId(id);
            return receptorOrg;
        }).collect(Collectors.toList()));
    }

    @Override
    public boolean updateDtoById(PetitionReceptorDTO dto) {
        Assert.fail(null == dto.getId(), ErrorCode.ID_REQUIRED);
        PetitionReceptor receptor = super.getById(dto.getId());
        Assert.fail(null == receptor, "修改接待员不存在");
        return super.updateById(dto.convert(PetitionReceptor.class))
                && linkReceptorOrgService.updateByIds(dto.getId(), dto.getOrgIds());
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        PetitionReceptor receptor = new PetitionReceptor();
        receptor.setId(id);
        receptor.setStatus(status > -1 ? 0 : -1);
        return updateById(receptor);
    }

    @Override
    public List<PetitionReceptor> listPetitionReceptors(Long orgId) {
        return null;
    }
}
