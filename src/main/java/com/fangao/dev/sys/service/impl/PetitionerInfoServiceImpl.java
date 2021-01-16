package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.dto.CountPetitonerAgeDTO;
import com.fangao.dev.sys.dto.CountPetitonerNumDTO;
import com.fangao.dev.sys.dto.CountPetitonerSexDTO;
import com.fangao.dev.sys.dto.DjcDTO;
import com.fangao.dev.sys.entity.PetitionerInfo;
import com.fangao.dev.sys.mapper.PetitionerInfoMapper;
import com.fangao.dev.sys.service.IPetitionerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 访问人信息表
 * </p>
 *
 * @author ykb
 * @since 2019-03-24
 */
@Service
public class PetitionerInfoServiceImpl extends ServiceImpl<PetitionerInfoMapper, PetitionerInfo> implements IPetitionerInfoService {



    @Override
    public IPage<DjcDTO> page(Page page, DjcDTO djcDTO) {
        List<DjcDTO> listDjcDTO = baseMapper.selectPetitionerOfEventNum(page,djcDTO);
        page.setRecords(listDjcDTO);
        return page;
    }

    @Override
    public int updateForSet(PetitionerInfo petitionerInfo) {
        return baseMapper.updateForSet(petitionerInfo);
    }

    @Override
    public List<CountPetitonerNumDTO> countPetitonerNumOfDate() {
        return baseMapper.countPetitonerNumOfDate();
    }

    @Override
    public List<CountPetitonerSexDTO> countPetitonerSex() {
        return baseMapper.countPetitonerSex();
    }

    @Override
    public Map<String,Object> countPetitonerAge() {
        List<CountPetitonerAgeDTO> countPetitonerAgeDTOs = baseMapper.countPetitonerAge();
        int total = count();
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("listData",countPetitonerAgeDTOs);
        return map;
    }
}
