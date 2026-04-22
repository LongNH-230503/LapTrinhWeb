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

@WebServlet("/update-cart")
public class UpdateCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        int bookId = Integer.parseInt(request.getParameter("id"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart != null) {
            CartItem itemToRemove = null;

            for (CartItem item : cart) {
                if (item.getBook().getBookId() == bookId) {
                    if (quantity <= 0) {
                        itemToRemove = item;
                    } else {
                        item.setQuantity(quantity);
                    }
                    break;
                }
            }

            if (itemToRemove != null) {
                cart.remove(itemToRemove);
            }
        }

        session.setAttribute("cart", cart);

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}