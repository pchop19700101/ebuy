package cn.porkchop.ebuy.controller;

import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchItemController {
    @Autowired
    private SearchItemService searchItemService;
    /**
     * 导入商品到索引库
     * @date 2018/1/12 19:58
     * @author porkchop
     */
    @ResponseBody
    @RequestMapping("/index/item/import")
    private E3Result importSearchItems(){
        return searchItemService.importSearchItems();
    }
}
