package cn.porkchop.ebuy.controller;

import cn.porkchop.ebuy.content.service.ContentService;
import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;
    /**
     * 添加内容
     * @date 2018/1/5 23:22
     * @author porkchop
     */
    @RequestMapping("/content/save")
    @ResponseBody
    private E3Result addContent(TbContent content){
        return contentService.addContent(content);
    }
}
