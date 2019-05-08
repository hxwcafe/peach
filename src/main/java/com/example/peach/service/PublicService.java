package com.example.peach.service;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.merge.UAlbum;
import com.example.peach.pojo.pay.TemplateData;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface PublicService {

    ServiceResponse<Void> recordExtension(Integer eid, Integer uid);

    void ExcelDown(List<UAlbum> uAlbumList,HttpServletResponse response) throws IOException;//活动报名的的导出

    void ExcelUser(String ider, HttpServletResponse response);//用户导出
    //消息推送
    ServiceResponse<String> wxtsUserInfo(String openid, String templateid, String formid, String accesstoken, String page, Map<String, TemplateData> data);
}
