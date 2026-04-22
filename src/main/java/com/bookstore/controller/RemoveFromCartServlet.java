package com.bookstore.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookstore.model.CartItem;

@WebServlet("/remove-from-cart")
public class RemoveFromCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        int bookId = Integer.parseInt(request.getParameter("id"));

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart != null) {
            cart.removeIf(item -> item.getBook().getBookId() == bookId);
        }

        session.setAttribute("cart", cart);

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}