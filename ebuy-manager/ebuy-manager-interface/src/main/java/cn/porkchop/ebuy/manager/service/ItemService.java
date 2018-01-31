package cn.porkchop.ebuy.manager.service;

import cn.porkchop.ebuy.pojo.*;

public interface ItemService {
    /**
     * 根据商品id获得商品信息
     * @return 未查到就返回null
     * @date 2017/12/26 19:15
     * @author porkchop
     */
    TbItem getItemById(Long id);

    /**
     * 分页显示商品列表
     *
     * @date 2017/12/26 19:15
     * @author porkchop
     */
    EasyUIDataGridResult getItemList(int page, int rows);

    /**
     * 添加商品,并发送添加商品的消息,内容是商品的id
     *
     * @date 2017/12/29 20:18
     * @author porkchop
     */
    E3Result addItem(TbItem tbItem, String desc);
    /**
     * 根据id查询商品描述
     * @date 2018/1/22 21:18
     * @author porkchop
     */
    TbItemDesc getItemDescByItemId(long id);
}
