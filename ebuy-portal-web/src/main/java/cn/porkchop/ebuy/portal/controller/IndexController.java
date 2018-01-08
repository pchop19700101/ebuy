package cn.porkchop.ebuy.portal.controller;

import cn.porkchop.ebuy.content.service.ContentService;
import cn.porkchop.ebuy.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class IndexController {
    @Value("${CONTENT_CAROUSEL_ID}")
    private long CONTENT_CAROUSEL_ID;

    @Autowired
    private ContentService contentService;

    @RequestMapping("/index")
    private String showIndex(Model model){
        List<TbContent> carouselList = contentService.getContentListByCategoryId(CONTENT_CAROUSEL_ID);
        model.addAttribute("carouselList",carouselList);
        return "index";
    }
}
