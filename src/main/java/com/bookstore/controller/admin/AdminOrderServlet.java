package com.bookstore.controller.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.OrdersDAO;
import com.bookstore.model.Orders;

@WebServlet("/admin/order")
public class AdminOrderServlet extends HttpServlet {

    private OrdersDAO ordersDAO;

    @Override
    public void init() {
        ordersDAO = new OrdersDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int page = 1;
        int pageSize = 10;

        String pageParam = req.getParameter("page");
        String search = req.getParameter("search");

        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        if (search == null) {
            search = "";
        }
        // Lấy tổng số orders (có thể theo search)
        int totalOrders = ordersDAO.countSearchOrders(search);

        int totalPages = (int) Math.ceil((double) totalOrders / pageSize);

        // Lấy danh sách orders theo trang
        List<Orders> orders = ordersDAO.searchOrdersByPage(search, page, pageSize);

        // Đếm đơn Pending
        int pendingCount = 0;
        for (Orders order : orders) {
            if ("Pending".equalsIgnoreCase(order.getStatus()))
                pendingCount++;
        }

        req.setAttribute("orders", orders);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("pendingOrders", pendingCount);
        req.setAttribute("search", search);

        req.getRequestDispatcher("/views/admin-order.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String orderIdParam = req.getParameter("orderId");
        String newStatus = req.getParameter("status");

        if (orderIdParam == null || newStatus == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/order");
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(orderIdParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/order");
            return;
        }

        Orders order = ordersDAO.getOrderById(orderId);
        if (order == null) {
            req.getSession().setAttribute("error", "Order không tồn tại");
            resp.sendRedirect(req.getContextPath() + "/admin/order");
            return;
        }

        // chỉ Pending mới được update
        if (!"Pending".equalsIgnoreCase(order.getStatus())) {
            req.getSession().setAttribute("error", "Chỉ đơn Pending mới được cập nhật");
            resp.sendRedirect(req.getContextPath() + "/admin/order");
            return;
        }

        // update
        ordersDAO.updateOrderStatus(orderId, newStatus);

        req.getSession().setAttribute("success", "Cập nhật trạng thái thành công");
        resp.sendRedirect(req.getContextPath() + "/admin/order");
    }
}