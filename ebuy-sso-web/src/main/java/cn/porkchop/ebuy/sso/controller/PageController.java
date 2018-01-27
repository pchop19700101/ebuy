package cn.porkchop.ebuy.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 负责跳转到jsp
 * @date 2018/1/27 13:46
 * @author porkchop
 */
@Controller
public class PageController {

    @RequestMapping("/page/register")
    public String showRegistePage(){
        return "register";

    }
    @RequestMapping("/page/login")
    public String showLoginPage(){
        return "login";
    }
}
