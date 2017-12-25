package cn.porkchop.ebuy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
