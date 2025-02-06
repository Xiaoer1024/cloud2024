package com.atguigu.cloud.apis;

import com.atguigu.cloud.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "seata-storage-service")//参数为Feign接口对应寻找的微服务seata-storage-service
public interface StorageFeignApi
{
    /**
     * 扣减库存
     */
    @PostMapping(value = "/storage/decrease")//映射扣减库存
    //库存扣减，productId对应产品ID，count作为扣减数目传参进来
    ResultData decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);
}
