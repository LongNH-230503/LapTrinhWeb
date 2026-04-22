package com.bookstore.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.UsersDAO;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/views/register.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // validate
        if (name == null || email == null || password == null
                || name.isEmpty() || email.isEmpty() || password.isEmpty()) {

            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu không khớp!");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        UsersDAO usersDAO = new UsersDAO();

        // check email tồn tại
        if (usersDAO.checkEmailExist(email)) {
            request.setAttribute("error", "Email đã tồn tại!");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        // insert user
        usersDAO.register(name, email, password);

        request.getSession().setAttribute("success",
                "Đăng ký thành công! Vui lòng đăng nhập.");

        response.sendRedirect(request.getContextPath() + "/login");
    }
}