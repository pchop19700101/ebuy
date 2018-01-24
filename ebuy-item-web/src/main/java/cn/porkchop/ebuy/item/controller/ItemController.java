package cn.porkchop.ebuy.item.controller;

import cn.porkchop.ebuy.pojo.Item;
import cn.porkchop.ebuy.pojo.TbItem;
import cn.porkchop.ebuy.pojo.TbItemDesc;
import cn.porkchop.ebuy.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable long itemId,Model model){

        TbItemDesc tbItemDesc = itemService.getItemDescByItemId(itemId);
        Item item = new Item(itemService.getItemById(itemId));
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",tbItemDesc);
        return "item";
    }
}
