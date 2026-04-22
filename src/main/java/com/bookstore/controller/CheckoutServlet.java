package com.bookstore.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookstore.dao.BookDAO;
import com.bookstore.dao.OrderItemsDAO;
import com.bookstore.dao.OrdersDAO;
import com.bookstore.model.CartItem;
import com.bookstore.model.OrderItems;
import com.bookstore.model.Orders;
import com.bookstore.model.Users;
import com.bookstore.util.DBContext;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private OrdersDAO ordersDAO;
    private OrderItemsDAO orderItemsDAO;
    private BookDAO bookDAO;

    @Override
    public void init() {
        ordersDAO = new OrdersDAO();
        orderItemsDAO = new OrderItemsDAO();
        bookDAO = new BookDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Users user = (Users) request.getSession().getAttribute("user");

        if (user == null || user.getRoleId() != 2) {
            response.sendRedirect("login");
            return;
        }

        request.getRequestDispatcher("/views/checkout.jsp")
                .forward(request, response);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        Users user = (Users) session.getAttribute("user");

        if (user == null || user.getRoleId() != 2) {
            response.sendRedirect("login");
            return;
        }

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("cart");
            return;
        }

        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String phone = request.getParameter("phone");
        System.out.println("city=" + city + " | state=" + state + " | address=" + address + " | phone=" + phone);

        if (address == null || address.trim().isEmpty() ||
                city == null || city.trim().isEmpty() ||
                state == null || state.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty()) {

            response.sendRedirect("checkout?error=missing_info");
            return;
        }

        address += (", " + state + ", " + city);

        // Tính tổng tiền
        double total = 0;
        for (CartItem item : cart) {
            total += item.getTotal();
        }

        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Tạo đơn hàng
            Orders order = new Orders();
            order.setTotalAmount(total);
            order.setUserId(user.getUserId());
            order.setStatus("Pending");
            order.setAddress(address);
            order.setPhone(phone);

            int orderId = ordersDAO.createOrder(order, conn);
            if (orderId == -1)
                throw new Exception("Failed to create order");

            // 2. Xử lý từng item
            for (CartItem item : cart) {
                int bookId = item.getBook().getBookId();
                int qty = item.getQuantity();

                if (!bookDAO.decreaseStock(bookId, qty, conn)) {
                    throw new Exception("Not enough stock for book: " + item.getBook().getTitle());
                }

                OrderItems orderItem = new OrderItems();
                orderItem.setOrderId(orderId);
                orderItem.setBookId(bookId);
                orderItem.setQuantity(qty);
                orderItem.setUnitPrice(item.getBook().getPrice());

                orderItemsDAO.addOrderItem(orderItem, conn);
            }

            conn.commit(); // Thành công hết thì mới lưu vào DB
            session.removeAttribute("cart");
            response.sendRedirect("order-history?success=order");

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            response.sendRedirect("cart?error=insufficient_stock");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}