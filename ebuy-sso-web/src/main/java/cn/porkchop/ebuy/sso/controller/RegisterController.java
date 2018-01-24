package cn.porkchop.ebuy.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterController {

    @RequestMapping("/page/register")
    public String showRegistePage(){
        return "register";

    }
}
