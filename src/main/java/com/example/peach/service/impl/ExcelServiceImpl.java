package com.example.peach.service.impl;


import com.example.peach.dao.UserMapper;
import com.example.peach.pojo.User;
import com.example.peach.service.ExcelService;
import com.example.peach.service.InterestService;
import com.example.peach.util.ExcelImportUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {


    private final static String excel2003 = ".xls";
    private final static String excel2007 = ".xlsx";

    @Resource
    private UserMapper userMapper;
    @Resource
    private InterestService interestService;

    @Override
    public HashMap<String,Object> selectUserByInterest(int id) {


        HashMap<String,Object> map=new HashMap();
        List<String> list=interestService.selectById(id);
        List<User> users=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            List<User> user1=userMapper.selectUserByInterest(list.get(i));
            if (user1 !=null && user1.size()>0){
                for (int j=0;j<user1.size();j++){
                    users.add(user1.get(j));
                }
            }
        }
        if (users!=null && users.size()>0){
            map.put("User",users);
        }else {
            map.put("User",null);
        }
        return map;
    }

    @Override
    public  int updateUserPhone(User user) {

        return userMapper.updateUserPhone(user);
    }





    @Override
    public User selectByUserPhone(String userPhone) {

        return userMapper.selectByphone(userPhone);
    }


    @Override
    public int insert(User user) {
        User user1 = userMapper.selectByphone(user.getUserphone());
        if (user1 != null) {
            return 0;
        } else {
            return userMapper.insert(user);
        }
    }

    /**
     * 上传excel文件到临时目录后并开始解析
     *
     * @param fileName
     * @param
     * @param
     * @return
     */
    @Override
    public String batchImport(String fileName, MultipartFile mfile) {

        File uploadDir = new File("E:\\fileupload");
        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
        if (!uploadDir.exists()) uploadDir.mkdirs();
        //新建一个文件
        File tempFile = new File("E:\\fileupload\\" + new Date().getTime() + ".xls");
        //初始化输入流
        InputStream is = null;
        try {
            //将上传的文件写入新建的文件中
            mfile.transferTo(tempFile);

            //根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);

            //根据版本选择创建Workbook的方式
            Workbook wb = null;
            //根据文件名判断文件是2003版本还是2007版本
            if (ExcelImportUtils.isExcel2007(fileName)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = new HSSFWorkbook(is);
            }
            //根据excel里面的内容读取知识库信息
            return readExcelValue(wb, tempFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        return "导入出错！请检查数据格式！";
    }


    /**
     * 解析Excel里面的数据
     *
     * @param wb
     * @return
     */
    private String readExcelValue(Workbook wb, File tempFile) {

        //错误信息接收器
        String errorMsg = "";
        //得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        //得到Excel的行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        //总列数
        int totalCells = 0;
        //得到Excel的列数(前提是有行数)，从第二行算起
        if (totalRows >= 2 && sheet.getRow(1) != null) {
            totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
        }
        List<User> userList = new ArrayList<User>();

        String br = "<br/>";

        //循环Excel行数,从第二行开始。标题不入库
        for (int r = 1; r < totalRows; r++) {
            String rowMessage = "";
            Row row = sheet.getRow(r);
            if (row == null) {
                errorMsg += br + "第" + (r + 1) + "行数据有问题，请仔细检查！";
                continue;
            }
            User user = new User();


            Integer id= null;
            String userNickname = "";
            String userRealname = "";
            int userSex = 0;
            String userPhone = "";
            Date userBirthday = null;
            String userAddress = "";
            String userAutograph = "";
            String userOccupation = "";
            int isMarriage = 0;
            Integer userHeight = null;
            String userEducation = "";
            String userSalary = "";
            String userInterest = "";
            Integer userJurisdiction = null;
            String userIdcard = "";
            String headimgurl = "";
            Integer userIntegral = null;
            int isRealname = 0;
            String openid = "";
            String userNative = "";
            String unionid = "";
            String province = "";
            String city = "";
            String country = "";


            //循环Excel的列
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        id= (int)cell.getNumericCellValue();
                        user.setId(id);
                    } else if (c == 1) {
                        userNickname = cell.getStringCellValue();
                        user.setNickName(userNickname);
                    } else if (c == 2) {
                        userRealname = cell.getStringCellValue();
                        user.setUserRealname(userRealname);
                    } else if (c == 3) {
                        userSex = (int)cell.getNumericCellValue();
                        if (userSex!=0){
                            user.setGender(1);
                        }else {
                            user.setGender(0);
                        }
                    } else if (c == 4) {
                        userPhone = String.valueOf(cell.getNumericCellValue());
                        user.setUserphone(userPhone);
                    } else if (c == 5) {
                        userBirthday = cell.getDateCellValue();
                        user.setUserBirthday((java.sql.Date) userBirthday);
                    } else if (c == 6) {
                        userAddress = cell.getStringCellValue();
                        user.setUserAddress(userAddress);
                    } else if (c == 7) {
                        userAutograph = cell.getStringCellValue();
                        user.setUserAutograph(userAutograph);
                    } else if (c == 8) {
                        userOccupation = cell.getStringCellValue();
                        user.setUserOccupation(userOccupation);
                    } else if (c == 9) {
                        isMarriage = (int)cell.getNumericCellValue();
                        if (isMarriage!=0){
                            user.setIsMarriage(true);
                        }else {
                            user.setIsMarriage(false);
                        }
                    } else if (c == 10) {
                        userHeight = (int)cell.getNumericCellValue();
                        user.setUserHeight(userHeight);
                    } else if (c == 11) {
                        userEducation = cell.getStringCellValue();
                        user.setUserEducation(userEducation);
                    } else if (c == 12) {
                        userSalary = cell.getStringCellValue();
                        user.setUserSalary(userSalary);
                    } else if (c == 13) {
                        userInterest = cell.getStringCellValue();
                        user.setUserInterest(userInterest);
                    } else if (c == 14) {
                        userJurisdiction = (int)cell.getNumericCellValue();
                        user.setUserJurisdiction(userJurisdiction);
                    } else if (c == 15) {
                        userIdcard = cell.getStringCellValue();
                        user.setUserIdcard(userIdcard);
                    } else if (c == 16) {
                        headimgurl = cell.getStringCellValue();
                        user.getAvatarUrl();
                    } else if (c == 17) {
                        userIntegral = (int)cell.getNumericCellValue();
                        user.setUserIntegral(userIntegral);
                    } else if (c == 18) {
                        isRealname = (int)cell.getNumericCellValue();
                        if (isRealname!=0){
                            user.setIsrealname(true);
                        }else {
                            user.setIsrealname(false);
                        }
                    } else if (c == 19) {
                        openid = cell.getStringCellValue();
                        user.setOpenid(openid);
                    } else if (c == 20) {
                        userNative = cell.getStringCellValue();
                        user.setUsernative(userNative);
                    } else if (c == 21) {
                        unionid = cell.getStringCellValue();
                        user.setUnionid(unionid);
                    } else if (c == 22) {
                        province = cell.getStringCellValue();
                        user.setProvince(province);
                    } else if (c == 23) {
                        city = cell.getStringCellValue();
                        user.setCity(city);
                    } else if (c == 24) {
                        country = cell.getStringCellValue();
                        user.setCountry(country);
                    }
                }
            }

            //拼接每行的错误提示
            if (!StringUtils.isEmpty(rowMessage)) {
                errorMsg += br + "第" + (r + 1) + "行，" + rowMessage;
            } else {
                userList.add(user);
            }

            //删除上传的临时文件
            if (tempFile.exists()) {
                tempFile.delete();
            }

        }
        if (StringUtils.isEmpty(errorMsg)) {
            for (User user1 : userList) {
                int rs=this.insert(user1);
                System.out.println(rs);
            }
            errorMsg = "导入成功，共" + userList.size() + "条数据！";
        }
        return errorMsg;
    }

}
//                        answer = cell.getStringCellValue();
//                        if(StringUtils.isEmpty(answer)){
//                            rowMessage += "答案不能为空；";
//                        }else if(answer.length()>1000){
//                          rowMessage += "答案的字数不能超过1000；";
//                       }
//                        tempUserKB.setAnswer(answer);