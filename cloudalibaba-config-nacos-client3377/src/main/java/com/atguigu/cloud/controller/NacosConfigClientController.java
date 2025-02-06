package com.atguigu.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
//在控制器类加入@RefreshScope注解使当前类下的配置支持Nacos的动态刷新功能，为了满足动态刷新和全局广播通知需求，相当于服务器一变更本地可以马上获得
public class NacosConfigClientController
{
    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config/info")
    public String getConfigInfo() {
        return configInfo;
    }
}
