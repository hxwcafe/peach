package com.example.peach.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PublicMapper {
    public String selectDictionariesByKey(@Param("d_key") String d_key);
    public int insertExtension(@Param("eid") Integer eid, @Param("uid") Integer uid, @Param("isReturn") Integer isReturn);
    public Integer selectExtensionCountById(@Param("uid") Integer uid);
}
