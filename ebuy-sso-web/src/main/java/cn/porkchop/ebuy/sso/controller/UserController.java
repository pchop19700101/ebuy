package cn.porkchop.ebuy.sso.controller;

import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.TbUser;
import cn.porkchop.ebuy.sso.service.UserService;
import cn.porkchop.ebuy.utils.JsonUtils;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Value("${USER_SESSION_EXPIRE_SECOND}")
    private int USER_SESSION_EXPIRE_SECOND;

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkRegisterForm(@PathVariable String param, @PathVariable int type) {
        return userService.checkRegisterForm(param, type);
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public E3Result register(TbUser user) {
        return userService.createUser(user);
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public E3Result login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        E3Result e3Result = userService.login(username, password);
        String domain = getDomain(request);
        Cookie cookie = new Cookie("session", (String) e3Result.getData());
        //设置共享cookie的域名
        if (!"localhost".equals(domain)) {
            cookie.setDomain(domain.substring(domain.indexOf(".")));
        }
        cookie.setPath("/");
        cookie.setMaxAge(USER_SESSION_EXPIRE_SECOND);
        response.addCookie(cookie);
        return e3Result;
    }

    /**
     * 获得访问的域名,不包含端口
     *
     * @return 域名
     * @date 2018/1/27 21:12
     * @author porkchop
     */
    private String getDomain(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String temp = url.substring(url.indexOf(":") + 3);
        String domainWithPort = temp.substring(0, temp.indexOf("/"));
        return !domainWithPort.contains(":") ? domainWithPort : domainWithPort.substring(0, domainWithPort.indexOf(":"));
    }

    /**
     * 获得用户信息,支持jsonp访问
     *
     * @date 2018/1/27 22:47
     * @author porkchop
     */
    @RequestMapping(value = "/user/token/{token}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
    @ResponseBody
    public String getUserByToken(@PathVariable String token, String callback) {
        E3Result result = userService.getUserByToken(token);
        if (StringUtils.isNotBlank(callback)) {
            return callback + "(" + JsonUtils.objectToJson(result) + ")";
        }
        return JsonUtils.objectToJson(result);
    }
}
