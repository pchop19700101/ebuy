package cn.porkchop.ebuy.content.service.impl;

import cn.porkchop.ebuy.content.service.ContentService;
import cn.porkchop.ebuy.jedis.JedisClient;
import cn.porkchop.ebuy.mapper.TbContentMapper;
import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.TbContent;
import cn.porkchop.ebuy.pojo.TbContentExample;
import cn.porkchop.ebuy.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;
    @Autowired
    private JedisClient cluster;
    @Value("${CONTENT_CATEGORY}")
    private String CONTENT_CATEGORY;


    @Override
    public E3Result addContent(TbContent content) {
        content.setUpdated(new Date());
        content.setCreated(new Date());
        tbContentMapper.insert(content);
        //把redis中和此对象相同类别的内容删除,重新从数据库中读取
        cluster.hdel(CONTENT_CATEGORY,content.getCategoryId()+"");
        return E3Result.ok();
    }

    @Override
    public List<TbContent> getContentListByCategoryId(long cid) {
        //先从redis获取
        String json = cluster.hget(CONTENT_CATEGORY, cid + "");
        if(StringUtils.isNotBlank(json)){
            List<TbContent> tbContents = JsonUtils.jsonToList(json, TbContent.class);
            return tbContents;
        }
        //redis中没有,从数据库中找
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> tbContentList = tbContentMapper.selectByExampleWithBLOBs(tbContentExample);
        //保存到redis中
        cluster.hset(CONTENT_CATEGORY,cid+"", JsonUtils.objectToJson(tbContentList));
        return tbContentList;
    }
}
