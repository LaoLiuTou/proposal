package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.dto.*;
import com.fangao.dev.sys.entity.PetitionReceptor;
import com.fangao.dev.sys.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 接待员表 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-09-16
 */
public interface IPetitionReceptorService extends IService<PetitionReceptor> {

    /**
     * <p>
     * 分页
     * </p>
     *
     * @param page 分页对象
     * @param receptor 人员信息
     * @return
     */
    IPage<PetitionReceptor> queryPetitionReceptor(IPage<PetitionReceptor> page, PetitionReceptor receptor);

    /**
     * <p>
     * 根据orgs查询所有接待人
     * </p>
     *
     * @param receptor 人员信息
     * @return
     */
    List<PetitionReceptor> queryPetitionReceptorByOrgs(PetitionReceptor receptor);



    /**
     * <p>
     * 查询人员是否已存在
     * </p>
     * @param id 人员 ID
     * @param idcard 身份证号
     * @return
     */
    int receptorNameCount(Long id, String idcard);


    /**
     * <p>
     * 保存接待员组织关系信息
     * </p>
     *
     * @param dto 人员 DTO
     * @return
     */
    boolean saveDto(PetitionReceptorDTO dto);



    /**
     * <p>
     * 修改接待员组织关系信息
     * </p>
     *
     * @param dto 人员 DTO
     * @return
     */
    boolean updateDtoById(PetitionReceptorDTO dto);


    /**
     * <p>
     * 更新接待员状态
     * </p>
     *
     * @param id     人员 ID
     * @param status 状态
     * @return
     */
    boolean updateStatus(Long id, Integer status);


    /**
     * 根据组织查询接待员
     * @param orgId 组织 ID
     * @return
     */
    List<PetitionReceptor> listPetitionReceptors(Long orgId);

}
