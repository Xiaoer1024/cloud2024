package com.atguigu.cloud.controller;

import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.service.StorageService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StorageController
{
    @Resource
    private StorageService storageService;//Controller调用Service层

    /**
     * 扣减库存
     */
    @RequestMapping("/storage/decrease")
    public ResultData decrease(@RequestParam("productId") Long productId,@RequestParam("count") Integer count) {

        storageService.decrease(productId, count);//Service调用自己封装的方法
        return ResultData.success("扣减库存成功!");
    }
}
