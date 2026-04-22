package com.bookstore.controller.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.BookDAO;
import com.bookstore.dao.OrdersDAO;
import com.bookstore.dao.UsersDAO;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UsersDAO userDao = new UsersDAO();
        BookDAO bookDao = new BookDAO();
        OrdersDAO orderDao = new OrdersDAO();

        request.setAttribute("totalOrders", orderDao.getTotalOrders());
        request.setAttribute("totalRevenue", orderDao.getTotalRevenue());
        request.setAttribute("totalUsers", userDao.getTotalUsers());
        request.setAttribute("totalBooks", bookDao.getTotalBooks());
        request.setAttribute("recentOrders", orderDao.getRecentOrders());
        request.setAttribute("revenue7Days", orderDao.getRevenueLast7Days());
        request.setAttribute("orderStatusStats", orderDao.getOrderStatusStats());

        request.getRequestDispatcher("/views/admin-dashboard.jsp").forward(request, response);
    }
}