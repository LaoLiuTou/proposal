package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fangao.dev.sys.dto.CountPetitonerAgeDTO;
import com.fangao.dev.sys.dto.CountPetitonerNumDTO;
import com.fangao.dev.sys.dto.CountPetitonerSexDTO;
import com.fangao.dev.sys.dto.DjcDTO;
import com.fangao.dev.sys.entity.PetitionerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 信访人基本信息表 Mapper 接口
 * </p>
 *
 * @author ykb
 * @since 2019-03-24
 */

@Mapper
public interface PetitionerInfoMapper extends BaseMapper<PetitionerInfo> {

    List<DjcDTO> selectPetitionerOfEventNum(IPage<DjcDTO> page, @Param(value = "djcDTO") DjcDTO djcDTO);

    int updateForSet(@Param(value = "petitionerInfo") PetitionerInfo petitionerInfo);

    List<CountPetitonerNumDTO> countPetitonerNumOfDate();

    List<CountPetitonerSexDTO> countPetitonerSex();

    List<CountPetitonerAgeDTO> countPetitonerAge();
}
