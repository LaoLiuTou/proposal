package com.fangao.dev.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.dto.*;
import com.fangao.dev.sys.entity.Org;
import com.fangao.dev.sys.entity.PetitionerInfo;
import com.fangao.dev.sys.service.*;
import com.fangao.dev.sys.vo.ReceptionOrgSelectedVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 登记处管理 前端控制器
 * </p>
 *
 * @author ykb
 * @since 2019-03-24
 */
@Api(tags = {"登记处"})
@RestController
@RequestMapping("/djc")
public class DjcController extends BaseController<IPetitionerInfoService, PetitionerInfo> {
    @Autowired
    protected DjcService djcService;
    @Autowired
    protected IUserService userService;

    @Override
    public R<Boolean> edit(PetitionerInfo var1) {
        return super.edit(var1);
    }

    @Autowired
    protected IOrgService orgService;
    @Autowired
    protected IUserOrgService userOrgService;
    @Autowired
    protected IDeleteCheckService deleteCheckService;

    @PostMapping("/djxx")
    public R<Boolean> djxx(DjcDTO djcDTO) {
        return djcService.addInfo(djcDTO);
    }

    @PostMapping("/djxx_eventnum")
    public R<String> djxxGetEventNum(DjcDTO djcDTO) {
        Org org = new Org();
        org.setId(djcDTO.getReceptionOrgId());
        org = orgService.getById(org);
        return success(djcService.addInfoGetEventNum(djcDTO)+"_"+org.getName()+"_"+(org.getAddress()!=null?org.getAddress():""));
    }

    @GetMapping("/djgl/page")
    public R<IPage<DjcDTO>> page(DjcDTO djcDTO) {
        Long loginUserId = getLoginUserId();
        djcDTO.setDjcOrgId(userOrgService.getOrgIdByUserId(loginUserId));
        R<IPage<DjcDTO>> iPageR = success(djcService.page(getPage(),djcDTO));
        return iPageR;
    }

    @GetMapping("/djgl/getInfoByIdCard")
    public R<PetitionerInfo> getInfoByIdCard(String idcard) {
        PetitionerInfo petitionerInfo = baseService.getOne(new QueryWrapper<PetitionerInfo>().eq("idcard", idcard));
        return success(petitionerInfo);
    }

    @GetMapping("/djxx/listReceptionUser")
    public R<List<UserDTO>> listReceptionUser(UserDTO userDTO) {
        R<List<UserDTO>> listUser = success(userService.listReceptionUser());
        return listUser;
    }

    @GetMapping("/djxx/listReceptionOrg")
    public R<List<ReceptionOrgSelectedVO>> listReceptionOrg() {
        R<List<ReceptionOrgSelectedVO>> listOrg = success(orgService.listReceptionOrg());
        return listOrg;
    }

    @ApiOperation(
            value = "领导控制台 按type查询登记数",
            notes = "type  1：周  2：月  3：年  4：总"
    )
    @GetMapping("/statis/console_{type}")
    public R<Long> statisDJNum(@PathVariable("type") Integer type){
        return success(djcService.statisDJNum(type));
    }

    @ApiOperation(value = "新领导控制台 查询登记批次、人次")
    @GetMapping("/statis/new_console")
    public R<StatisDTO> statisDJCountAndPeople(@RequestParam("start") String start, @RequestParam("end") String end){
        return success(djcService.statisDJCountAndPeople(start,end));
    }

    @ApiOperation(value = "新领导控制台 查询日登记批次、人次")
    @GetMapping("/statis/new_console_every_day")
    public R<List<StatisDTO>> statisEveryDayCountAndPeople(@RequestParam("start") String start, @RequestParam("end") String end){
        return success(djcService.statisEveryDayCountAndPeople(start,end));
    }

    @PostMapping("/edit")
    public R<Boolean> edit(DjcDTO djcDTO) {
        boolean result = djcService.editInfo(djcDTO);
        return success(result);
    }

    @DeleteMapping("/remove/{eventId}")
    public R<Boolean> remove(@PathVariable("eventId") String eventId) {
        boolean b = deleteCheckService.applyRemoveById(eventId);
        return success(b);
    }

    @GetMapping("/statistics/countPetitonerSex")
    public R<List<CountPetitonerSexDTO>> countPetitonerSex() {
        R<List<CountPetitonerSexDTO>> countSex = success(baseService.countPetitonerSex());
        return countSex;
    }

    @GetMapping("/statistics/countUserOfDate")
    public R<List<CountPetitonerNumDTO>> countUserOfDate() {
        R<List<CountPetitonerNumDTO>> countUser = success(baseService.countPetitonerNumOfDate());
        return countUser;
    }

    @GetMapping("/statistics/countPetitonerAge")
    public R<Map<String, Object>> countPetitonerAge() {
        R<Map<String, Object>> success = success(baseService.countPetitonerAge());
        return success;
    }
}
