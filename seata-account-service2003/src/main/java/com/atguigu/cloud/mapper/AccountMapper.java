package com.atguigu.cloud.mapper;

import com.atguigu.cloud.entities.Account;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.common.Mapper;

public interface AccountMapper extends Mapper<Account>
{

    /**
     * @param userId
     * @param money 本次消费金额
     * 什么样的用户账号减了多少金额与参数一一对应
     */
    void decrease(@Param("userId") Long userId, @Param("money") Long money);
}

