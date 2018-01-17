package cn.porkchop.ebuy.search.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalExceptionReslover implements HandlerExceptionResolver {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionReslover.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //记录日志
        logger.error("系统发生异常",ex);
        //发邮件.发短信等等
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message","系统异常,请稍后重试");
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}
