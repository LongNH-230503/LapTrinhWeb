package com.bookstore.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookstore.dao.UsersDAO;
import com.bookstore.model.Users;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UsersDAO usersDAO = new UsersDAO();
        Users user = usersDAO.login(email, password);

        if (user != null) {

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // role_id = 1 -> ADMIN
            if (user.getRoleId() == 1) {

                session.setAttribute("role", "ADMIN");
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");

            } else {

                session.setAttribute("role", "USER");
                response.sendRedirect(request.getContextPath() + "/home");
            }

        } else {
            HttpSession session = request.getSession();
            session.setAttribute("error", "Email hoặc mật khẩu không đúng");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}