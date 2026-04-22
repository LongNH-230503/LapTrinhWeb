package com.bookstore.controller.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.BookDAO;

@WebServlet("/admin/book/update-stock")
public class AdminBookUpdateStockServlet extends HttpServlet {

    private BookDAO bookDAO;

    @Override
    public void init() {
        bookDAO = new BookDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String action = request.getParameter("action");

            bookDAO.updateStock(bookId, quantity, action);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redirect lại trang book
        response.sendRedirect(request.getContextPath() + "/admin/book");
    }
}