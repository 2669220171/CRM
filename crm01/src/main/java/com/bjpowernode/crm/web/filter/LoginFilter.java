package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    public LoginFilter() {
        super();
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        System.out.println("进入到验证有没有登录过的过滤器");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse)resp;

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String path = request.getServletPath();

        if ("/login.jsp".equals(path)||"/settings/user/login.do".equals(path)){

            chain.doFilter(req,resp);

        }else {

            if (user != null){

                chain.doFilter(req,resp);

            }else {

                response.sendRedirect(request.getContextPath()+"/login.jsp");

            }
        }



    }

    public void destroy() {

    }
}
