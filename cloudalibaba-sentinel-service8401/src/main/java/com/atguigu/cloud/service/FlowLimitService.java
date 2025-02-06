package com.atguigu.cloud.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

@Service
public class FlowLimitService
{
    @SentinelResource(value = "common")//哨兵需要的资源，方法名common
    public void common()
    {
        System.out.println("------FlowLimitService come in");
    }
}
