package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.dto.DataProvinceQueryDTO;
import com.fangao.dev.sys.entity.DataProvince;
import com.fangao.dev.sys.entity.DataProvinceContentType;
import com.fangao.dev.sys.mapper.DataProvinceMapper;
import com.fangao.dev.sys.service.IDataProvinceService;
import com.fangao.dev.sys.vo.DataProvinceContentTypeVO;
import com.fangao.dev.sys.vo.DataProvinceCountVO;
import com.fangao.dev.sys.vo.DataProvinceSolveVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 实现类
 * </p>
 *
 */
@Service
public class DataProvinceServiceImpl extends ServiceImpl<DataProvinceMapper, DataProvince> implements IDataProvinceService{
    @Override
    public IPage<DataProvince> page(Page page, DataProvince data) {
        return page.setRecords(baseMapper.queryData(page,data));
    }

    @Override
    public List<DataProvinceSolveVO> statisSolve(String start,String end) {
        List<DataProvinceQueryDTO> list =  baseMapper.querySolve(start,end);
        if(CollectionUtils.isEmpty(list)) return null;
        List<DataProvinceSolveVO> returnList = new ArrayList<>();
        HashMap<Long,DataProvinceSolveVO> dataMap = new HashMap<>();
        for(int i=0;i<list.size();i++){
            DataProvinceQueryDTO cur = list.get(i);
            Long id = cur.getId();
            Integer solve = cur.getSolve();
            DataProvinceSolveVO vo = new DataProvinceSolveVO();
            if(dataMap.get(cur.getId()) == null){
                vo.setId(id);
                vo.setName(cur.getName());
                if(solve == 0){
                    vo.setOverDateCount(cur.getCount());
                    vo.setUnSolveCount(cur.getCount());
                } else if(solve == 1) {
                    vo.setInDateCount(cur.getCount());
                    vo.setSolveCount(cur.getCount());
                }
                dataMap.put(id,vo);
            }else{
                vo = dataMap.get(id);
                if(solve == 0){
                    vo.setOverDateCount(cur.getCount());
                    vo.setUnSolveCount(cur.getCount());
                } else if(solve == 1) {
                    vo.setInDateCount(cur.getCount());
                    vo.setSolveCount(cur.getCount());
                }
                dataMap.put(id,vo);
            }
        }
        for(Map.Entry<Long, DataProvinceSolveVO> entry : dataMap.entrySet()){
            DataProvinceSolveVO temp = entry.getValue();
            Integer inCount = temp.getInDateCount();
            Integer overCount = temp.getOverDateCount();
            if(inCount == null){
                temp.setInDateCount(0);
                temp.setSolveCount(0);
            }
            if(overCount == null){
                temp.setOverDateCount(0);
                temp.setUnSolveCount(0);
            }
            if(temp.getInDateCount()==0 && temp.getOverDateCount()>0){
                temp.setDatePercent(0d);
                temp.setSolvePercent(0d);
            }
            BigDecimal b_in = new BigDecimal(temp.getInDateCount());
            BigDecimal b_out = new BigDecimal(temp.getOverDateCount());
            BigDecimal sum = b_in.add(b_out);
            temp.setCount(sum.intValue());
            if(temp.getInDateCount()>0){
                double percent = b_in.divide(sum,4, RoundingMode.HALF_UP).doubleValue();
                temp.setDatePercent(percent);
                temp.setSolvePercent(percent);
            }
            returnList.add(temp);
        }
        return returnList;
    }

    @Override
    public List<DataProvinceCountVO> statisCount(String start, String end) {
        List<DataProvinceQueryDTO> list =  baseMapper.queryCount(start,end);
        if(CollectionUtils.isEmpty(list)) return null;
        List<DataProvinceCountVO> returnList = new ArrayList<>();
        HashMap<Long,DataProvinceCountVO> dataMap = new HashMap<>();
        for(int i=0;i<list.size();i++){
            DataProvinceQueryDTO cur = list.get(i);
            Long id = cur.getId();
            Long formatId = cur.getFormatId();
            DataProvinceCountVO vo = new DataProvinceCountVO();
            if(dataMap.get(cur.getId()) == null){
                vo.setId(id);
                vo.setName(cur.getName());
            }else{
                vo = dataMap.get(id);
            }
            switch(formatId.intValue()){
                case 1: vo.setCountLetter(cur.getCount());vo.setPeopleLetter(cur.getPeople());break;
                case 2: vo.setCountVisit(cur.getCount());vo.setPeopleVisit(cur.getPeople());break;
                case 3: vo.setCountInternet(cur.getCount());vo.setPeopleInternet(cur.getPeople());break;
                case 4: vo.setCountMobile(cur.getCount());vo.setPeopleMobile(cur.getPeople());break;
                case 5: vo.setCountWeChat(cur.getCount());vo.setPeopleWeChat(cur.getPeople());break;
                case 6: vo.setCountAdvise(cur.getCount());vo.setPeopleAdvise(cur.getPeople());break;
                case 7: vo.setCountPhone(cur.getCount());vo.setPeoplePhone(cur.getPeople());break;
                case 8: vo.setCountEmail(cur.getCount());vo.setPeopleEmail(cur.getPeople());break;
                default:break;
            }
            dataMap.put(id,vo);
        }
        for(Map.Entry<Long, DataProvinceCountVO> entry : dataMap.entrySet()){
            DataProvinceCountVO temp = entry.getValue();
            Integer count = temp.getCountLetter()+temp.getCountVisit()+temp.getCountInternet()+temp.getCountMobile()
                    +temp.getCountWeChat()+temp.getCountAdvise()+temp.getCountPhone()+temp.getCountEmail();
            Integer people = temp.getPeopleLetter()+temp.getPeopleVisit()+temp.getPeopleInternet()+temp.getPeopleMobile()
                    +temp.getPeopleWeChat()+temp.getPeopleAdvise()+temp.getPeoplePhone()+temp.getPeopleEmail();
            temp.setCount(count);
            temp.setPeople(people);
            returnList.add(temp);
        }
        return returnList;
    }

    @Override
    public List<DataProvinceContentTypeVO> statisContentType(String start, String end) {
        List<DataProvinceQueryDTO> list =  baseMapper.queryContentType(start,end);
        if(CollectionUtils.isEmpty(list)) return null;
        List<DataProvinceContentTypeVO> returnList = new ArrayList<>();
        HashMap<Long,DataProvinceContentTypeVO> dataMap = new HashMap<>();
        for(int i=0;i<list.size();i++){
            DataProvinceQueryDTO cur = list.get(i);
            Long id = cur.getId();
            Long contentTypeId = cur.getContentTypeId();
            DataProvinceContentTypeVO vo = new DataProvinceContentTypeVO();
            if(dataMap.get(cur.getId()) == null){
                vo.setId(id);
                vo.setName(cur.getName());
            }else{
                vo = dataMap.get(id);
            }
            switch(contentTypeId.intValue()){
                case 1: vo.setOne(cur.getCount());break;
                case 2: vo.setTwo(cur.getCount());break;
                case 3: vo.setThree(cur.getCount());break;
                case 4: vo.setFour(cur.getCount());break;
                case 5: vo.setFive(cur.getCount());break;
                case 6: vo.setSix(cur.getCount());break;
                case 7: vo.setSeven(cur.getCount());break;
                case 8: vo.setEight(cur.getCount());break;
                case 9: vo.setNine(cur.getCount());break;
                case 10: vo.setTen(cur.getCount());break;
                case 11: vo.setEleven(cur.getCount());break;
                case 12: vo.setTwelve(cur.getCount());break;
                case 13: vo.setThirteen(cur.getCount());break;
                case 14: vo.setFourteen(cur.getCount());break;
                case 15: vo.setFifteen(cur.getCount());break;
                case 16: vo.setSixteen(cur.getCount());break;
                case 17: vo.setSeventeen(cur.getCount());break;
                default:break;
            }
            dataMap.put(id,vo);
        }
        for(Map.Entry<Long, DataProvinceContentTypeVO> entry : dataMap.entrySet()){
            returnList.add(entry.getValue());
        }
        return returnList;
    }
}
