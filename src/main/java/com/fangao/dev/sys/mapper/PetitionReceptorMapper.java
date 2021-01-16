package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fangao.dev.sys.dto.UserDTO;
import com.fangao.dev.sys.dto.UserInfoDTO;
import com.fangao.dev.sys.dto.UserResourceDTO;
import com.fangao.dev.sys.entity.PetitionReceptor;
import com.fangao.dev.sys.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 接待员表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-09-16
 */

@Mapper
public interface PetitionReceptorMapper extends BaseMapper<PetitionReceptor> {

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @return
     */
    List<PetitionReceptor> queryPetitionReceptor(IPage<PetitionReceptor> page, @Param(value = "receptor") PetitionReceptor receptor);

    /**
     * <p>
     * 根据orgs查询
     * </p>
     *
     * @return
     */
    List<PetitionReceptor> queryPetitionReceptor(@Param(value = "receptor") PetitionReceptor receptor);
}
