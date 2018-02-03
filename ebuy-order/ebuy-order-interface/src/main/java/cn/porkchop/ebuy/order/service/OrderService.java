package cn.porkchop.ebuy.order.service;

import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.OrderInfo;

public interface OrderService {
    /**
     * 创建订单
     *
     * @date 2018/2/3 14:14
     * @author porkchop
     */
    E3Result createOrder(OrderInfo orderInfo);
}
