package cn.porkchop.ebuy.order.controller;

import cn.porkchop.ebuy.cart.service.CartService;
import cn.porkchop.ebuy.order.service.OrderService;
import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.OrderInfo;
import cn.porkchop.ebuy.pojo.TbItem;
import cn.porkchop.ebuy.pojo.TbUser;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
/**
 * 显示订单确认页面
 * @date 2018/2/3 15:28
 * @author porkchop
 */
    @RequestMapping("/order-cart")
    public String showOrderCart(HttpServletRequest request){
        TbUser user = (TbUser) request.getAttribute("user");
        List<TbItem> list = cartService.getCartListFromRedis(user.getId());
        request.setAttribute("cartList",list);
        return "order-cart";
    }
/**
 * 生成订单
 * @date 2018/2/3 15:28
 * @author porkchop
 */
    @RequestMapping("/create")
    public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
        TbUser user = (TbUser)request.getAttribute("user");
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());
        E3Result result = orderService.createOrder(orderInfo);
        request.setAttribute("orderId", result.getData());
        request.setAttribute("payment", orderInfo.getPayment());
        request.setAttribute("date",new DateTime().toString("yyyy-MM-dd"));
        return "success";
    }


}
