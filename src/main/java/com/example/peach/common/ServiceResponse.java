package com.example.peach.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * service的响应类
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean status;//状态:0,1
    private String msg;//响应的信息
    private T data;//数据

    private ServiceResponse(Boolean status){
        this.status = status;
    }
    private ServiceResponse(Boolean status,String msg){
        this.status = status;
        this.msg = msg;
    }
    private ServiceResponse(Boolean status,T data){
        this.status = status;
        this.data = data;
    }
    private ServiceResponse(Boolean status,String msg,T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    //是否响应成功
    @JsonIgnore
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS.getCode();
    }

    /**
     * 成功
     * @param <T>
     * @return
     */
    public static  <T> ServiceResponse<T> createBySuccess(){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public static  <T> ServiceResponse<T> createBySuccess(String msg){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static  <T> ServiceResponse<T> createBySuccess(T data){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static  <T> ServiceResponse<T> createBySuccess(String msg, T data){
        return  new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    /**
     * 异常
     * @return
     */
    public static <T> ServiceResponse<T> createByError(){
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode());
    }
    public static <T> ServiceResponse<T> createByError(String errorMsg){
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode(),errorMsg);
    }
    public static <T> ServiceResponse<T> createByError(T errorData){
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode(),errorData);
    }
    public static <T> ServiceResponse<T> createByError(String errorMsg, T errorData){
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode(),errorMsg,errorData);
    }
}
