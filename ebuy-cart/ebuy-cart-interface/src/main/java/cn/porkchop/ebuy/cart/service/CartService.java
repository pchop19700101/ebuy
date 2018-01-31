package cn.porkchop.ebuy.cart.service;

import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.TbItem;

import java.util.List;

/**
 * <pre>把购物车的商品添加到redis时,
 *      保存为hash,key为 用户id,field为商品id,value为商品对象的json<pre/>
 * @date 2018/1/30 19:06
 * @author porkchop
 */
public interface CartService {
    /**
     * 添加商品到redis
     * @date 2018/1/30 19:05
     * @author porkchop
     */
    E3Result addItemToRedis(long itemId,long userId,int num);

    /**
     * 合并购物车,把cookie的移入redis中
     * @date 2018/1/30 23:38
     * @author porkchop
     */
    E3Result merge(Long id, List<TbItem> list);

    /**
     * 从redis中取出购物车
     * @date 2018/1/31 11:35
     * @author porkchop
     */
    List<TbItem> getCartListFromRedis(long userId);

    /**
     * 更新redis中的商品数量
     * @date 2018/1/31 13:36
     * @author porkchop
     */
    E3Result updateNumToRedis(long userId,long itemId,int num);
    /**
     * 从购物车中删除这个商品
     * @date 2018/1/31 14:44
     * @author porkchop
     */
    E3Result deleteFromRedis(long userId,long itemId);

}
