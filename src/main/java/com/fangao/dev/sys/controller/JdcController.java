package com.fangao.dev.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.dto.*;
import com.fangao.dev.sys.entity.*;
import com.fangao.dev.sys.service.*;
import com.fangao.dev.sys.utils.ExportExcelUtils;
import com.fangao.dev.sys.utils.FileUtil;
import com.fangao.dev.sys.vo.DrawPetitionCatIntervalVO;
import com.fangao.dev.sys.vo.DrawPetitionLineVO;
import com.fangao.dev.sys.vo.DrawPetitionPieVO;
import com.fangao.dev.sys.vo.UserRoleSelectedVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 登记处管理 前端控制器
 * </p>
 *
 * @author ykb
 * @since 2019-03-24
 */
@Api(tags = {"接待处"})
@RestController
@RequestMapping("/jdc")
public class JdcController extends BaseController<IPetitionEventInfoService, PetitionEventInfo> {
    @Autowired
    protected JdcService jdcService;
    @Autowired
    protected IUserOrgService userOrgService;
    @Autowired
    protected IOrgService orgService;
    @Autowired
    protected IPetitionEventInfoService petitionEventInfoService;
    @Autowired
    protected IPetitionEventProgressService petitionEventProgressService;
    @Autowired
    protected IDeleteCheckService deleteCheckService;
    @Autowired
    protected IPetitionReceptorService petitionReceptorService;
    @Autowired
    protected JdcImportExcelService jdcImportExcelService;
    @Autowired
    protected IPetitionEventExtraPraiseService petitionEventExtraPraiseService;
    @Autowired
    protected IPetitionEventExtraMeetingService petitionEventExtraMeetingService;
    @Autowired
    protected IPetitionEventExtraRevisitService petitionEventExtraRevisitService;
    @Autowired
    protected IPetitionEventExtraHandleService petitionEventExtraHandleService;
    @Autowired
    protected IDictPetitionSolveService dictPetitionSolveService;
    @Autowired
    protected IUserService userService;
    @Autowired
    protected IUserRoleService userRoleService;
    @GetMapping("/djdr/page")
    public R<IPage<JdcDTO>> page(JdcDTO jdcDTO) {
        Long loginUserId = getLoginUserId();
        jdcDTO.setReceptionOrgId(userOrgService.getOrgIdByUserId(loginUserId));
        R<IPage<JdcDTO>> iPageR = success(jdcService.djdrPage(getPage(), jdcDTO));
        return iPageR;
    }

    @PostMapping("/djxx")
    public R<Boolean> djxx(JdcDTO jdcDTO) {
        String contentTypeIdStr = jdcDTO.getContentTypeIdStr();
        if(StringUtils.isNotBlank(contentTypeIdStr)){
            String[] split = contentTypeIdStr.split("/");
            int index = split.length-1;
            long contentTypeId = Long.parseLong(split[index]);
            jdcDTO.setContentTypeId(contentTypeId);
        }
        jdcDTO.setEventNum(null);
        return jdcService.addEventInfo(jdcDTO);
    }

    @PutMapping("/djxx/edit")
    public R<Boolean> djxxEdit(JdcDTO jdcDTO) {
        String contentTypeIdStr = jdcDTO.getContentTypeIdStr();
        if(StringUtils.isNotBlank(contentTypeIdStr)){
            String[] split = contentTypeIdStr.split("/");
            int index = split.length-1;
            long contentTypeId = Long.parseLong(split[index]);
            jdcDTO.setContentTypeId(contentTypeId);
        }
        return jdcService.editEventInfo(jdcDTO);
    }

    @PutMapping("/djxx/edit_xfld")
    public R<Boolean> djxxEditXFLD(JdcDTO jdcDTO) {
        String contentTypeIdStr = jdcDTO.getContentTypeIdStr();
        if(StringUtils.isNotBlank(contentTypeIdStr)){
            String[] split = contentTypeIdStr.split("/");
            int index = split.length-1;
            long contentTypeId = Long.parseLong(split[index]);
            jdcDTO.setContentTypeId(contentTypeId);
        }
        return jdcService.editEventInfo(jdcDTO);
    }

    @PutMapping("/xxgl/solve")
    public R<Boolean> xxglEditDictSolveId(PetitionEventInfo info){

        DictPetitionSolve dps = new DictPetitionSolve();
        dps.setStatus(0);
        dps.setName(info.getName());
        dps.setSort(0);

        List<DictPetitionSolve> list=dictPetitionSolveService.list(new QueryWrapper<DictPetitionSolve>().eq("name",info.getName()));

        if(list.size()>0){
            info.setDictSolveId(list.get(0).getId());
            return success(baseService.update(new UpdateWrapper<PetitionEventInfo>().set("dict_solve_id",info.getDictSolveId()).eq("id",info.getId())));

        }
        else{
            if(dictPetitionSolveService.save(dps)){
                info.setDictSolveId(dps.getId());
                return success(baseService.update(new UpdateWrapper<PetitionEventInfo>().set("dict_solve_id",info.getDictSolveId()).eq("id",info.getId())));
            }
            else{
                return failed("选择失败");
            }
        }



}

    @PutMapping("/xxgl/satisfaction")
    public R<Boolean> xxglEditDictSatisfactionId(PetitionEventInfo info){
        return success(baseService.update(new UpdateWrapper<PetitionEventInfo>().set("dict_satisfaction_id",info.getDictSatisfactionId()).eq("id",info.getId())));
    }

    @PostMapping("/xxgl/praise")
    public R<Boolean> xxglAddExtraPraise(PetitionEventExtraPraise temp){
        return success(petitionEventExtraPraiseService.save(temp));
    }

    @PostMapping("/xxgl/revisit")
    public R<Boolean> xxglAddExtraRevisit(PetitionEventExtraRevisit temp){
        return success(petitionEventExtraRevisitService.save(temp));
    }

    @PostMapping("/xxgl/meeting")
    public R<Boolean> xxglAddExtraMeeting(PetitionEventExtraMeeting temp){
        return success(petitionEventExtraMeetingService.save(temp));
    }

    @PostMapping("/xxgl/db/handle")
    public R<Boolean> xxglAddExtraHandle(PetitionEventExtraHandle temp){
        return success(petitionEventExtraHandleService.save(temp));
    }

    @GetMapping("/xxgl/page")
    public R<IPage<JdcDTO>> xxglPage(JdcDTO jdcDTO) {
        Long loginUserId = getLoginUserId();
        //UserInfoDTO userInfo =userService.queryUserInfo(loginUserId);
        String roleNames = "";
        List<UserRoleSelectedVO> roleList = userRoleService.listSelectedVO(loginUserId);
        if(roleList != null && roleList.size() > 0){
            for(UserRoleSelectedVO userRoleSelectedVO: roleList){
                if(userRoleSelectedVO.getSelected() != null && userRoleSelectedVO.getSelected()){
                    roleNames+=userRoleSelectedVO.getName()+"，";
                }
            }
        }
        if(jdcDTO.getReceptionOrgId()==null&&!roleNames.contains("管理员")){
            jdcDTO.setReceptionOrgId(userOrgService.getOrgIdByUserId(loginUserId));
        }
        //jdcDTO.setReceptionOrgId(userOrgService.getOrgIdByUserId(loginUserId));
        R<IPage<JdcDTO>> iPageR = success(jdcService.xxglPage(getPage(), jdcDTO));
        return iPageR;
    }

    @GetMapping("/xxgl/export")
    public void xxglPageExport(@RequestParam("status") String status,
                               @RequestParam("name") String name,
                               @RequestParam("petitionerName") String petitionerName,
                               @RequestParam("eventNum") String eventNum,
                               @RequestParam("highSearch") String highSearch,
                               @RequestParam("cols[]") List<String> cols,
                               @RequestParam("colNames[]") List<String> colNames,
                               HttpServletResponse response) throws IOException{
        try{
            JdcDTO jdcDTO = new JdcDTO();
            jdcDTO.setStatus(Integer.valueOf(status));
            jdcDTO.setName(name);
            jdcDTO.setPetitionerName(petitionerName);
            jdcDTO.setEventNum(eventNum);
            jdcDTO.setHighSearch(highSearch);
            Long loginUserId = getLoginUserId();
            jdcDTO.setReceptionOrgId(userOrgService.getOrgIdByUserId(loginUserId));
            List<Map<String,Object>> list = jdcService.xxglPageExport(jdcDTO);
            ExportExcelUtils.downLoadExcel("信访件导出","信访件","信访件导出",cols,colNames,list,response);
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(response.SC_NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/xxgl/page_xfld")
    public R<IPage<JdcDTO>> xxglPageSFLD(JdcDTO jdcDTO) {
        R<IPage<JdcDTO>> iPageR = success(jdcService.xxglPage(getPage(), jdcDTO));
        return iPageR;
    }

    @GetMapping("/xxgl/export_xfld")
    public void xxglPageExportSFLD(@RequestParam("status") String status,
                                  @RequestParam("name") String name,
                                  @RequestParam("petitionerName") String petitionerName,
                                  @RequestParam("eventNum") String eventNum,
                                  @RequestParam("highSearch") String highSearch,
                                  @RequestParam("cols[]") List<String> cols,
                                  @RequestParam("colNames[]") List<String> colNames,
                                  HttpServletResponse response) throws IOException{
        try{
            JdcDTO jdcDTO = new JdcDTO();
            jdcDTO.setStatus(Integer.valueOf(status));
            jdcDTO.setName(name);
            jdcDTO.setPetitionerName(petitionerName);
            jdcDTO.setEventNum(eventNum);
            jdcDTO.setHighSearch(highSearch);
            List<Map<String,Object>> list = jdcService.xxglPageExport(jdcDTO);
            ExportExcelUtils.downLoadExcel("信访件导出","信访件","信访件导出",cols,colNames,list,response);
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(response.SC_NOT_FOUND, e.getMessage());
        }
    }

    @ApiOperation(
            value = "领导控制台 按type查询接待数",
            notes = "type  1：周  2：月  3：年  4：总"
    )
    @GetMapping("/statis/console_{type}")
    public R<Long> statisJDNum(@PathVariable("type") Integer type){
        return success(jdcService.statisJDNum(type));
    }

    @ApiOperation(value = "新领导控制台 查询接待批次、人次")
    @GetMapping("/statis/new_console")
    public R<StatisDTO> statisJDCountAndPeople(@RequestParam("start") String start, @RequestParam("end") String end){
        return success(jdcService.statisJDCountAndPeople(start,end));
    }

    @ApiOperation(
            value = "领导控制台 按type查询重访数",
            notes = "type  1：周  2：月  3：年  4：总"
    )
    @GetMapping("/statis/console_cf_{type}")
    public R<Long> statisCFNum(@PathVariable("type") Integer type){
        return success(jdcService.statisCFNum(type));
    }

    @ApiOperation(value = "新领导控制台 查询纯件批次、人次")
    @GetMapping("/statis/new_console_cj")
    public R<StatisDTO> statisCJCountAndPeople(@RequestParam("start") String start, @RequestParam("end") String end){
        return success(jdcService.statisCJCountAndPeople(start,end));
    }

    @ApiOperation(
            value = "领导控制台 按type查询已处理数",
            notes = "type  1：周  2：月  3：年  4：总"
    )
    @GetMapping("/statis/console_ycl_{type}")
    public R<Long> statisYCLNum(@PathVariable("type") Integer type){
        return success(jdcService.statisYCLNum(type));
    }

    @ApiOperation(value = "新领导控制台 查询已处理批次、人次")
    @GetMapping("/statis/new_console_ycl")
    public R<StatisDTO> statisYCLCountAndPeople(@RequestParam("start") String start, @RequestParam("end") String end){
        return success(jdcService.statisYCLCountAndPeople(start,end));
    }

    @ApiOperation(value = "新领导控制台 查询事项")
    @GetMapping("/statis/new_console_event")
    public R<List<PetitionEventStatisticDTO>> statisEvent(StatisQueryEventDTO dto){
        List<Long> dutyUnitIds = dto.getDutyUnitIds();
        if(dutyUnitIds != null && dutyUnitIds.size() == 1 && dutyUnitIds.get(0) == -1){
            dto.setDutyUnitIds(null);
            dto.setDutyUnitId(-1L);
        }
        return success(petitionEventInfoService.statisticsQueryEventList(dto));
    }

    @ApiOperation(value = "新领导控制台 查询重访事项")
    @GetMapping("/statis/new_console_repeat_event")
    public R<List<PetitionEventStatisticDTO>> statisRepeatEvent(StatisQueryEventDTO dto){
        return success(petitionEventInfoService.statisticsQueryRepeatEventList(dto));
    }

    @ApiOperation(
            value = "领导控制台 按type查询事项饼图",
            notes = "type  1：周  2：月  3：年  4：总"
    )
    @GetMapping("/statis/console_pie_{type}")
    public R<List<DrawPetitionPieVO>> statisPetitionPie(@PathVariable("type") Integer type){
        return success(jdcService.statisPetitionPie(type));
    }

    @ApiOperation(
            value = "领导控制台 按type查询事项柱形图",
            notes = "type  1：周  2：月  3：年  4：总"
    )
    @GetMapping("/statis/console_line_{type}")
    public R<List<DrawPetitionLineVO>> statisPetitionLine(@PathVariable("type") Integer type){
        return success(jdcService.statisPetitionLine(type));
    }

    @ApiOperation(value = "新领导控制台 查询接待处批次、人次")
    @GetMapping("/statis/new_console_reception_org")
    public R<List<StatisDTO>> statisticsQueryReceptionOrg(@RequestParam("start") String start, @RequestParam("end") String end){
        List<StatisDTO> returnList = new ArrayList<>();
        List<StatisDTO> list = petitionEventInfoService.statisticsQueryReceptionOrg(start,end);
        List<Org> orgs = orgService.list(new QueryWrapper<Org>().in("id",12,13,14,15).orderByAsc("id"));
        HashMap<Long,StatisDTO> flag = new HashMap<>();
        if(list != null){
            if(list.size() < 3){
                for(StatisDTO dto:list){
                    flag.put(dto.getId(),dto);
                }
                for(Org o:orgs){
                    if(flag.get(o.getId()) != null){
                        returnList.add(flag.get(o.getId()));
                    }else{
                        returnList.add(new StatisDTO(o.getId(),o.getName(),0,0,0));
                    }
                }
            }else{
                returnList.addAll(list);
            }
        }else{
            for(Org o:orgs){
                returnList.add(new StatisDTO(o.getId(),o.getName(),33.33,0,0));
            }
        }
        return success(returnList);
    }

    @ApiOperation(value = "新领导控制台 查询事发地批次、人次")
    @GetMapping("/statis/new_console_event_place")
    public R<List<StatisDTO>> statisticsQueryEventPlace(@RequestParam("start") String start, @RequestParam("end") String end){
        return success(petitionEventInfoService.statisticsQueryEventPlace(start,end));
    }

    @ApiOperation(value = "新领导控制台 查询责任单位批次、人次")
    @GetMapping("/statis/new_console_duty_unit")
    public R<List<StatisDTO>> statisticsQueryDutyUnit(@RequestParam("start") String start, @RequestParam("end") String end){
        return success(petitionEventInfoService.statisticsQueryDutyUnit(start,end));
    }

    @ApiOperation(value = "新领导控制台 查询市直部门责任单位批次、人次")
    @GetMapping("/statis/new_console_duty_city_unit")
    public R<List<StatisDTO>> statisticsQueryDutyCityUnit(@RequestParam("start") String start, @RequestParam("end") String end){
        return success(petitionEventInfoService.statisticsQueryDutyCityUnit(start,end));
    }

    @ApiOperation(value = "新领导控制台 查询一级内容分类批次、人次")
    @GetMapping("/statis/new_console_content_type_first")
    public R<List<StatisDTO>> statisticsQueryContentTypeFirst(@RequestParam("start") String start, @RequestParam("end") String end){
        return success(petitionEventInfoService.statisticsQueryContentTypeFirst(start,end));
    }

    @ApiOperation(value = "新领导控制台 查询二级内容分类批次、人次")
    @GetMapping("/statis/new_console_content_type_second_{firstId}")
    public R<List<StatisDTO>> statisticsQueryContentTypeSecond(@RequestParam("start") String start, @RequestParam("end") String end,@PathVariable("firstId") Long firstId){
        return success(petitionEventInfoService.statisticsQueryContentTypeSecond(start,end,firstId==0L?null:firstId));
    }

    @GetMapping("/statis/console/jdpie")
    public R<List<DrawPetitionPieVO>> countJDPie(){
        return success(baseService.countJDPie());
    }

    @GetMapping("/statis/console/placepie")
    public R<List<DrawPetitionPieVO>> countJDPlacePie(){
        return success(baseService.countJDPlacePie());
    }

    @GetMapping("/statis/console/unitpie")
    public R<List<DrawPetitionPieVO>> countJDUnitPie(){
        return success(baseService.countJDUnitPie());
    }

    @GetMapping("/statis/console/contentinterval")
    public R<List<DrawPetitionCatIntervalVO>> countContentTypeInterval(){
        return success(baseService.countContentTypeInterval());
    }

    @PostMapping("/xxgl/addProgress/{levelId}/{LeaderName}")
    public R<Boolean> addProgress(@PathVariable("LeaderName") String leaderName,@PathVariable("levelId") Long levelId,JdcDTO jdcDTO) {
        Long loginUserId = getLoginUserId();
        jdcDTO.setReceptionOrgId(userOrgService.getOrgIdByUserId(loginUserId));
        return jdcService.addProgress(leaderName,levelId,jdcDTO);
    }

    @GetMapping("/xxgl/listProgress/{id}")
    public R<List<PetitionEventProgressDTO>> listProgress(@PathVariable("id") Long eventId) {
        R<List<PetitionEventProgressDTO>> listDTO = success(petitionEventProgressService.listProgressByEventId(eventId));
        return listDTO;
    }

    @DeleteMapping("/remove/{id}")
    public R<Boolean> remove(@PathVariable("id") String id) {
        boolean b = deleteCheckService.applyRemoveById(id);
        return success(b);
    }

    @DeleteMapping("/remove_giveup/{id}")
    public R<Boolean> removeGiveUp(@PathVariable("id") String id) {
        PetitionEventInfo eventInfo =  new PetitionEventInfo();
        eventInfo.setId(Long.valueOf(id));
        eventInfo.setStatus(-1);
        return success(petitionEventInfoService.updateById(eventInfo));
    }

    @GetMapping("/djxx/repeatList")
    public R<List<JdcDTO>> getRepeatList(JdcDTO jdcDTO){
        return success(jdcService.getRepeatList(jdcDTO));
    }

    @GetMapping("/djxx/edit_data")
    public R<JdcDTO> getEventEditData(@RequestParam("id") long id){
        return success(petitionEventInfoService.getEventEditData(id));
    }

    @GetMapping("/show/event_info")
    public R<PetitionEventShowPrintDTO> getEventInfoShow(@RequestParam("id") long id){
        return success(baseService.getEventInfoShow(id));
    }

    @GetMapping("/export_excel")
    public void jdcExportExcel(@RequestParam("isUseDateLimit") Boolean isUseDateLimit, @RequestParam("beginDate") String beginDate,
                               @RequestParam("endDate") String endDate, @RequestParam(value = "checkedUnitIdList") List<Long> checkedUnitIdList, HttpServletResponse response) throws Exception {

        /**
         * @Description: 接待处导出excel
         */

        JdcExportExcelDTO jdcExportExcelDTO=new JdcExportExcelDTO();
        jdcExportExcelDTO.setIsUseDateLimit(isUseDateLimit);
        jdcExportExcelDTO.setCheckedUnitIdList(checkedUnitIdList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date begin=null;
        Date end=null;
        if(isUseDateLimit){
            begin=sdf.parse(beginDate);
            end=sdf.parse(endDate);
            long time =end.getTime();
            time = time+24*60*60*1000-1;
            end.setTime(time);
        }
        jdcExportExcelDTO.setBeginDate(begin);
        jdcExportExcelDTO.setEndDate(end);
        Long loginUserId = getLoginUserId();
        Long orgId=userOrgService.getOrgIdByUserId(loginUserId);
        List<File> files=jdcService.jdcExportExcelFileList(orgId,jdcExportExcelDTO);
        String name=orgService.getById(orgId).getName();
        if(isUseDateLimit){
            name+="("+sdf.format(begin)+"至"+sdf.format(end)+")";
        }
        ExportExcelUtils.downLoadFiles(files,ResourceUtils.getURL("classpath:").getPath()+"excel/tempFiles/"+name+".zip",response);
        for(File f:files){
            f.delete();
        }
    }
    @GetMapping("/export_week_report_excel")
    public void jdcExportMyExcel(@RequestParam("Jdc_id") Long Jdc_id, @RequestParam("beginDate") String beginDate,
                               @RequestParam("endDate") String endDate, @RequestParam("filler_name") String filler_name,@RequestParam("remark") String remark,@RequestParam("check") Boolean switch_status, HttpServletResponse response) throws Exception {

        /**
         * @Description: 接待处导出excel
         */

        JdcExportWeekReportExcelDTO jdcExportWeekReportExcelDTO = new JdcExportWeekReportExcelDTO();
        jdcExportWeekReportExcelDTO.setJdc_id(Jdc_id);
        jdcExportWeekReportExcelDTO.setName(filler_name);
        jdcExportWeekReportExcelDTO.setRemark(remark);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date begin=null;
        Date end=null;
        begin=sdf.parse(beginDate);
        end=sdf.parse(endDate);
        long time =end.getTime();
        time = time+24*60*60*1000-1;
        end.setTime(time);
        jdcExportWeekReportExcelDTO.setBeginDate(begin);
        jdcExportWeekReportExcelDTO.setEndDate(end);
        jdcExportWeekReportExcelDTO.setSwitch_status(switch_status);
        Long loginUserId = getLoginUserId();
        Long orgId=userOrgService.getOrgIdByUserId(loginUserId);
        jdcExportWeekReportExcelDTO.setJdc_id(orgId);
        File file=jdcService.jdcExportWeek_Report_ExcelFileList(orgId,jdcExportWeekReportExcelDTO);
        String name=orgService.getById(orgId).getName().toString();
        List<File> files = new ArrayList<>();
        files.add(file);
        name+="("+sdf.format(begin)+"至"+sdf.format(end)+")";
        ExportExcelUtils.downLoadFiles(files,ResourceUtils.getURL("classpath:").getPath()+"excel/tempFiles/"+name+".zip",response);
        for(File f:files){
            f.delete();
        }
    }
    @GetMapping("/export_briefing")
    public R<List<JdcBriefingDTO>> jdcExportBriefing( @RequestParam("beginDate") String beginDate,
                                 @RequestParam("endDate") String endDate,HttpServletResponse response) throws Exception {

        /**
         * @Description: 接待处导出excel
         */
        List<JdcBriefingDTO> briefingDTOS = new ArrayList<>();
        briefingDTOS = jdcService.jdcExportBriefing(beginDate,endDate);
        return success(briefingDTOS);
    }
    @GetMapping("/export_briefing_excel")
    public void jdcExportBriefingExcel(@RequestParam("beginDate") String beginDate, @RequestParam("endDate") String endDate,HttpServletResponse response) throws Exception {

        /**
         * @Description: 接待处导出excel
         */

        JdcBriefingGetDTO jdcBriefingGetDTO = new JdcBriefingGetDTO();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date begin=null;
        Date end=null;
        if(beginDate!=null&&(!beginDate.equals(""))&&(!beginDate.equals("''"))&&endDate!=null&&(!endDate.equals(""))&&(!endDate.equals("''"))){
            begin=sdf.parse(beginDate);
            end=sdf.parse(endDate);
            long time =end.getTime();
            time = time+24*60*60*1000-1;
            end.setTime(time);
        }
        //jdcBriefingGetDTO.setBeginDate(begin);
        //jdcBriefingGetDTO.setEndDate(end);
        //Long loginUserId = getLoginUserId();
        //Long orgId=userOrgService.getOrgIdByUserId(loginUserId);


        /*File file=jdcService.jdcExportBriefing_ExcelFileList(beginDate,endDate);
        String name="信访接待中心简报";
        List<File> files = new ArrayList<>();
        files.add(file);
        if(begin!=null&&end!=null)
            name+="("+sdf.format(begin)+"至"+sdf.format(end)+")";
        ExportExcelUtils.downLoadFiles(files,ResourceUtils.getURL("classpath:").getPath()+"excel/tempFiles/"+name+".zip",response);
        for(File f:files){
            f.delete();
        }*/

        jdcService.exportExcelNew(beginDate,endDate,response);
    }
    @PostMapping("/uploadExcel")
    //@RequestMapping(value="/upload2.do", method = RequestMethod.POST)
    //上传的文件会转换成MultipartFile对象，file名字对应html中上传控件的name
    public R<List<JdcImportExcelErrorListDTO>> upload2(MultipartFile[] file) throws IOException{
        Long loginUserId = getLoginUserId();
        Long orgId=userOrgService.getOrgIdByUserId(loginUserId);
        if(file.length == 0){
            System.out.println( "请选择要上传的文件");
            return null;
        }
        List<JdcImportExcelErrorListDTO> targetList=new ArrayList<>();
        for (MultipartFile multipartFile : file) {
            if(multipartFile.isEmpty()){
                System.out.println("文件上传失败");
            }
            byte[] fileBytes = multipartFile.getBytes();
            String filePath = ResourceUtils.getURL("classpath:").getPath()+"excel/tempImportExcel/"+loginUserId.toString()+"/";
            //String filePath = "src/main/resources/excel/tempImportExcel/"+loginUserId.toString()+"/";
            //取得当前上传文件的文件名称
            String originalFilename = multipartFile.getOriginalFilename();
            //生成文件名
            String fileName =originalFilename;
            FileUtil.uploadFile(fileBytes, filePath, fileName);
            JdcImportExcelErrorListDTO jdcImportExcelErrorListDTO= jdcImportExcelService.jdcImportExcel(orgId,filePath+ originalFilename,originalFilename);
            targetList.add(jdcImportExcelErrorListDTO);
        }
        System.out.println(targetList);
        return success(targetList);
    }
//    @PostMapping("/uploadExcel")
//    //@RequestMapping(value="/upload.do", method = RequestMethod.POST)
//    //上传的文件会转换成MultipartFile对象，file名字对应html中上传控件的name
//    public R<JdcImportExcelErrorListDTO> upload(MultipartFile file) throws IllegalStateException, IOException{
//        Long loginUserId = getLoginUserId();
//        Long orgId=userOrgService.getOrgIdByUserId(loginUserId);
//        //取得当前上传文件的文件名称
//        String originalFilename = file.getOriginalFilename();
//        //transferTo是保存文件，参数就是要保存到的目录和名字
//        String filePath = "D:\\信访项目\\program\\src\\main\\resources\\excel\\tempImportExcel\\"+loginUserId.toString()+"\\";
//        File targetFile = new File(filePath);
//        if(!targetFile.exists()){
//            targetFile.mkdirs();
//        }
//        file.transferTo(new File(filePath + originalFilename));
//        System.out.println("文件类型："+file.getContentType());
//        System.out.println("原文件名："+originalFilename);
//        System.out.println("是否为空："+file.isEmpty());
//        System.out.println("文件大小："+file.getSize());
//        JdcImportExcelErrorListDTO jdcImportExcelErrorListDTO= jdcImportExcelService.jdcImportExcel(orgId,filePath+ originalFilename,originalFilename);
//        return success(jdcImportExcelErrorListDTO);
//    }

    @PostMapping("/djxx/addPeg")
    public R<Boolean> addPep (PetitionEventProgress pep){
        return success(jdcService.addPep(pep));
    }

}
