package cn.porkchop.ebuy.cart.service.impl;

import cn.porkchop.ebuy.cart.service.CartService;
import cn.porkchop.ebuy.jedis.JedisClient;
import cn.porkchop.ebuy.mapper.TbItemMapper;
import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.TbItem;
import cn.porkchop.ebuy.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${CART_REDIS_KEY}")
    private String CART_REDIS_KEY;

    @Override
    public E3Result addItemToRedis(long itemId, long userId, int num) {
        Boolean isCartExist = jedisClient.hexists(CART_REDIS_KEY + ":" + userId, itemId + "");
        if (isCartExist) {
            //有添加过这个商品,则修改数量
            String json = jedisClient.hget(CART_REDIS_KEY + ":" + userId, itemId + "");
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            tbItem.setNum(tbItem.getNum() + num);
            jedisClient.hset(CART_REDIS_KEY + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
            return E3Result.ok();
        }
        //没有添加过这个商品
        //需要从数据库中查询
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        item.setNum(num);
        //取第一张图片
        item.setImage(item.getImage().split(",")[0]);
        jedisClient.hset(CART_REDIS_KEY + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        return E3Result.ok();
    }

    @Override
    public E3Result merge(Long itemId, List<TbItem> list) {
        for (TbItem item : list) {
            addItemToRedis(item.getId(), itemId, item.getNum());
        }
        return E3Result.ok();
    }

    @Override
    public List<TbItem> getCartListFromRedis(long userId) {
        List<String> jsonList = jedisClient.hvals(CART_REDIS_KEY + ":" + userId);
        ArrayList<TbItem> cartList = new ArrayList<>();
        for (String json : jsonList) {
            cartList.add(JsonUtils.jsonToPojo(json, TbItem.class));
        }
        return cartList;
    }

    @Override
    public E3Result updateNumToRedis(long userId, long itemId, int num) {
        TbItem item = JsonUtils.jsonToPojo(jedisClient.hget(CART_REDIS_KEY + ":" + userId, itemId + ""), TbItem.class);
        item.setNum(num);
        jedisClient.hset(CART_REDIS_KEY + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        return E3Result.ok();
    }

    @Override
    public E3Result deleteFromRedis(long userId, long itemId) {
        jedisClient.hdel(CART_REDIS_KEY + ":" + userId, itemId + "");
        return E3Result.ok();
    }


}
