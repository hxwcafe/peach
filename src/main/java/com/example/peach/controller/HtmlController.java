package com.example.peach.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/11/29.
 */
@Controller
@RequestMapping("/http")
public class HtmlController {
    private static Logger logger = Logger.getLogger(HtmlController.class);
    @RequestMapping(value = "/index")
    public String getHtml(){
        return "login";
    }

    @RequestMapping(value = "/test")
    public String test(){
        logger.error("------------error----------------");
        return "666";
    }
}
