package cn.porkchop.ebuy.controller;

import cn.porkchop.ebuy.content.service.ContentCategoryService;
import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.EasyUITreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 根据父节点的id得到商品分类信息<br>
     *
     * @param parentId 父节点id,请求过来的参数名是id
     * @return json<br>[ { "id": 1, "text": "Node 1", "state": "closed" }, { "id": 2, "text": "Node 2", "state": "closed" } ]
     * @date 2018/1/5 18:51
     * @author porkchop
     */
    @RequestMapping("/content/category/list")
    @ResponseBody
    private List<EasyUITreeNode> getContentCategoryList(@RequestParam(value = "id",defaultValue = "0") long parentId) {
        return contentCategoryService.getContentCategoryList(parentId);
    }

    /**
     * 添加类别
     * @date 2018/1/5 20:02
     * @author porkchop
     * @param name 类别名称
     */
    @RequestMapping("/content/category/create")
    @ResponseBody
    private E3Result createContentCategory(long parentId,String name){
        return contentCategoryService.addContentCategory(parentId,name);
    }
}
