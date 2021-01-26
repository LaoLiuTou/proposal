package com.fangao.dev.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.dto.JdcDTO;
import com.fangao.dev.sys.dto.PetitionReceptorDTO;
import com.fangao.dev.sys.entity.PetitionReceptor;
import com.fangao.dev.sys.service.IPetitionEventInfoService;
import com.fangao.dev.sys.service.IPetitionReceptorService;
import com.fangao.dev.sys.service.IUserOrgService;
import com.fangao.dev.sys.service.IUserRoleService;
import com.fangao.dev.sys.vo.UserRoleSelectedVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 接待人员表 前端控制器
 * </p>
 *
 */
@Api(tags = {"接待人员"})
@RestController
@RequestMapping("/jdry")
public class ReceptorController extends BaseController<IPetitionReceptorService, PetitionReceptor> {

    @Autowired
    protected IUserOrgService userOrgService;

    @Autowired
    private IPetitionEventInfoService petitionEventInfoService;
    @Autowired
    protected IUserRoleService userRoleService;
    @ApiOperation(value = "orgId接待处下 所有接待员")
    @GetMapping("/list")
    public R<List<PetitionReceptor>> list() {
        //改成所有接待员
        Long loginUserId = getLoginUserId();
        String roleNames = "";
        List<UserRoleSelectedVO> roleList = userRoleService.listSelectedVO(loginUserId);
        if(roleList != null && roleList.size() > 0){
            for(UserRoleSelectedVO userRoleSelectedVO: roleList){
                if(userRoleSelectedVO.getSelected() != null && userRoleSelectedVO.getSelected()){
                    roleNames+=userRoleSelectedVO.getName()+"，";
                }
            }
        }
        PetitionReceptor receptor = new PetitionReceptor();
        if(!roleNames.contains("管理员")){
            receptor.setOrgs(String.valueOf(userOrgService.getOrgIdByUserId(loginUserId)));
        }
        return  success(baseService.queryPetitionReceptorByOrgs(receptor));
    }

    @ApiOperation(value = "领导编辑时 信访件所接待组织下的所有接待员")
    @GetMapping("/list_xfld/{receptionOrgId}")
    public R<List<PetitionReceptor>> list(@PathVariable("receptionOrgId") Long receptionOrgId) {
        PetitionReceptor receptor = new PetitionReceptor();
        receptor.setOrgs(String.valueOf(receptionOrgId));
        return  success(baseService.queryPetitionReceptorByOrgs(receptor));
    }

    @ApiOperation(value = "分页查询 所有接待员")
    @GetMapping("/page")
    public R<IPage<PetitionReceptor>> page(PetitionReceptor receptor) {
        return  success(baseService.queryPetitionReceptor(getPage(), receptor));
    }

    @ApiOperation(
            value = "查询 接待员姓名和身份证 存在与否",
            notes = "新增时，id默认-1，返回存在数量；更新时，id为接待员id，返回除更新接待员以外的数量"
    )
    @GetMapping("/verify/{id}/{idcard}")
    public R<Integer> userNameNotUnique(@PathVariable("id") Long id,@PathVariable("idcard") String idcard){
        return success(baseService.receptorNameCount(id,idcard));
    }

    @ApiOperation(value = "新增接待员")
    @PostMapping("/dto")
    public R<Boolean> save(PetitionReceptorDTO dto) {
        return success(baseService.saveDto(dto));
    }

    @ApiOperation(value = "更新接待员")
    @PutMapping("/dto/edit")
    public R<Boolean> edit(PetitionReceptorDTO dto) {
        return success(baseService.updateDtoById(dto));
    }

    @ApiOperation(value = "根据 id 更新状态")
    @PutMapping("/status_{id}")
    public R<Boolean> status(@PathVariable("id") Long id,
                             @RequestParam Integer status) {
        return success(baseService.updateStatus(id, status));
    }
}
