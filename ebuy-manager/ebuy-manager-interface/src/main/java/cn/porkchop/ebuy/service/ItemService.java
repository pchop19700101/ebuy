package cn.porkchop.ebuy.service;

import cn.porkchop.ebuy.pojo.EasyUIDataGridResult;
import cn.porkchop.ebuy.pojo.EasyUITreeNode;
import cn.porkchop.ebuy.pojo.TbItem;

public interface ItemService{
    /**
     * 根据商品id获得商品信息
     * @date 2017/12/26 19:15
     * @author porkchop
     */
    TbItem getItemById(Long id);
    /**
     * 分页显示商品列表
     * @date 2017/12/26 19:15
     * @author porkchop
     */
    EasyUIDataGridResult getItemList(int page, int rows);

}
