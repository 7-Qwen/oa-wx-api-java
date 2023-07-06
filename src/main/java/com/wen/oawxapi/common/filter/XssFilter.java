package com.wen.oawxapi.common.filter;

import com.wen.oawxapi.common.config.XssRequestHandler;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author: 7wen
 * @Date: 2023-05-19 15:30
 * @description: 过滤所有请求以完成对HttpRequestServlet的装饰
 */
@WebFilter(urlPatterns = "/*")
public class XssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        XssRequestHandler xssRequestHandler = new XssRequestHandler(httpServletRequest);
        filterChain.doFilter(xssRequestHandler, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
