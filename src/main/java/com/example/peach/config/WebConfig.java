package com.example.peach.config;

import com.example.peach.interceptor.UrlInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Created by Administrator on 2018/11/11.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    //    private final ApiSecurityProperties security;
//    @Autowired
//    public WebInterceptorConfig(ApiSecurityProperties security) {
//        this.security = security;
//    }
    @Autowired
    private UrlInterceptor urlInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(urlInterceptor).addPathPatterns("/test/**").excludePathPatterns("/test/test",
                "/test/toLogin");

    }
    //可以方便的将一个请求映射成视图，无需书写控制器，addViewCOntroller("请求路径").setViewName("请求页面文件路径")
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/show/getLogin").setViewName("login");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 资源映射路径
         * addResourceHandler：访问映射路径
         * addResourceLocations：资源绝对路径
         */
      //  registry.addResourceHandler("/static/images/**").addResourceLocations("/static/images/");
    }


}
