package com.example.peach.util;

import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

//参数校验
public class Check {
	private String requestToken = "a42baa1d879ab77b53ebec0b3870b9f6";//密文
	
	
	
	//登录参数校验,正确返回true
	public static boolean checkLogin(String uname,String pwd,String code){	//用户名,密码,验证码
		//输入为空
		if("".equals(uname) || "".equals(pwd) || "".equals(code)){
			return false;
		}
		//未获取到值
		if(uname==null || pwd==null || code==null){
			return false;
		}
		return true;
	}
	
	public void checkRequestParam(String param) {
		if(param ==null || param.equals("")) {
			throw new RuntimeException("参数有误");
		}
	}
	
	//参数批量验证
	public void checkRequestParam(String[] params) {
		for (int i = 0; i < params.length; i++) {
			if(params[i] ==null || params[i].equals("")) {
				throw new RuntimeException("参数有误");
			}
		}
	}
	
	//数字验证
	public Integer StringParseInteger(String str) {
		Integer i ;
		try {
			i = Integer.parseInt(str);
			return i;
		} catch (Exception e) {
			throw new RuntimeException("参数有误");
		}
	}
	
//
//	//登录验证
//	public Map checkSession(String session) {
//		RedisUtil r = new RedisUtil();
//		checkRequestParam(session);
//		String user = r.get(session);
//		if(user==null || user.equals("")) {
//			throw new RuntimeException("未登录,请先登录");
//		}
//		Map map  = (Map) JSON.parse(user);
//		return map;
//	}
	//登录验证(后台)
	public void checkSession(HttpSession session) {
		if(session.getAttribute("loginuser")==null) {
			throw new RuntimeException("未登录");
		}
	}
	
	//token验证
	public void checkToken(HttpServletRequest request) {
		String key = request.getParameter("key");
		String time = request.getParameter("time");
		checkRequestParam(new String[] {key,time});
		if(!DigestUtils.md5Hex(requestToken+time).equals(key)) {
			throw new RuntimeException("Token验证不通过");
		}
	}
	
	public static String jsonString(String s){
        char[] temp = s.toCharArray();       
        int n = temp.length;
        for(int i =0;i<n;i++){
            if(temp[i]==':'&&temp[i+1]=='"'){
                    for(int j =i+2;j<n;j++){
                        if(temp[j]=='"'){
                            if(temp[j+1]!=',' &&  temp[j+1]!='}'){
                                temp[j]='”';
                            }else if(temp[j+1]==',' ||  temp[j+1]=='}'){
                                break ;
                            }
                        }
                    }   
            }
        }       
        return new String(temp);
    }
	public static Integer check2Num(Integer[] newData,Integer[] oldData) {
		List<Integer> list1 = new ArrayList<Integer>();
		List<Integer> list2 = new ArrayList<Integer>();
		for (int i = 0; i < newData.length; i++) {
			list1.add(newData[i]);
		}
		for (int j = 0; j < oldData.length; j++) {
			list2.add(oldData[j]);
		}
		list2.removeAll(list1);
		if(list2.size()>0) {
			return list2.get(0);
		}else {
			return null;
		}
	}
}
