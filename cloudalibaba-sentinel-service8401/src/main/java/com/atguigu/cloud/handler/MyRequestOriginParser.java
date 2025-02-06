package com.atguigu.cloud.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component//接口要用需添加Component注解方式
public class MyRequestOriginParser implements RequestOriginParser {

    @Override//凡是类要实现接口需要导包
    //调用方信息通过方法中的参数传入
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("serverName");
    }
}
