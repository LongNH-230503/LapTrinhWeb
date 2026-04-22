package com.bookstore.controller.admin;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookstore.model.Users;

// Chặn tất cả các đường dẫn bắt đầu bằng /admin/
@WebFilter("/admin/*") 
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // 1. Kiểm tra session có tồn tại không
        Users user = (session != null) ? (Users) session.getAttribute("user") : null;

        // 2. Kiểm tra quyền Admin (Giả sử trong model User bạn có field role)
        if (user != null && user.getRoleId() == 1) {
            // roleId == 1 nghĩa là Admin
            // Nếu đúng là Admin, cho phép truy cập tiếp
            chain.doFilter(request, response);
        } else {
            // Nếu không phải Admin hoặc chưa đăng nhập, đá về trang login hoặc báo lỗi 403
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?error=access-denied");
        }
    }
}