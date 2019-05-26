package com.xd.common;

import com.xd.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {
    private static final String START_TIME = "requestStartTime";
    //请求处理前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        Map parameterMap = request.getParameterMap();
        log.info("request start. url:{}, params:{}",url, JsonMapper.objToString(parameterMap));
        //记录请求时间
        long start = System.currentTimeMillis();
        request.setAttribute(START_TIME,start);
        return true;
    }
    //请求正常处理后调用
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        String url = request.getRequestURL().toString();
//        Map parameterMap = request.getParameterMap();
//        long start = (Long)request.getAttribute(START_TIME);
//        long end = System.currentTimeMillis();
//        log.info("request finished. url:{}, cost:{}",url, end-start);
    }
    //处理后调用，包括不正常处理
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURL().toString();
        Map parameterMap = request.getParameterMap();
        long start = (Long)request.getAttribute(START_TIME);
        long end = System.currentTimeMillis();
        log.info("request Completion. url:{}, cost:{}",url, end-start);
    }
}
