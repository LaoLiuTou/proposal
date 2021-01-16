package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fangao.dev.sys.dto.JdcImportExcelDTO;
import com.fangao.dev.sys.dto.JdcImportExcelErrorDTO;
import com.fangao.dev.sys.dto.JdcImportExcelErrorListDTO;
import com.fangao.dev.sys.entity.DictPetitionLeaderLevel;
import com.fangao.dev.sys.entity.PetitionEventInfo;
import com.fangao.dev.sys.service.IDictPetitionLeaderLevelService;
import com.fangao.dev.sys.service.IPetitionEventInfoService;
import com.fangao.dev.sys.service.JdcImportExcelService;
import com.fangao.dev.sys.service.JdcService;
import com.fangao.dev.sys.utils.Excel_reader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JdcImportExcelServiceImpl implements JdcImportExcelService {
    @Autowired
    private IPetitionEventInfoService petitionEventInfoService;
    @Autowired
    private IDictPetitionLeaderLevelService dictPetitionLeaderLevelService;
    @Autowired
    private JdcService jdcService;
    @Override
    public JdcImportExcelErrorListDTO jdcImportExcel(long orgId, String path,String fileName) throws IOException {
        String filePath = path;
        Excel_reader test= new Excel_reader();
        ArrayList<ArrayList<String>> arr=test.xlsx_reader(filePath,0,5,6,7,8,9,10,11,12,13,14,15);  //后面的参数代表需要输出哪些列，参数个数可以任意
        List<JdcImportExcelDTO> jdcImportExcelDTOList=new ArrayList<>();
        for(int i=1;i<arr.size();i++){
            ArrayList<String> row=arr.get(i);
            JdcImportExcelDTO a=new JdcImportExcelDTO();
            a.setEventNum(row.get(0));
            a.setCrux(row.get(1));
            a.setProgressContent(row.get(2));
            a.setIsLeaderHadText(row.get(3));
            a.setDictPetitionLeaderLevelText(row.get(4));
            a.setLeaderIdText(row.get(5));
            a.setIsVerifiedText(row.get(6));
            a.setVerifyDetail(row.get(7));
            a.setIsReasonableText(row.get(8));
            a.setUnreasonableReason(row.get(9));
            a.setIsFinalOpinionText(row.get(10));
            a.setFinalOpinion(row.get(11));
            if(!row.get(0).equals("") || !row.get(1).equals("") || !row.get(2).equals("") || !row.get(3).equals("") ||
                    !row.get(4).equals("") || !row.get(5).equals("") || !row.get(6).equals("") || !row.get(7).equals("") ||
                    !row.get(8).equals("") || !row.get(9).equals("") || !row.get(10).equals("") || !row.get(11).equals(""))
            jdcImportExcelDTOList.add(a);
//            for(int j=0;j<row.size();j++){
//                System.out.print(row.get(j)+" ");
//            }
//            System.out.println("");
        }
        int totalCount = jdcImportExcelDTOList.size();
//		System.out.println(jdcImportExcelDTOList.toString());
        JdcImportExcelErrorListDTO jdcImportExcelErrorListDTO=judgeAll(fileName,jdcImportExcelDTOList,orgId);
        jdcImportExcelErrorListDTO.setTotalEventNums(totalCount);
//        System.out.println(jdcImportExcelErrorListDTO);
        return jdcImportExcelErrorListDTO;
    }
    //判断所有的字段是否符合规范
    public JdcImportExcelErrorListDTO judgeAll(String fileName, List<JdcImportExcelDTO> jdcImportExcelDTOList, Long orgId){
        JdcImportExcelErrorListDTO jdcImportExcelErrorListDTO=new JdcImportExcelErrorListDTO();
        Integer errorEventNums=0;
        Integer successEventNums=0;
        Integer lineNumber=1;
        jdcImportExcelErrorListDTO.setErrorFileName(fileName);
        List<JdcImportExcelErrorDTO> jdcImportExcelErrorDTOList=new ArrayList<>();
        for(JdcImportExcelDTO jdcImportExcelDTO :jdcImportExcelDTOList){
            int errorFlag=0;
            lineNumber++;
            String eventNumErrorReason =judgeEventNum(jdcImportExcelDTO.getEventNum(),orgId);
            if(!eventNumErrorReason.equals("0")){
                JdcImportExcelErrorDTO a1=new JdcImportExcelErrorDTO();
                a1.setEventNum(jdcImportExcelDTO.getEventNum());
                a1.setErrorLineNumber(lineNumber);
                a1.setTableHeadText("事件编号");
                a1.setErrorContent(jdcImportExcelDTO.getEventNum());
                a1.setReason(eventNumErrorReason);
                jdcImportExcelErrorDTOList.add(a1);
                errorFlag++;
                continue;
            };
            String newestProgressErrorReason =judgeNewestProgress(jdcImportExcelDTO.getProgressContent());
            if(!newestProgressErrorReason.equals("0")){
                JdcImportExcelErrorDTO a2=new JdcImportExcelErrorDTO();
                a2.setEventNum(jdcImportExcelDTO.getEventNum());
                a2.setErrorLineNumber(lineNumber);
                a2.setTableHeadText("进展内容");
                a2.setErrorContent(jdcImportExcelDTO.getProgressContent());
                a2.setReason(newestProgressErrorReason);
                jdcImportExcelErrorDTOList.add(a2);
                errorFlag++;
            }
            String isLeaderHadErrorReason =judegIsOrNot(convert(jdcImportExcelDTO.getIsLeaderHadText()));
            if(!isLeaderHadErrorReason.equals("0")){
                JdcImportExcelErrorDTO a3=new JdcImportExcelErrorDTO();
                a3.setEventNum(jdcImportExcelDTO.getEventNum());
                a3.setErrorLineNumber(lineNumber);
                a3.setTableHeadText("是否领导包案");
                a3.setErrorContent(jdcImportExcelDTO.getIsLeaderHadText());
                a3.setReason(isLeaderHadErrorReason);
                jdcImportExcelErrorDTOList.add(a3);
                errorFlag++;
            }else{
                jdcImportExcelDTO.setIsLeaderHad(convert(jdcImportExcelDTO.getIsLeaderHadText()));
            }
            String dictPetitionLeaderLevelErrorReason =judgeDictPetitionLeaderLevel(jdcImportExcelDTO.getDictPetitionLeaderLevelText(),convert(jdcImportExcelDTO.getIsLeaderHadText()));
            if(!dictPetitionLeaderLevelErrorReason.equals("0")){
                JdcImportExcelErrorDTO a4=new JdcImportExcelErrorDTO();
                a4.setEventNum(jdcImportExcelDTO.getEventNum());
                a4.setErrorLineNumber(lineNumber);
                a4.setTableHeadText("领导层级");
                a4.setErrorContent(jdcImportExcelDTO.getDictPetitionLeaderLevelText());
                a4.setReason(dictPetitionLeaderLevelErrorReason);
                jdcImportExcelErrorDTOList.add(a4);
                errorFlag++;
            }else{
                if(convert(jdcImportExcelDTO.getIsLeaderHadText())==1){
                    DictPetitionLeaderLevel dictPetitionLeaderLevel= dictPetitionLeaderLevelService.getOne(new QueryWrapper<DictPetitionLeaderLevel>().eq("name", jdcImportExcelDTO.getDictPetitionLeaderLevelText().trim())
                            .eq("deleted", 0));
                    jdcImportExcelDTO.setDictPetitionLeaderLevelId(dictPetitionLeaderLevel.getId());
                }else{
                    jdcImportExcelDTO.setLeaderIdText("");
                }


            };
            String leaderIdTextErrorReason =judgetLeaderIdText(jdcImportExcelDTO.getLeaderIdText(),convert(jdcImportExcelDTO.getIsLeaderHadText()));
            if(!leaderIdTextErrorReason.equals("0")){
                JdcImportExcelErrorDTO a5=new JdcImportExcelErrorDTO();
                a5.setEventNum(jdcImportExcelDTO.getEventNum());
                a5.setErrorLineNumber(lineNumber);
                a5.setTableHeadText("领导姓名");
                a5.setErrorContent(jdcImportExcelDTO.getLeaderIdText());
                a5.setReason(leaderIdTextErrorReason);
                jdcImportExcelErrorDTOList.add(a5);
                errorFlag++;
            };
            String isVerifiedTextErrorReason =judegIsOrNot(convert(jdcImportExcelDTO.getIsVerifiedText()));
            if(!isVerifiedTextErrorReason.equals("0")){
                JdcImportExcelErrorDTO a6=new JdcImportExcelErrorDTO();
                a6.setEventNum(jdcImportExcelDTO.getEventNum());
                a6.setErrorLineNumber(lineNumber);
                a6.setTableHeadText("是否属实");
                a6.setErrorContent(jdcImportExcelDTO.getIsVerifiedText());
                a6.setReason(isVerifiedTextErrorReason);
                jdcImportExcelErrorDTOList.add(a6);
                errorFlag++;
            }else{
                jdcImportExcelDTO.setIsVerified(convert(jdcImportExcelDTO.getIsVerifiedText()));
            };
            String verifyDetailErrorReason =judgetVerifyDetail(jdcImportExcelDTO.getVerifyDetail(),convert(jdcImportExcelDTO.getIsVerifiedText()));
            if(!verifyDetailErrorReason.equals("0")){
                JdcImportExcelErrorDTO a7=new JdcImportExcelErrorDTO();
                a7.setEventNum(jdcImportExcelDTO.getEventNum());
                a7.setErrorLineNumber(lineNumber);
                a7.setTableHeadText("调查核实情况");
                a7.setErrorContent(jdcImportExcelDTO.getVerifyDetail());
                a7.setReason(verifyDetailErrorReason);
                jdcImportExcelErrorDTOList.add(a7);
                errorFlag++;
            };


            String isReasonableTextErrorReason =judegIsOrNot(convertReasonableText(jdcImportExcelDTO.getIsReasonableText()));
            if(!isReasonableTextErrorReason.equals("0")){
                JdcImportExcelErrorDTO a8=new JdcImportExcelErrorDTO();
                a8.setEventNum(jdcImportExcelDTO.getEventNum());
                a8.setErrorLineNumber(lineNumber);
                a8.setTableHeadText("是否有理");
                a8.setErrorContent(jdcImportExcelDTO.getIsReasonableText());
                a8.setReason(isReasonableTextErrorReason);
                jdcImportExcelErrorDTOList.add(a8);
                errorFlag++;
            }else{
                jdcImportExcelDTO.setIsReasonable(convertReasonableText(jdcImportExcelDTO.getIsReasonableText()));
            };
            String unreasonableReasonErrorReason =judgeUnreasonableReason(jdcImportExcelDTO.getUnreasonableReason(),convertReasonableText(jdcImportExcelDTO.getIsReasonableText()));
            if(!unreasonableReasonErrorReason.equals("0")){
                JdcImportExcelErrorDTO a9=new JdcImportExcelErrorDTO();
                a9.setEventNum(jdcImportExcelDTO.getEventNum());
                a9.setErrorLineNumber(lineNumber);
                a9.setTableHeadText("理由");
                a9.setErrorContent(jdcImportExcelDTO.getUnreasonableReason());
                a9.setReason(unreasonableReasonErrorReason);
                jdcImportExcelErrorDTOList.add(a9);
                errorFlag++;
            };


            String isFinalOpinionTextErrorReason =judegIsOrNot(convert(jdcImportExcelDTO.getIsFinalOpinionText()));
            if(!isFinalOpinionTextErrorReason.equals("0")){
                JdcImportExcelErrorDTO a10=new JdcImportExcelErrorDTO();
                a10.setEventNum(jdcImportExcelDTO.getEventNum());
                a10.setErrorLineNumber(lineNumber);
                a10.setTableHeadText("是否最终意见");
                a10.setErrorContent(jdcImportExcelDTO.getIsFinalOpinionText());
                a10.setReason(isFinalOpinionTextErrorReason);
                jdcImportExcelErrorDTOList.add(a10);
                errorFlag++;
            }else{
                jdcImportExcelDTO.setIsFinalOpinion(convert(jdcImportExcelDTO.getIsFinalOpinionText()));
            };
            String finalOpinionErrorReason =judgeFinalOpinion(jdcImportExcelDTO.getFinalOpinion(),convert(jdcImportExcelDTO.getIsFinalOpinionText()));
            if(!finalOpinionErrorReason.equals("0")){
                JdcImportExcelErrorDTO a11=new JdcImportExcelErrorDTO();
                a11.setEventNum(jdcImportExcelDTO.getEventNum());
                a11.setErrorLineNumber(lineNumber);
                a11.setTableHeadText("最终意见");
                a11.setErrorContent(jdcImportExcelDTO.getFinalOpinion());
                a11.setReason(finalOpinionErrorReason);
                jdcImportExcelErrorDTOList.add(a11);
                errorFlag++;
            };
            if(errorFlag==0){
                successEventNums++;
            }

        }
        if(jdcImportExcelDTOList.size()==successEventNums){
            for(JdcImportExcelDTO jdcImportExcelDTO :jdcImportExcelDTOList){
//                System.out.println(jdcImportExcelDTO);
                jdcImportExcelDTO.setReceptionOrgId(orgId);
                jdcService.addProgress(jdcImportExcelDTO.getLeaderIdText(),jdcImportExcelDTO.getDictPetitionLeaderLevelId(),jdcImportExcelDTO);
            }
        }
        jdcImportExcelErrorListDTO.setErrorList(jdcImportExcelErrorDTOList);
        jdcImportExcelErrorListDTO.setErrorNums(jdcImportExcelErrorDTOList.size());
        jdcImportExcelErrorListDTO.setSuccessEventNums(successEventNums);
        jdcImportExcelErrorListDTO.setErrorEventNums(jdcImportExcelDTOList.size()-successEventNums);
        return jdcImportExcelErrorListDTO;
    }
    //判断事件编号是否合规
    public String judgeEventNum(String eventNum,Long orgId){
        PetitionEventInfo petitionEventInfo=petitionEventInfoService.getOne(new QueryWrapper<PetitionEventInfo>().eq("event_num", eventNum)
                .eq("deleted", 0));
        if(petitionEventInfo==null){
            return "事件编号不存在，或事件被删除";
        }else if(petitionEventInfo.getReceptionOrgId()!=orgId){
            return "当前账号所属机构，没有权限处理该事件";
        }else if(petitionEventInfo.getStatus()==-1){
            return "当前事件处于“放弃处理”状态，暂不能操作";
        }else if(petitionEventInfo.getStatus()==0){
            return "当前事件处于“待处理”状态，暂不能操作";
        }else if(petitionEventInfo.getStatus()==2){
            return "当前事件处于“处理完成”状态，暂不能操作";
        }else if(petitionEventInfo.getIsFinalOpinion()!=null){
            if(petitionEventInfo.getIsFinalOpinion().equals(1)) return "事件已有最终意见";
        }

        return "0";
    }
    //判断进展内容是否为空
    public String judgeNewestProgress(String newestProgress){
        if(StringUtils.isEmpty(newestProgress)){return "未输入进展内容";};
        return "0";
    }
    //判断输入内容只能是  ‘是’或者‘否’
    public String judegIsOrNot(Integer flag){
        if(flag==-1){return "只能填‘是’或者‘否’";}
        else return "0";
    }
    //判断领导层级是否正确
    public String judgeDictPetitionLeaderLevel(String leaderLevel,Integer isLeaderHad){
        if(isLeaderHad==1){
            if(StringUtils.isEmpty(leaderLevel)){return "是领导包案，应填写领导层级";}
            else{
                DictPetitionLeaderLevel dictPetitionLeaderLevel= dictPetitionLeaderLevelService.getOne(new QueryWrapper<DictPetitionLeaderLevel>().eq("name", leaderLevel.trim())
                        .eq("deleted", 0));
                if(dictPetitionLeaderLevel==null){return "没有对应的领导层级，请正确填写";}
                else{ return "0";}
            }
        }else{
            if(!StringUtils.isEmpty(leaderLevel)){return " 领导包案为否，则不应填写领导层级";}
            else{ return "0";}
        }

    }
    //判断领导姓名是否正确
    public String judgetLeaderIdText(String leaderIdText,Integer isLeaderHad){
        if(isLeaderHad==1){
            if(StringUtils.isEmpty(leaderIdText)){return "是领导包案，应填写领导姓名";}
            else{
                return "0";
            }
        }else{
            if(!StringUtils.isEmpty(leaderIdText)){return " 领导包案为否，则不应填写领导姓名";}
            else{ return "0";}
        }

    }
    //判断核实情况是否填写
    public String judgetVerifyDetail(String verifyDetail,Integer isVerified){
        if(isVerified==0){
            if(StringUtils.isEmpty(verifyDetail)){return "不属实，则应填写调查核实情况";}
            else{
                return "0";
            }
        }
        return "0";
    }
    //判断 部分合理或不合理是否填写
    public String judgeUnreasonableReason(String unreasonableReason,Integer isReasonable){
        if(isReasonable==1||isReasonable==2){
            if(StringUtils.isEmpty(unreasonableReason)){return "部分合理或不合理，应填写理由";}
            else{
                return "0";
            }
        }else{
            if(!StringUtils.isEmpty(unreasonableReason)){return " 合理，则不应填写理由";}
            else{ return "0";}
        }
    }
    //判断最终结论填写
    public String judgeFinalOpinion(String finalOpinion,Integer isFinalOpinion){
        if(isFinalOpinion==1){
            if(StringUtils.isEmpty(finalOpinion)){return "最终意见为‘是’，则应填写最终意见";}
            else{
                return "0";
            }
        }else{
            if(!StringUtils.isEmpty(finalOpinion)){return  "最终意见为‘否’，则不应填写最终意见";}
            else{ return "0";}
        }
    }
    public Integer convert(String flag){
        if(StringUtils.isEmpty(flag))return 0;
        switch (flag.trim()){
            case "是":
                return 1;
            case "否":
                return 0;

        }
        return -1;
    }
    public Integer convertReasonableText(String flag){
        switch (flag.trim()){
            case "是":
                return 0;
            case "部分":
                return 1;
            case "否":
                return 2;
        }
        return -1;
    }
}
