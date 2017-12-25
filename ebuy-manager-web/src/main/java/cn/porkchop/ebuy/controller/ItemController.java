package cn.porkchop.ebuy.controller;

import cn.porkchop.ebuy.pojo.EasyUIDataGridResult;
import cn.porkchop.ebuy.pojo.TbItem;
import cn.porkchop.ebuy.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     * 获得单个商品的json数据
     * @date 2017/12/25 16:30
     * @author porkchop
     */
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    private TbItem getItemById(@PathVariable Long itemId){

        return itemService.getItemById(itemId);
    }

    /**
     * 后台获得商品列表,并且是分页的
     *
     * 请求的url：/item/list <br>
     * 请求的参数：page=1&rows=30<br>
     * 响应的json数据格式：<br>
     * Easyui中datagrid控件要求的数据格式为：total:”2”,rows:[{“id”:”1”,”name”:”张三”},{“id”:”2”,”name”:”李四”}]}
     * @date 2017/12/25 16:30
     * @author porkchop
     */
    @RequestMapping("/item/list")
    @ResponseBody
    private EasyUIDataGridResult getItemList(int page, int rows){
        EasyUIDataGridResult easyUIDataGridResult = itemService.getItemList(page, rows);
        return easyUIDataGridResult;
    }


}
