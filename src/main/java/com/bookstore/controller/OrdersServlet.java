package com.bookstore.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.OrdersDAO;
import com.bookstore.model.Orders;
import com.bookstore.model.Users;

@WebServlet("/order-history")
public class OrdersServlet extends HttpServlet {

    private OrdersDAO orderDAO;

    @Override
    public void init() {
        orderDAO = new OrdersDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Users user = (Users) request.getSession().getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        int page = 1;
        int recordsPerPage = 5;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int start = (page - 1) * recordsPerPage;

        List<Orders> orders = orderDAO.getOrdersByUserPaging(
                user.getUserId(), start, recordsPerPage);

        int totalOrders = orderDAO.countOrdersByUser(user.getUserId());
        int totalPages = (int) Math.ceil((double) totalOrders / recordsPerPage);

        request.setAttribute("orders", orders);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/views/order-history.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Users user = (Users) request.getSession().getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String orderIdParam = request.getParameter("orderId");
        String action = request.getParameter("action");
        String page = request.getParameter("page");

        if (orderIdParam != null && "cancel".equals(action)) {
            try {
                int orderId = Integer.parseInt(orderIdParam);

                // Kiểm tra xem order có thuộc user không và có phải là Pending không
                Orders order = orderDAO.getOrderById(orderId);
                if (order != null && order.getUserId() == user.getUserId()
                        && "Pending".equals(order.getStatus())) {

                    // Cập nhật trạng thái thành Cancelled
                    orderDAO.updateOrderStatus(orderId, "Cancelled");

                    request.getSession().setAttribute("success", "cancelled");
                    response.sendRedirect("order-history?page=" + page);
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("order-history");
            }
        }

        response.sendRedirect("order-history");
    }
}