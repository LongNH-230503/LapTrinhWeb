package com.bookstore.controller;

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
import com.bookstore.model.Users;

@WebServlet("/order-detail")
public class OrderDetailsServlet extends HttpServlet {

    private OrdersDAO orderDAO;
    private OrderItemsDAO itemDAO;

    @Override
    public void init() {
        orderDAO = new OrdersDAO();
        itemDAO = new OrderItemsDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Users user = (Users) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect("order-history");
            return;
        }

        try {
            int orderId = Integer.parseInt(idParam);

            Orders order = orderDAO.getOrderById(orderId);
            List<OrderItems> items = itemDAO.getItemsByOrderId(orderId);

            double totalAmount = 0;
            for (OrderItems item : items) {
                totalAmount += item.getUnitPrice() * item.getQuantity();
            }

            request.setAttribute("order", order);
            request.setAttribute("items", items);
            request.setAttribute("orderId", orderId);
            request.setAttribute("totalAmount", totalAmount);

            request.getRequestDispatcher("/views/order-detail.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("order-history");
        }
    }
}