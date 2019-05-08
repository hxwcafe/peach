package com.example.peach.controller;


import com.example.peach.pojo.User;
import com.example.peach.service.ExcelService;
import com.example.peach.service.UserService;
import com.example.peach.util.ExcelImportUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 输入导入导出
 * Created by Administrator on 2018/11/27.
 */
@RestController
@RequestMapping(value = "/User")
public class ExcelController {

    @Resource
    private ExcelService excelService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/ExcelDown")
    public void downloadAllUser(HttpServletResponse response) throws IOException{

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("信息表");

        List<User> list = userService.userList();
//        for(int i=0;i<list.size();i++){
//            System.out.println(list.get(i).getUserNickname());
//        }
//        System.out.println(list.size());

        String fileName = "用户信息表" + ".xls";//设置要导出的文件的名字
        //新增数据行，并且设置单元格数据

        int rowNum = 1;

        String[] headers = {"用户ID", "昵称", "真实姓名", "性别(0未知 1男，2女)", "电话号码", "生日", "现居地", "签名", "职业",
                "婚否(0是,1否)", "身高", "学历", "年薪", "兴趣爱好", "会员等级", "身份证", "头像", "积分", "实名验证(0是,1否)",
                "微信用户唯一标识", "籍贯", "微信用户通行证", "省", "市", "国家"};
        //headers表示excel表中第一行的表头

        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头

        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //在表中存放查询到的数据放入对应的列
        for (User user : list) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(user.getId());
            row1.createCell(1).setCellValue(user.getNickName());
            row1.createCell(2).setCellValue(user.getUserRealname());
            row1.createCell(3).setCellValue(user.getGender());
            row1.createCell(4).setCellValue(user.getUserphone());
            row1.createCell(5).setCellValue(user.getUserBirthday());
            row1.createCell(6).setCellValue(user.getUserAddress());
            row1.createCell(7).setCellValue(user.getUserAutograph());
            row1.createCell(8).setCellValue(user.getUserOccupation());
            row1.createCell(9).setCellValue(user.getIsMarriage());
            row1.createCell(10).setCellValue(user.getUserHeight());
            row1.createCell(11).setCellValue(user.getUserEducation());
            row1.createCell(12).setCellValue(user.getUserSalary());
            row1.createCell(13).setCellValue(user.getUserInterest());
            row1.createCell(14).setCellValue(user.getUserJurisdiction());
            row1.createCell(15).setCellValue(user.getUserIdcard());
            row1.createCell(16).setCellValue(user.getAvatarUrl());
            row1.createCell(17).setCellValue(user.getUserIntegral());
            row1.createCell(18).setCellValue(user.getIsrealname());
            row1.createCell(19).setCellValue(user.getOpenid());
            row1.createCell(20).setCellValue(user.getUsernative());
            row1.createCell(21).setCellValue(user.getUnionid());
            row1.createCell(22).setCellValue(user.getProvince());
            row1.createCell(23).setCellValue(user.getCity());
            row1.createCell(24).setCellValue(user.getCountry());
           rowNum++;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }



    //导入
    @RequestMapping(value = "/Import")
    public String batchImportUserKnowledge(@RequestParam(value="filename") MultipartFile file,
                                           HttpServletRequest request, HttpServletResponse response, HttpSession session
                                           ) throws IOException{

        //判断文件是否为空
        if(file==null){
            session.setAttribute("msg","文件不能为空！");
            return "redirect:toUserKnowledgeImport";
        }

        //获取文件名
        String fileName=file.getOriginalFilename();

        //验证文件名是否合格
        if(!ExcelImportUtils.validateExcel(fileName)){
            session.setAttribute("msg","文件必须是excel格式！");
            return "redirect:toUserKnowledgeImport";
        }

        //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
        long size=file.getSize();
        if(StringUtils.isEmpty(fileName) || size==0){
            session.setAttribute("msg","文件不能为空！");
            return "redirect:toUserKnowledgeImport";
        }

        //批量导入
        String message = excelService.batchImport(fileName,file);
        session.setAttribute("msg",message);
        return "redirect:toUserKnowledgeImport";
    }
}
