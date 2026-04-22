package com.bookstore.controller;

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

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

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

        String categoryIdParam = request.getParameter("categoryId");
        String keyword = request.getParameter("keyword");
        String pageParam = request.getParameter("page");

        int page = 1;
        int pageSize = 10;

        if (pageParam != null && !pageParam.isEmpty()) {
            page = Integer.parseInt(pageParam);
        }

        List<Category> categories = categoryDAO.getAllCategories();
        List<Book> books;
        String activeCategoryName = "All Books";

        if (keyword != null && !keyword.trim().isEmpty()) {

            books = bookDAO.searchBooksByPage(keyword, page, pageSize);
            int totalBooks = bookDAO.countSearchBooks(keyword);

            int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

            request.setAttribute("keyword", keyword);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalBooks", totalBooks);
            activeCategoryName = "Results for: " + keyword;

        } else if (categoryIdParam != null && !categoryIdParam.isEmpty()) {

            int categoryId = Integer.parseInt(categoryIdParam);

            request.setAttribute("activeCategoryId", categoryId);

            books = bookDAO.getBooksByCategoryPage(categoryId, page, pageSize);
            int totalBooks = bookDAO.countBooksByCategory(categoryId);

            int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

            request.setAttribute("categoryId", categoryId);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalBooks", totalBooks);

            for (Category c : categories) {
                if (c.getCategoryId() == categoryId) {
                    activeCategoryName = c.getCategoryName();
                    break;
                }
            }
        } else {

            books = bookDAO.getBooksByPage(page, pageSize);

            int totalBooks = bookDAO.getTotalBooks();

            int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalBooks", totalBooks);
        }

        request.setAttribute("currentPage", page);
        request.setAttribute("books", books);
        request.setAttribute("categories", categories);
        request.setAttribute("activeCategoryName", activeCategoryName);

        request.getRequestDispatcher("/views/home.jsp")
                .forward(request, response);
    }
}