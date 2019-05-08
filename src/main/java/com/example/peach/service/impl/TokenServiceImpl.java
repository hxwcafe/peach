package com.example.peach.service.impl;

import com.example.peach.dao.TokenMapper;
import com.example.peach.pojo.Token;
import com.example.peach.service.TokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/11/16.
 */


@Service
public class TokenServiceImpl implements TokenService {

    @Resource
    private TokenMapper tokenMapper;//注入mapper



    /**
     * 查詢
     * @param
     * @return
     */
    @Override
    public Token selectById() {
        Token token = tokenMapper.selectById(1);
            if (token!=null){
                return token;
            }else {
                return null;
            }
    }

    /**
     * 修改
     * @param
     * @return
     */
    @Override
    public int updateById(String access_token) {


        int rs=tokenMapper.updateById(access_token);
        return rs;

    }
}
