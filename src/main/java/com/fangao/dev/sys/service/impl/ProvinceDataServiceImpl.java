package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fangao.dev.sys.entity.*;
import com.fangao.dev.sys.service.*;
import com.fangao.dev.sys.utils.Excel_reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ProvinceDataServiceImpl implements IProvinceDataService {

    @Autowired
    private IDataProvinceService dataProvinceService;
    @Autowired
    private IDataProvinceContentTypeService dataProvinceContentTypeService;
    @Autowired
    private IDataProvinceEventPlaceService dataProvinceEventPlaceService;
    @Autowired
    private IDataProvinceFormatService dataProvinceFormatService;
    @Autowired
    private IDataProvincePurposeService dataProvincePurposeService;
    @Autowired
    private IDataProvinceOrgService dataProvinceOrgService;
    @Autowired
    private IDataProvinceOperatorService dataProvinceOperatorService;
    @Autowired
    private IDataProvinceStatusService dataProvinceStatusService;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean importExcel(String path, String fileName) throws IOException,ParseException {
        ArrayList<ArrayList<String>> arr = new Excel_reader().xlsx_reader(path,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25);

        if(CollectionUtils.isEmpty(arr)) return false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String,Integer> indexMap = new HashMap<>();
        List<DataProvince> dataList = new ArrayList<>();
        HashMap<String,Long> formatMap = new HashMap<>();
        formatMap.put("来信",1L);
        formatMap.put("来访",2L);
        formatMap.put("网上信访",3L);
        formatMap.put("手机信访",4L);
        formatMap.put("微信信访",5L);
        formatMap.put("网上建议",6L);
        formatMap.put("电话信访",7L);
        formatMap.put("领导信箱",8L);

        HashMap<String,Long> purposeMap = new HashMap<>();
        purposeMap.put("求决",1L);
        purposeMap.put("意见建议",2L);
        purposeMap.put("申诉",3L);
        purposeMap.put("揭发控告",4L);
        purposeMap.put("其他",5L);

        HashMap<String,Long> statusMap = new HashMap<>();
        statusMap.put("受理申请审查中",1L);
        statusMap.put("不予受理",2L);
        statusMap.put("受理",3L);
        statusMap.put("已作出处理意见",4L);
        statusMap.put("超期未作出处理意见",5L);
        statusMap.put("不再受理",6L);
        statusMap.put("超期未申请复查",7L);
        statusMap.put("复查",8L);
        statusMap.put("提出复查意见",9L);
        statusMap.put("超期未作出复查意见",10L);
        statusMap.put("超期未申请复核",11L);
        statusMap.put("复核",12L);
        statusMap.put("提出复核意见",13L);
        statusMap.put("超期未作出复核意见",14L);
        statusMap.put("审核认定办结",15L);

        for(int i=0;i<arr.size();i++){
            ArrayList<String> row = arr.get(i);
            if(CollectionUtils.isEmpty(row)) continue;
            if(i==0){
                for(int j=0;j<row.size();j++){
                    switch(row.get(j)){
                        case "信访件编号": indexMap.put("code",j);break;
                        case "姓名": indexMap.put("name",j);break;
                        case "信访人数": indexMap.put("number",j);break;
                        case "住址": indexMap.put("address",j);break;
                        case "手机号码": indexMap.put("mobile",j);break;
                        case "问题属地": indexMap.put("eventPlace",j);break;
                        case "信访目的": indexMap.put("purpose",j);break;
                        case "内容分类": indexMap.put("contentType",j);break;
                        case "信访日期": indexMap.put("visitDate",j);break;
                        case "登记日期": indexMap.put("checkDate",j);break;
                        case "限办时间": indexMap.put("deadlineDate",j);break;
                        case "信访形式": indexMap.put("format",j);break;
                        case "概况": indexMap.put("description",j);break;
                        case "是否办结": indexMap.put("solve",j);break;
                        case "是否全国初次信访": indexMap.put("firstTime",j);break;
                        case "联名信或集体访": indexMap.put("isGroup",j);break;
                        case "信访件状态": indexMap.put("status",j);break;
                        case "登记部门": indexMap.put("org",j);break;
                        case "登记人": indexMap.put("operator",j);break;
                        default:break;
                    }
                }
            }
            if(i > 0){
                DataProvince dataProvince = new DataProvince();
                dataProvince.setCode(row.get(indexMap.get("code")));
                dataProvince.setName(row.get(indexMap.get("name")));
                dataProvince.setNumber(Integer.valueOf(row.get(indexMap.get("number"))));
                dataProvince.setAddress(row.get(indexMap.get("address")));
                dataProvince.setMobile(row.get(indexMap.get("mobile")));
                dataProvince.setEventPlaceId(insertEventPlace(row.get(indexMap.get("eventPlace"))));
                /*dataProvince.setPurposeId(insertPurpose(row.get(indexMap.get("purpose"))));*/
                dataProvince.setPurposeId(purposeMap.get(row.get(indexMap.get("purpose"))));
                dataProvince.setContentTypeId(insertContentType(row.get(indexMap.get("contentType"))));
                dataProvince.setVisitDate(sdf.parse(row.get(indexMap.get("visitDate"))));
                dataProvince.setCheckDate(sdf.parse(row.get(indexMap.get("checkDate"))));
                dataProvince.setDeadlineDate(sdf.parse(row.get(indexMap.get("deadlineDate"))));
                /*dataProvince.setFormatId(insertFormat(row.get(indexMap.get("format"))));*/
                dataProvince.setFormatId(formatMap.get(row.get(indexMap.get("format"))));
                dataProvince.setDescription(row.get(indexMap.get("description")));
                dataProvince.setSolve(row.get(indexMap.get("solve")).equals("是")?1:0);
                dataProvince.setFirstTime(row.get(indexMap.get("firstTime")).equals("是")?1:0);
                dataProvince.setIsGroup(row.get(indexMap.get("isGroup")).equals("是")?1:0);
                /*dataProvince.setStatusId(insertStatus(row.get(indexMap.get("status"))));*/
                dataProvince.setStatusId(statusMap.get(row.get(indexMap.get("status"))));
                dataProvince.setOrgId(insertOrg(row.get(indexMap.get("org"))));
                dataProvince.setOperatorId(insertOperator(row.get(indexMap.get("operator"))));
                dataList.add(dataProvince);
            }
        }
        return dataProvinceService.saveOrUpdateBatch(dataList);
    }

    private Long insertEventPlace(String place){
        Long returnId = null;

        String [] first = new String[2];
        String [] second = new String[2];
        String [] third = new String[2];

        DataProvinceEventPlace province = null;
        DataProvinceEventPlace city = null;
        DataProvinceEventPlace area = null;
        DataProvinceEventPlace address = null;

        if(StringUtils.isEmpty(place)) return null;

        // 省插入
        first = place.split("省");
        province = insertEventPlaceInner(first[0],"省",1,0L,0L,"");

        if(province == null){return null;}
        else if(first.length==1 || StringUtils.isEmpty(first[1])){returnId = province.getId();}
        else{
            // 市插入
            second = first[1].split("市");
            city = insertEventPlaceInner(second[0],first[1].contains("市")?"市":"",2,province.getId(),province.getId(),first[0]+"省");

            if(city == null){return null;}
            else if(second.length==1 || StringUtils.isEmpty(second[1])){returnId = city.getId();}
            else{
                // 区插入
                third = second[1].split("区");
                area = insertEventPlaceInner(third[0],second[1].contains("区")?"区":"",3,city.getId(),city.getRid(),first[0]+"省"+second[0]+"市");

                if(area == null){return null;}
                else if(third.length==1 || StringUtils.isEmpty(third[1])){returnId = area.getId();}
                else{
                    // 地址插入
                    address = insertEventPlaceInner(third[1],"",4,area.getId(),area.getRid(),first[0]+"省"+second[0]+"市"+third[0]+"区");

                    if(address == null){return null;}
                    else {returnId = address.getId();}
                }
            }
        }


        return returnId;
    }

    private DataProvinceEventPlace insertEventPlaceInner(String name,String unit,Integer level,Long pid,Long rid,String prevName){
        DataProvinceEventPlace new_place = new DataProvinceEventPlace();
        if(StringUtils.isEmpty(name)) return null;
        DataProvinceEventPlace has_province = dataProvinceEventPlaceService.getOne(new QueryWrapper<DataProvinceEventPlace>().eq("name",name+unit));
        if(has_province!=null){new_place=has_province;}
        else{
            new_place.setPid(level==1?0L:pid);
            if(level != 1) new_place.setRid(rid);
            new_place.setLevel(level);
            new_place.setName(name+unit);
            new_place.setShowName(prevName+name+unit);
            boolean flag = dataProvinceEventPlaceService.save(new_place);
            if(flag) {
                if(level==1){
                    new_place.setRid(new_place.getId());
                    dataProvinceEventPlaceService.updateById(new_place);
                }
            }else{
                return null;
            }
        }
        return new_place;
    }

    private Long insertContentType(String content){
        Long returnId = null;

        if(StringUtils.isEmpty(content)) return null;

        String [] str = content.split("_");
        String showName = "";
        Long pid = 0L;
        Long rid = 0L;
        for(int i=0;i<str.length;i++){
            showName += ('_'+str[i]);
            DataProvinceContentType new_data = new DataProvinceContentType();
            DataProvinceContentType has_data = dataProvinceContentTypeService.getOne(new QueryWrapper<DataProvinceContentType>().eq("name",str[i]));
            if(has_data != null){new_data = has_data;}
            else{
                new_data.setPid(pid);
                new_data.setLevel(i+1);
                new_data.setName(str[i]);
                new_data.setShowName(showName.substring(1,showName.length()));
                if(i > 0) new_data.setRid(rid);
                boolean flag = dataProvinceContentTypeService.save(new_data);
                if(flag) {
                    if(i == 0) {
                        new_data.setRid(new_data.getId());
                        dataProvinceContentTypeService.updateById(new_data);
                    }
                } else {
                    return null;
                }
            }
            pid = new_data.getId();
            if(i == 0) rid = new_data.getId();
            if(i == str.length-1) returnId = new_data.getId();
        }
        return returnId;
    }

    private Long insertPurpose(String purpose){
        if(StringUtils.isEmpty(purpose)) return null;
        DataProvincePurpose new_data = new DataProvincePurpose();
        DataProvincePurpose has_data = dataProvincePurposeService.getOne(new QueryWrapper<DataProvincePurpose>().eq("name",purpose));
        if(has_data != null) {
            new_data = has_data;
        } else{
            new_data.setName(purpose);
            dataProvincePurposeService.save(new_data);
        }
        return new_data.getId();
    }

    private Long insertFormat(String format){
        if(StringUtils.isEmpty(format)) return null;
        DataProvinceFormat new_data = new DataProvinceFormat();
        DataProvinceFormat has_data = dataProvinceFormatService.getOne(new QueryWrapper<DataProvinceFormat>().eq("name",format));
        if(has_data != null) {
            new_data = has_data;
        } else{
            new_data.setName(format);
            dataProvinceFormatService.save(new_data);
        }
        return new_data.getId();
    }

    private Long insertStatus(String status){
        if(StringUtils.isEmpty(status)) return null;
        DataProvinceStatus new_data = new DataProvinceStatus();
        DataProvinceStatus has_data = dataProvinceStatusService.getOne(new QueryWrapper<DataProvinceStatus>().eq("name",status));
        if(has_data != null) {
            new_data = has_data;
        } else{
            new_data.setName(status);
            dataProvinceStatusService.save(new_data);
        }
        return new_data.getId();
    }

    private Long insertOrg(String org){
        Long returnId = null;

        if(StringUtils.isEmpty(org)) return null;

        String [] str = org.split("/");
        String showName = "";
        Long pid = 0L;
        Long rid = 0L;
        if(str.length > 0) {
            str = orgReverse(str);
        }else{
            return null;
        }
        for(int i=0;i<str.length;i++){
            showName += ('/'+str[i]);
            DataProvinceOrg new_data = new DataProvinceOrg();
            DataProvinceOrg has_data = dataProvinceOrgService.getOne(new QueryWrapper<DataProvinceOrg>().eq("name",str[i]));
            if(has_data != null){new_data = has_data;}
            else{
                new_data.setPid(pid);
                new_data.setLevel(i+1);
                new_data.setName(str[i]);
                new_data.setShowName(showName.substring(1,showName.length()));
                if(i > 0) new_data.setRid(rid);
                boolean flag = dataProvinceOrgService.save(new_data);
                if(flag) {
                    if(i == 0) {
                        new_data.setRid(new_data.getId());
                        dataProvinceOrgService.updateById(new_data);
                    }
                } else {
                    return null;
                }
            }
            pid = new_data.getId();
            if(i == 0) rid = new_data.getId();
            if(i == str.length-1) returnId = new_data.getId();
        }
        return returnId;
    }

    private String [] orgReverse(String [] originArray) {
        int length = originArray.length;
        String [] reverseArray = new String[length];
        for (int i = 0; i < length; i++) {
            reverseArray[i] = originArray[length - i - 1];
        }
        return reverseArray;
    }

    private Long insertOperator(String operator){
        if(StringUtils.isEmpty(operator)) return null;
        DataProvinceOperator new_data = new DataProvinceOperator();
        DataProvinceOperator has_data = dataProvinceOperatorService.getOne(new QueryWrapper<DataProvinceOperator>().eq("name",operator));
        if(has_data != null) {
            new_data = has_data;
        } else{
            new_data.setName(operator);
            dataProvinceOperatorService.save(new_data);
        }
        return new_data.getId();
    }


}
