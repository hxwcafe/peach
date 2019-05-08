package com.example.peach.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.PublicMapper;
import com.example.peach.dao.UserVipMapper;
import com.example.peach.pojo.UserVip;
import com.example.peach.pojo.merge.UAMate;
import com.example.peach.pojo.merge.UAlbum;
import com.example.peach.pojo.pay.TemplateData;
import com.example.peach.service.MateService;
import com.example.peach.service.PublicService;
import com.example.peach.util.HttpRequest;
import com.example.peach.util.PayUtil;
import com.example.peach.util.Signature;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//分享二维码
@Service
public class PublicServiceImpl implements PublicService {
    @Resource
    private PublicMapper publicMapper;
    @Resource
    private UserVipMapper userVipMapper;
    @Resource
    private MateService mateService;


    public ServiceResponse<Void> recordExtension(Integer eid , Integer uid){

        //验证  查询uid eid 是否为空 与 记录表是否已经存在 uid
        if(userVipMapper.selectUvipUser(eid) == null || userVipMapper.selectUvipUser(uid) == null || publicMapper.selectExtensionCountById(uid)>=1){
            throw new RuntimeException("用户id有误!");
        }


        //记录分享记录
        Integer rowCount = publicMapper.insertExtension(eid,uid,1);
        if(rowCount <=0 ){
            throw new RuntimeException("分享记录增加失败");
        }

        //修改分享者钱包金额
        UserVip uv = userVipMapper.selectUvipUser(eid);//查询分享者当前余额
        String m = publicMapper.selectDictionariesByKey("extensionMoney");//查询返现金额
        Double sysMoney =0.0;//返现金额
        try{
            sysMoney = Double.parseDouble(m);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("返现金额转换失败");
        }
        Double money = uv.getUserWallet()+sysMoney;
        uv.setUserWallet(money);
        Integer rowCount2 = userVipMapper.updateUMoneyByUserId(uv);


        return ServiceResponse.createBySuccess("邀请成功");
    }

    @Override
    public void ExcelDown(List<UAlbum> uAlbumList,HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("信息表");

        String fileName = "活动报名表" + ".xls";//设置要导出的文件的名字
        //新增数据行，并且设置单元格数据
        int rowNum = 1;

        String[] headers = {"真实姓名", "性别(0未知 1男，2女)" ,"年龄","身高", "学历","职业", "薪资" , "婚否(0是,1否)","电话号码", "现居地"};
        //headers表示excel表中第一行的表头

        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头

        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //在表中存放查询到的数据放入对应的列
        for (UAlbum uAlbum:uAlbumList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(uAlbum.getNickName());
            row1.createCell(1).setCellValue(uAlbum.getGender());
            row1.createCell(2).setCellValue(uAlbum.getUserage());
            row1.createCell(3).setCellValue(uAlbum.getUserHeight());
            row1.createCell(4).setCellValue(uAlbum.getUserEducation());
            row1.createCell(5).setCellValue(uAlbum.getUserOccupation());
            row1.createCell(6).setCellValue(uAlbum.getUserSalary());
            row1.createCell(7).setCellValue(uAlbum.getIsMarriage());
            row1.createCell(9).setCellValue(uAlbum.getUserphone());
            row1.createCell(10).setCellValue(uAlbum.getUserAddress());
            rowNum++;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        FileOutputStream output=new FileOutputStream("G:/"+fileName);
        workbook.write(output);
        output.flush();
    }

    /**
     * 用户信息的导出
     * @param ider 用户id,
     * @param response excel文件
     */
    @Override
    public void ExcelUser(String ider, HttpServletResponse response){
        String[] id = ider.split(",");

        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("信息表");

            String fileName = "用户" + ".xls";//设置要导出的文件的名字
            //新增数据行，并且设置单元格数据
            int rowNum = 1;

            String[] headers = {"ID","姓名","手机号","性别", "年龄", "身高", "学历", "薪资", "择偶年龄", "择偶身高", "择偶学历", "择偶收入"};
            //headers表示excel表中第一行的表头

            HSSFRow row = sheet.createRow(0);
            //在excel表中添加表头
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
            for(int i=0;i<id.length;i++){
                UAMate uaMate = mateService.selectUAMate(Integer.valueOf(id[i])).getData();
                HSSFRow row1 = sheet.createRow(rowNum);
                exceluserCL(uaMate,row1);
                rowNum++;
            }
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment;downFileName =" + URLEncoder.encode(fileName, "utf-8"));
            response.setContentType("application/vnd.ms-excel");
            workbook.write(output);
            output.flush();
            output.close();
            }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ServiceResponse<String> wxtsUserInfo(String openid, String templateid, String formid, String accesstoken, String page, Map<String, TemplateData> data) {
        StringBuilder url = new StringBuilder("https://api.weixin.qq.com/cgibin/message/wxopen/template/send?access_token=");
        url.append(accesstoken);//设置accesstoken
        JSONObject json = new JSONObject();
        json.put("touser", openid);//设置openid
        json.put("template_id", templateid);//设置模板id
        json.put("form_id", formid);//设置formid
        json.put("data", data);//设置模板消息内容
        json.put("page", page);//跳转微信小程序页面路径（非必须）
        String result = HttpRequest.doPost(url.toString(), json);//请求
        try {
            Map<String, Object> map = PayUtil.doXMLParse(result);
            if (map != null && "ok".equals(map.get("errmsg"))) {
                System.out.println("模版消息发送成功");
                return ServiceResponse.createBySuccess();
            } else {
                //封装一个异常
                StringBuilder sb = new StringBuilder("模版消息发送失败\n");
                sb.append(map.toString());
                throw new Exception(sb.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ServiceResponse.createByError();
    }


    public void exceluserCL(UAMate uaMate,HSSFRow row1){
        row1.createCell(0).setCellValue(uaMate.getId());
        if(uaMate.getUserRealname()!=null) {
            row1.createCell(1).setCellValue(uaMate.getUserRealname());
        }else{
            row1.createCell(1).setCellValue("null");
        }
        if(uaMate.getUserphone()!=null) {
            row1.createCell(2).setCellValue(uaMate.getUserphone());
        }else{
            row1.createCell(2).setCellValue("null");
        }
        row1.createCell(0).setCellValue(uaMate.getId());
        String gender = "null";
        if(uaMate.getGender()!=null) {
            if (uaMate.getGender() == 1) {
                gender = "男";
            } else if (uaMate.getGender() == 2) {
                gender = "女";
            } else {
                gender = "未知";
            }
        }
        row1.createCell(3).setCellValue(gender);
        if(uaMate.getUserage()!=null) {
            row1.createCell(4).setCellValue(uaMate.getUserage());
        }else{
            row1.createCell(4).setCellValue("null");
        }
        if(uaMate.getUserHeight()!=null) {
            row1.createCell(5).setCellValue(uaMate.getUserHeight());
        }else{
            row1.createCell(5).setCellValue("null");
        }
        if(uaMate.getUserEducation()!=null) {
            row1.createCell(6).setCellValue(uaMate.getUserEducation());
        }else{
            row1.createCell(6).setCellValue("null");
        }
        if(uaMate.getUserSalary()!=null) {
            row1.createCell(7).setCellValue(uaMate.getUserSalary());
        }else{
            row1.createCell(7).setCellValue("null");
        }
        if(uaMate.getMate()!=null) {
            if(uaMate.getMate().getMateAge()!=null){
                row1.createCell(8).setCellValue(uaMate.getMate().getMateAge());
            }else{
                row1.createCell(8).setCellValue("null");
            }
            if(uaMate.getMate().getMateHeight()!=null){
                row1.createCell(9).setCellValue(uaMate.getMate().getMateHeight());
            }else{
                row1.createCell(9).setCellValue("null");
            }
            if(uaMate.getMate().getMateEducation()!=null){
                row1.createCell(10).setCellValue(uaMate.getMate().getMateEducation());
            }else{
                row1.createCell(10).setCellValue("null");
            }
            if(uaMate.getMate().getMateSalary()!=null){
                row1.createCell(11).setCellValue(uaMate.getMate().getMateSalary());
            }else{
                row1.createCell(11).setCellValue("null");
            }
        }else{
            row1.createCell(8).setCellValue("null");
            row1.createCell(9).setCellValue("null");
            row1.createCell(10).setCellValue("null");
            row1.createCell(11).setCellValue("null");
        }
    }
}
