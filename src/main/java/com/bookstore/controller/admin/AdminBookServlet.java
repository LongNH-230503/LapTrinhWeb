package com.bookstore.controller.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.BookDAO;
import com.bookstore.dao.CategoryDAO;
import com.bookstore.model.Book;
import com.bookstore.model.Category;

@WebServlet("/admin/book")
public class AdminBookServlet extends HttpServlet {

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

        int page = 1;
        int pageSize = 7;

        String pageParam = request.getParameter("page");
        String search = request.getParameter("search");

        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }
        if (search == null) {
            search = "";
        }
        // total books
        int totalBooks = bookDAO.countSearchBooks(search);

        // total categories
        int totalCategories = categoryDAO.getTotalCategories();

        // pagination
        int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

        List<Book> books = bookDAO.searchBooksByPage(search, page, pageSize);

        // load categories
        List<Category> categories = categoryDAO.getAllCategories();

        // send data to JSP
        request.setAttribute("books", books);
        request.setAttribute("categories", categories);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalBooks", totalBooks);
        request.setAttribute("totalCategories", totalCategories);

        request.getRequestDispatcher("/views/admin-book.jsp").forward(request, response);
    }
}