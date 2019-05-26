package com.xd.common;

import com.xd.exception.ParamException;
import com.xd.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        String url = httpServletRequest.getRequestURL().toString();
        ModelAndView mv;
        String defaultMsg = "System error";
        //判断数据or页面请求
        //提前定义所有的数据请求用.json结尾，所有页面请求用.page结尾
        if(url.endsWith(".json")){
            //定义自己的异常类，来判断如何输出相应的msg
            if(e instanceof PermissionException || e instanceof ParamException){
                JsonData result = JsonData.fail(e.getMessage());
                mv = new ModelAndView("jsonView",result.toMap());   //在spring-servlet中定义过jsonView
            }else{
                log.error("unknown json exception, url: " + url + e); //打印异常
                JsonData result = JsonData.fail(defaultMsg);
                mv = new ModelAndView("jsonView",result.toMap());
            }
        }else if (url.endsWith(".page")){
            log.error("unknown page exception, url: " + url + e);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("exception",result.toMap());      //对应/views/exception.jsp,注意引入jsp-api依赖
        }else{
            log.error("unknown exception, url: " + url + e);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("jsonView",result.toMap());
        }
        return mv;
    }
}
