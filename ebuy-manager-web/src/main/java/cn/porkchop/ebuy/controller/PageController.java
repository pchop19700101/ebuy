package cn.porkchop.ebuy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 页面跳转的controller
 * @date 2017/12/26 19:35
 * @author porkchop
 */
@Controller
public class PageController {
    @RequestMapping("/")
    private String showIndex(){
        return "index";
    }

    @RequestMapping("/{page}")
    private String showPage(String page){
        return page;
    }
}
