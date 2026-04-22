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

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {

    private UsersDAO userDAO;

    @Override
    public void init() {
        userDAO = new UsersDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/views/change-password.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        if(user == null){
            response.sendRedirect("login");
            return;
        }

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if(!userDAO.checkPassword(user.getUserId(), currentPassword)){
            request.setAttribute("error", "Current password incorrect");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request,response);
            return;
        }

        if(!newPassword.equals(confirmPassword)){
            request.setAttribute("error", "Password confirmation does not match");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request,response);
            return;
        }

        userDAO.updatePassword(user.getUserId(), newPassword);

        session.setAttribute("successMessage","Password updated successfully");

        response.sendRedirect("home");
    }
}