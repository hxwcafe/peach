package com.example.peach.common;

public enum  ResponseCode {
    SUCCESS(true,"SUCCESS"),
    ERROR(false,"ERROR");
    private final Boolean code;
    private final String desc;

    ResponseCode(Boolean code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public Boolean getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
