package cn.porkchop.ebuy.order.service.impl;

import cn.porkchop.ebuy.jedis.JedisClient;
import cn.porkchop.ebuy.mapper.TbOrderItemMapper;
import cn.porkchop.ebuy.mapper.TbOrderMapper;
import cn.porkchop.ebuy.mapper.TbOrderShippingMapper;
import cn.porkchop.ebuy.order.service.OrderService;
import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.OrderInfo;
import cn.porkchop.ebuy.pojo.TbOrderItem;
import cn.porkchop.ebuy.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Value("${CART_REDIS_KEY}")
    private String CART_REDIS_KEY;


    @Value("${ORDER_GENERATE_KEY}")
    private String ORDER_GENERATE_KEY;
    @Value("${ORDER_ID_BEGIN}")
    private String ORDER_ID_BEGIN;
    @Value("${ORDER_ITEM_ID_GEN_KEY}")
    private String ORDER_ITEM_ID_GEN_KEY;

    @Override
    public E3Result createOrder(OrderInfo orderInfo) {
        if (!jedisClient.exists(ORDER_GENERATE_KEY)) {
            //没有设置过初始值
            jedisClient.set(ORDER_GENERATE_KEY, ORDER_ID_BEGIN);
        }
        String orderId = jedisClient.incr(ORDER_GENERATE_KEY).toString();
        orderInfo.setOrderId(orderId);
        orderInfo.setPostFee("10");
        //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        orderMapper.insert(orderInfo);


        List<TbOrderItem> orderItemList=orderInfo.getOrderItems();
        for(TbOrderItem item:orderItemList){
            String orderItemId = jedisClient.incr("ORDER_ITEM_ID_GEN_KEY").toString();
            item.setId(orderItemId);
            item.setOrderId(orderId);
            orderItemMapper.insert(item);
        }


        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        orderShippingMapper.insert(orderShipping);
        //删除redis中购物车的内容
        jedisClient.del(CART_REDIS_KEY+":"+orderInfo.getUserId());
        return E3Result.ok(orderId);
    }
}
