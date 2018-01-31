package cn.porkchop.ebuy.cart.controller;

import cn.porkchop.ebuy.cart.service.CartService;
import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.TbItem;
import cn.porkchop.ebuy.pojo.TbUser;
import cn.porkchop.ebuy.manager.service.ItemService;
import cn.porkchop.ebuy.sso.service.UserService;
import cn.porkchop.ebuy.utils.JsonUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Value("${CART_COOKIE_NAME}")
    private String CART_COOKIE_NAME;
    @Value("${CART_COOKIE_EXPIRE_SECOND}")
    private Integer CART_COOKIE_EXPIRE_SECOND;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    /**
     * <pre>添加商品到购物车,如果登陆,则放到redis中,
     *                       未登录,则放到cookie中</pre>
     *
     * @date 2018/1/30 18:37
     * @author porkchop
     */
    @RequestMapping("/add/{itemid}")
    public String addItemToCart(@PathVariable long itemid, @RequestParam(defaultValue = "1") int num, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        TbUser user = getUser(request);
        if (user != null) {
            //用户已经登陆
            cartService.addItemToRedis(itemid, user.getId(), num);
            return "cartSuccess";
        }
        //用户没有登陆
        List<TbItem> list = getCartListFromCookie(request);
        for (TbItem item : list) {
            if (item.getId().equals(itemid)) {
                //遍历cookie中的数据,已经有这个商品,就增加数量
                item.setNum(item.getNum() + num);
                //保存到cookie中
                setCartListToCookie(request, list, response);
                return "cartSuccess";
            }
        }
        //cookie中没有这个商品
        TbItem item = itemService.getItemById(itemid);
        if (item == null) {
            //没有这个商品,直接跳转到添加成功
            return "cartSuccess";
        }
        item.setNum(num);
        //取第一张图片
        item.setImage(item.getImage().split(",")[0]);
        list.add(item);
        //保存到cookie中
        setCartListToCookie(request, list, response);
        return "cartSuccess";
    }

    /**
     * 展示购物车页面
     *
     * @date 2018/1/30 13:10
     * @author porkchop
     */
    @RequestMapping("/cart")
    public String showCart(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
        TbUser user = getUser(request);
        if (user != null) {
            //用户已经登陆
            List<TbItem> list = getCartListFromCookie(request);
            if(list.size()!=0){
                //cookie购物车中有商品,就合并
                cartService.merge(user.getId(),list);
            }
            List<TbItem> cartList= cartService.getCartListFromRedis(user.getId());
            request.setAttribute("cartList",cartList);
            //删除cookie中的值
            Cookie cookie = new Cookie(CART_COOKIE_NAME, "");
            String domain = getDomain(request);
            if (!"localhost".equals(domain)) {
                cookie.setDomain(domain.substring(domain.indexOf(".")));
            }
            cookie.setPath("/");
            cookie.setMaxAge(CART_COOKIE_EXPIRE_SECOND);
            response.addCookie(cookie);
            return "cart";
        }
        List<TbItem> list = getCartListFromCookie(request);
        request.setAttribute("cartList", list);
        return "cart";
    }

    /**
     * 修改商品数量
     *
     * @param num
     *         更新后的数量
     * @date 2018/1/30 14:26
     * @author porkchop
     */
    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateNum(HttpServletResponse response, @PathVariable long itemId, HttpServletRequest request, @PathVariable int num) throws UnsupportedEncodingException {
        TbUser user = getUser(request);
        if (user != null) {
            //用户已经登陆
            return cartService.updateNumToRedis(user.getId(), itemId, num);
        }
        List<TbItem> list = getCartListFromCookie(request);
        for (TbItem item : list) {
            if (item.getId().equals(itemId)) {
                item.setNum(num);
                setCartListToCookie(request, list, response);
                return E3Result.ok();
            }
        }
        return E3Result.build(400, "未找到该商品");
    }

    /**
     * 从购物车中删除该商品
     *
     * @date 2018/1/30 18:01
     * @author porkchop
     */
    @RequestMapping("/delete/{itemId}")
    public String deleteItemFromCart(HttpServletResponse response, HttpServletRequest request, @PathVariable long itemId) throws UnsupportedEncodingException {
        TbUser user = getUser(request);
        if (user != null) {
            //用户已经登陆
            cartService.deleteFromRedis(user.getId(),itemId);
            return "redirect:/cart/cart.html";
        }
        List<TbItem> list = getCartListFromCookie(request);
        for (TbItem item : list) {
            if (item.getId().equals(itemId)) {
                list.remove(item);
                setCartListToCookie(request, list, response);
                break;
            }
        }
        setCartListToCookie(request, list, response);
        return "redirect:/cart/cart.html";

    }

    /**
     * 获得访问的域名,不包含端口
     *
     * @return 域名
     * @date 2018/1/27 21:12
     * @author porkchop
     */
    private String getDomain(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String temp = url.substring(url.indexOf(":") + 3);
        String domainWithPort = temp.substring(0, temp.indexOf("/"));
        return !domainWithPort.contains(":") ? domainWithPort : domainWithPort.substring(0, domainWithPort.indexOf(":"));
    }

    /**
     * 从cookie中获得购物车的list
     *
     * @return 没有购物车cookie时返回空list
     * @date 2018/1/29 23:46
     * @author porkchop
     */
    private List<TbItem> getCartListFromCookie(HttpServletRequest request) throws UnsupportedEncodingException {
        for (Cookie cookie : request.getCookies()) {
            if (CART_COOKIE_NAME.equals(cookie.getName())) {
                if("".equals(cookie.getValue())){
                    return new ArrayList<>();
                }
                //有中文,需要转码
                return JsonUtils.jsonToList(URLDecoder.decode(cookie.getValue(), "utf-8"), TbItem.class);
            }
        }
        return new ArrayList<>();
    }

    /**
     * 把购物车保存到cookie中
     *
     * @date 2018/1/29 23:47
     * @author porkchop
     */
    private void setCartListToCookie(HttpServletRequest request, List<TbItem> list, HttpServletResponse response) throws UnsupportedEncodingException {
        String json = JsonUtils.objectToJson(list);
        Cookie cookie = new Cookie(CART_COOKIE_NAME, URLEncoder.encode(json == null ? "" : json, "utf-8"));
        String domain = getDomain(request);
        //设置共享cookie的域名
        if (!"localhost".equals(domain)) {
            cookie.setDomain(domain.substring(domain.indexOf(".")));
        }
        cookie.setPath("/");
        cookie.setMaxAge(CART_COOKIE_EXPIRE_SECOND);
        response.addCookie(cookie);
    }

    /**
     * 获得登陆的用户信息
     *
     * @return 如果未登陆或非法token, 返回null
     * @date 2018/1/30 18:04
     * @author porkchop
     */
    private TbUser getUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("session")) {
                String session = cookie.getValue();
                Object data = userService.getUserByToken(session).getData();
                if (data != null) {
                    return (TbUser) data;
                }
                //从cookie中拿到的token已经过期
                return null;
            }
        }
        //未登录
        return null;
    }

    @Test
    public void test1() {
        System.out.println(JsonUtils.jsonToList("", Object.class));
    }
}
