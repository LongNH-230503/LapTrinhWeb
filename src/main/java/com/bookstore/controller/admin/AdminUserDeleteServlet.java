package com.bookstore.controller.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.UsersDAO;

@WebServlet("/admin/user/delete")
public class AdminUserDeleteServlet extends HttpServlet {

    private UsersDAO userDAO;

    @Override
    public void init() {
        userDAO = new UsersDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            userDAO.deleteUser(id);
            request.getSession().setAttribute("success", "Xóa người dùng thành công");
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/admin/user");
    }
}