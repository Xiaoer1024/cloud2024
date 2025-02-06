package com.atguigu.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RateLimitController
{
    @GetMapping("/rateLimit/byUrl")//访问此地址将会返回以下结果
    public String byUrl()
    {
        return "按rest地址限流测试OK";
    }

    @GetMapping("/rateLimit/byResource")
    @SentinelResource(value = "byResourceSentinelResource",blockHandler = "handlerBlockHandler")
    //value指定哪一个资源需要哨兵保护,value自定义一个不重复的名字,此名字与流控面板中的资源名要一致。如果没限流则调用byResource方法
    //blockHandler中的名字要与方法名一致。限流则调用自定义方法handlerBlockHandler
    public String byResource(){
        return "按照资源名称sentinelResource限流测试OK";
    }

    public String handlerBlockHandler(BlockException blockException){
        return "服务不可用触发了@SentinelResource启动";
    }

    @GetMapping("/rateLimit/doAction/{p1}")
    @SentinelResource(value = "doActionSentinelResource",
            blockHandler = "doActionBlockHandler", fallback = "doActionFallback")
    //doActionFallback出问题了则由doActionFallback异常处理方法处理
    //doAction中的参数需要全部拷贝到blockHandler和fallBack方法中，保证重载时能找到
    public String doAction(@PathVariable("p1") Integer p1) {
        if (p1 == 0){
            throw new RuntimeException("p1等于零直接异常");
        }
        return "doAction";
    }

    public String doActionBlockHandler(@PathVariable("p1") Integer p1,BlockException e){
        log.error("sentinel配置自定义限流了:{}", e);
        return "sentinel配置自定义限流了";
    }

    public String doActionFallback(@PathVariable("p1") Integer p1,Throwable e){
        log.error("程序逻辑异常了:{}", e);
        return "程序逻辑异常了"+"\t"+e.getMessage();
    }

    /**
     * 热点参数限流
     * @param p1
     * @param p2
     * @return
     */
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler = "dealHandler_testHotKey")
    //在sentinel配置里面触发了testHotKey规则，则按以下方法进行处理
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,

                             @RequestParam(value = "p2",required = false) String p2){
        return "------testHotKey";
    }
    public String dealHandler_testHotKey(String p1,String p2,BlockException exception)
    {
        return "-----dealHandler_testHotKey";
    }

}