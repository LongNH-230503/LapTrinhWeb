package com.bookstore.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.BookDAO;
import com.bookstore.dao.CategoryDAO;
import com.bookstore.model.Book;

@WebServlet("/book-detail")
public class BookDetailServlet extends HttpServlet {

    private BookDAO bookDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        bookDAO = new BookDAO();
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect("home");
            return;
        }

        int id = Integer.parseInt(idParam);

        Book book = bookDAO.getBookById(id);

        if (book == null) {
            response.sendRedirect("home");
            return;
        }

        request.setAttribute("book", book);

        request.getRequestDispatcher("/views/book-detail.jsp")
               .forward(request, response);
    }
}