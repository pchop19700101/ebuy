package cn.porkchop.ebuy.order.interceptor;

import cn.porkchop.ebuy.pojo.TbUser;
import cn.porkchop.ebuy.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;
    @Value("${SSO_URL}")
    private String SSO_URL;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("session")) {
                String session = cookie.getValue();
                Object data = userService.getUserByToken(session).getData();
                if (data != null) {

                    //保存到request域
                    request.setAttribute("user",data);
                    return true;
                }
                //跳转到登陆页面
                response.sendRedirect(SSO_URL + "/page/login");
                //从cookie中拿到的token已经过期
                return false;
            }
        }
        //跳转到登陆页面
        response.sendRedirect(SSO_URL + "/page/login");
        //未登录
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
