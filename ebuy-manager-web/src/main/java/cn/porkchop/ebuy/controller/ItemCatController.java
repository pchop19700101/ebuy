package cn.porkchop.ebuy.controller;

import cn.porkchop.ebuy.pojo.EasyUITreeNode;
import cn.porkchop.ebuy.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商品类别的controller
 *
 * @author porkchop
 * @date 2017/12/26 19:35
 */
@Controller
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    /**
     * 根据父节点的id得到商品分类信息<br>
     * 初始化tree请求的url：/item/cat/list<br>
     *
     * @param parentId 父节点id,请求过来的参数名是id
     * @return json<br>[ { "id": 1, "text": "Node 1", "state": "closed" }, { "id": 2, "text": "Node 2", "state": "closed" } ]
     *
     * @date 2017/12/26 19:44
     * @author porkchop
     */
    @RequestMapping("/item/cat/list")
    @ResponseBody
    private List<EasyUITreeNode> getItemCatList(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        return itemCatService.getCatList(parentId);
    }
}
