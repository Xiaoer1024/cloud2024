package com.atguigu.cloud.service.impl;

import com.atguigu.cloud.apis.AccountFeignApi;
import com.atguigu.cloud.apis.StorageFeignApi;
import com.atguigu.cloud.entities.Order;
import com.atguigu.cloud.mapper.OrderMapper;
import com.atguigu.cloud.service.OrderService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService
{
    @Resource
    private OrderMapper orderMapper;//调用order自己的增删改查

    @Resource
    private StorageFeignApi storageFeignApi;//订单微服务通过OpenFeign去调用库存微服务

    @Resource
    private AccountFeignApi accountFeignApi;//订单微服务通过OpenFeign去调用账户微服务

    @Override
    @GlobalTransactional(name = "Xiaoer-create-order",rollbackFor = Exception.class) //AT
    public void create(Order order) {
        //xid全局事务id的检查，重要
        String xid=RootContext.getXID();//使用seata建议要显示地定义出来
        //1.新建订单
        log.info("------------------开始新建订单："+"\t"+"xid："+xid);
        //订单新建时默认初始的订单状态是零
        order.setStatus(0);
        int result=orderMapper.insertSelective(order);
        //插入订单成功后获得插入mysql的实体对象
        Order orderFromDB=null;

        if(result>0){
            //从mysql里面查出刚插入的记录
            orderFromDB=orderMapper.selectOne(order);
            log.info("------->新建订单成功，orderFromDB info："+orderFromDB);
            System.out.println();
            //2.扣减库存
            log.info("-------> 订单微服务开始调用Storage库存，做扣减count");
            storageFeignApi.decrease(orderFromDB.getProductId(), orderFromDB.getCount());
            log.info("-------> 订单微服务结束调用Storage库存，做扣减完成");
            System.out.println();

            //3. 扣减账号余额
            log.info("-------> 订单微服务开始调用Account账号，做扣减money");
            accountFeignApi.decrease(orderFromDB.getUserId(),orderFromDB.getMoney());
            log.info("-------> 订单微服务结束调用Account账号，做扣减完成");
            System.out.println();

            //4. 修改订单状态,将订单状态从0修改为1,订单状态status：0：创建中；1：已完结
            log.info("-------> 修改订单状态");
            orderFromDB.setStatus(1);

            /**
             * 修改订单状态后，更新数据库操作
             */
            Example whereCondition=new Example(Order.class);
            //mybatis工具里面带条件的东西，相当于mysql中where查询语句后面各种条件的拼接
            Example.Criteria criteria = whereCondition.createCriteria();
            //固定写法套路，相当于把上面各种条件拼接之后赋值给criteria
            criteria.andEqualTo("userId",orderFromDB.getUserId());
            //根据id来筛选更新哪个值
            criteria.andEqualTo("status",0);
            //把状态为0的设置为1
            //相当于把状态为1后，按照查询条件找到对应信息，进行对应条件的覆盖和编写
            int updateResult=orderMapper.updateByExampleSelective(orderFromDB,whereCondition);//更新结果
            log.info("-------> 修改订单状态完成"+"\t"+updateResult);
            log.info("-------> orderFromDB info: "+orderFromDB);
        }
        System.out.println();
        log.info("------------------结束新建订单："+"\t"+"xid："+xid);//能打印出日志说明以上新建订单业务无误

    }
}
