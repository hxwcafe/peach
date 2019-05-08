package com.example.peach.service;

import com.example.peach.common.ServiceResponse;

import java.util.List;

public interface UILinkedService {
    //插入兴趣
    ServiceResponse<String> addUILinked(List<Integer> intlist, Integer userId);
    ServiceResponse<String> UIKinkedHandle(String category, Integer userId);

}
