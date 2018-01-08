package cn.porkchop.ebuy.content.service;

import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.TbContent;

import java.util.List;

public interface ContentService {
    /**
     * 添加内容
     * @date 2018/1/5 23:02
     * @author porkchop
     */
    E3Result addContent(TbContent content);
    /**
     * 根据类别id获得内容
     * @date 2018/1/6 15:23
     * @author porkchop
     */
    List<TbContent> getContentListByCategoryId(long cid);
}
