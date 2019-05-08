package com.example.peach.util;

import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 *
 * Created by Administrator on 2018/12/8.
 */
public class FileUpload {

    public static  String  fileUpload( MultipartFile file,String scfille,String photoFolder){
        if (file.isEmpty()) {
            System.out.println("文件为空空");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String basePatha1 = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        try {
            basePatha1 = URLDecoder.decode(basePatha1, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String filePath = basePatha1 + "static/userimage/"; // 上传后的路径
        fileName = RandomStringGenerator.getRandomStringtime(12) + "_" + RandomStringGenerator.getNumber(4) + suffixName;// 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(new File(photoFolder+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = scfille + fileName;
        return filename;
    }

    /**
     * 删除图片
     * @param file 文件路径
     * @param phonefile 图片名
     */
    public static void filedele(String file,String phonefile){
        String[] ar = phonefile.split("/");
        new File(file+ar[ar.length-1]).delete();//删除原先的图片
    }
}
