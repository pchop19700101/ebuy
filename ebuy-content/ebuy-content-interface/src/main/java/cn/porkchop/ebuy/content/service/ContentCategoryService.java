package cn.porkchop.ebuy.content.service;


import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.EasyUITreeNode;

import java.util.List;

public interface ContentCategoryService {
    /**
     * 获得内容分类列表
     * @date 2018/1/5 19:29
     * @author porkchop
     */
    List<EasyUITreeNode> getContentCategoryList(long parentId);
    /**
     * 添加内容分类
     * @date 2018/1/5 19:29
     * @author porkchop
     */
    E3Result addContentCategory(long parentId, String name);
}
