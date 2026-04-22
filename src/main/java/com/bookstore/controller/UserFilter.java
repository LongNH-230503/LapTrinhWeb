package com.bookstore.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.model.Users;

@WebFilter(urlPatterns = {"/order-history", "/cart", "/checkout", "/change-password", "/cancel-order"})
public class UserFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        Users user = (Users) req.getSession().getAttribute("user");

        if (user == null || user.getRoleId() != 2) {
            // Nếu chưa đăng nhập, đá về trang login
            res.sendRedirect(req.getContextPath() + "/login");
        } else {
            // Đã đăng nhập, cho phép đi tiếp
            chain.doFilter(request, response);
        }
    }
}