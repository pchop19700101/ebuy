package cn.porkchop.ebuy.manager.service.impl;

import cn.porkchop.ebuy.jedis.JedisClient;
import cn.porkchop.ebuy.mapper.TbItemDescMapper;
import cn.porkchop.ebuy.mapper.TbItemMapper;
import cn.porkchop.ebuy.pojo.*;
import cn.porkchop.ebuy.manager.service.ItemService;
import cn.porkchop.ebuy.utils.IDUtils;
import cn.porkchop.ebuy.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource
    private Destination addItemTopic;
    @Value("${REDIS_ITEM_INFO_PRE}")
    private String REDIS_ITEM_INFO_PRE;
    @Autowired
    private JedisClient jedisClient;
    @Value("${ITEM_INFO_EXPIRE_TIME}")
    private int ITEM_INFO_EXPIRE_TIME;

    @Override
    public TbItem getItemById(Long id) {
        //从redis中获取
        String json = jedisClient.get(REDIS_ITEM_INFO_PRE + ":" + id + ":BASE");
        if (StringUtils.isNotBlank(json)) {
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            return tbItem;
        }

        TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
        //添加到redis中
        if (tbItem != null) {
            jedisClient.set(REDIS_ITEM_INFO_PRE + ":" + id + ":BASE", JsonUtils.objectToJson(tbItem));
            jedisClient.expire(REDIS_ITEM_INFO_PRE + ":" + id + ":BASE", ITEM_INFO_EXPIRE_TIME);
        }
        return tbItem;

    }


    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        PageHelper.startPage(page, rows);
        List<TbItem> list = tbItemMapper.selectByExample(new TbItemExample());
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        return new EasyUIDataGridResult(pageInfo.getTotal(), list);
    }

    @Override
    public E3Result addItem(TbItem tbItem, String desc) {
        long id = IDUtils.genItemId();
        //补全不填写的属性
        tbItem.setId(id);
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        //插入表中
        int insert = tbItemMapper.insert(tbItem);
        //创建详情
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(id);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        tbItemDesc.setItemDesc(desc);
        //插入详情
        tbItemDescMapper.insert(tbItemDesc);
        //发送添加商品的消息,内容是商品id
        jmsTemplate.send(addItemTopic, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {

                return session.createTextMessage(id + "");
            }
        });
        return E3Result.ok();
    }

    @Override
    public TbItemDesc getItemDescByItemId(long id) {
        //从redis中获取
        String json = jedisClient.get(REDIS_ITEM_INFO_PRE + ":" + id + ":DESC");
        if (StringUtils.isNotBlank(json)) {
            TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
            return tbItemDesc;
        }
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(id);
        //添加到redis中
        if (tbItemDesc != null) {
            jedisClient.set(REDIS_ITEM_INFO_PRE + ":" + id + ":DESC", JsonUtils.objectToJson(tbItemDesc));
            jedisClient.expire(REDIS_ITEM_INFO_PRE + ":" + id + ":DESC", ITEM_INFO_EXPIRE_TIME);
        }
        return tbItemDesc;
    }

}
