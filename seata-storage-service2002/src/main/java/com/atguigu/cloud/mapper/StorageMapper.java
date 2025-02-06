package com.atguigu.cloud.mapper;

import com.atguigu.cloud.entities.Storage;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.common.Mapper;
import org.apache.ibatis.annotations.Param;

public interface StorageMapper extends Mapper<Storage>
{
    /**
     * 扣减库存
     */
    void decrease(@Param("productId") Long productId, @Param("count") Integer count);
    //decrease方法对标的对象就不是自动生成的，而是根据对应产品id，减少几个库存
}