package com.example.peach.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/11/11.
 */
@Component
public class UrlInterceptor implements HandlerInterceptor {
//    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityInterceptor.class);
//
//    private final ApiSecurityProperties security;
//
//    @Autowired
//    public UrlInterceptor(ApiSecurityProperties security) {
//        this.security = security;
//    }

        //业务处理请求之前被调用
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception{
            HttpSession session=request.getSession();
                if (session.getAttribute("user")==null){
                    response.sendRedirect("/test/toLogin");
                    return false;
                }
                session.setAttribute("user","asasasa");

            return true;
        }
       // 在业务处理器处理请求完成之后，生成视图之前执行
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
                throws Exception{
        }
        // 在DispatcherServlet完全处理完请求之后被调用，可用于清理资源,日志打印
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
                throws Exception{
        }


}
