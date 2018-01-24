package cn.porkchop.ebuy.mapper;

import cn.porkchop.ebuy.pojo.SearchItem;

import java.util.List;

public interface SearchItemMapper {
    /**
     * 获得所有商品
     * @return 商品集合
     */
    List<SearchItem> getAllItem();

    /**
     * @param id 商品的id
     * @return 搜索对象
     */
    SearchItem getItemById(long id);
}
