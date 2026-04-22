package com.bookstore.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookstore.dao.BookDAO;
import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.Users;

@WebServlet("/add-to-cart")
public class AddToCartServlet extends HttpServlet {

    private BookDAO bookDAO;

    @Override
    public void init() {
        bookDAO = new BookDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // kiểm tra đăng nhập
        Users user = (Users) session.getAttribute("user");

        if (user == null || user.getRoleId() != 2) {

            session.setAttribute("loginMessage", "Please login to add items to cart.");

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int bookId = Integer.parseInt(request.getParameter("id"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        // Lấy book từ database
        Book book = bookDAO.getBookById(bookId);

        if (book == null) {
            response.sendRedirect("/home");
            return;
        }

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        boolean exists = false;

        for (CartItem item : cart) {

            if (item.getBook().getBookId() == bookId) {

                item.setQuantity(item.getQuantity() + quantity);
                exists = true;
                break;

            }
        }

        if (!exists) {

            CartItem newItem = new CartItem(book, quantity);
            cart.add(newItem);

        }

        session.setAttribute("cart", cart);
        session.setAttribute("successMessage", "Book added to cart!");

        String returnUrl = request.getParameter("returnUrl");

        if (returnUrl == null || returnUrl.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            response.sendRedirect(request.getContextPath() + returnUrl);
        }
        
    }
}