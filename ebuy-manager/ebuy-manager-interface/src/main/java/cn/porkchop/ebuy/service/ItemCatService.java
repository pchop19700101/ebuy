package cn.porkchop.ebuy.service;

import cn.porkchop.ebuy.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
    /**
     * 根据父节点的id得到商品分类
     *
     * @date 2017/12/26 19:16
     * @author porkchop
     */
    List<EasyUITreeNode> getCatList(long parentId);
}
