package com.bookstore.controller.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.OrderItemsDAO;
import com.bookstore.dao.OrdersDAO;
import com.bookstore.model.OrderItems;
import com.bookstore.model.Orders;

@WebServlet("/admin/order/detail")
public class AdminOrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int orderId = Integer.parseInt(request.getParameter("id"));

        OrdersDAO orderDAO = new OrdersDAO();
        OrderItemsDAO itemDAO = new OrderItemsDAO();

        Orders order = orderDAO.getOrderById(orderId);
        List<OrderItems> items = itemDAO.getItemsByOrderId(orderId);

        request.setAttribute("order", order);
        request.setAttribute("items", items);

        request.getRequestDispatcher("/views/admin-order-detail.jsp")
                .forward(request, response);
    }
}