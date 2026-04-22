package com.bookstore.controller.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.BookDAO;

@WebServlet("/admin/book/delete")
public class AdminBookDeleteServlet extends HttpServlet {

    private BookDAO bookDAO;

    @Override
    public void init() {
        bookDAO = new BookDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String bookIdParam = request.getParameter("bookId");
        
        if (bookIdParam != null && !bookIdParam.isEmpty()) {
            try {
                int bookId = Integer.parseInt(bookIdParam);
                bookDAO.deleteBook(bookId);
                request.getSession().setAttribute("success", "Xóa sách thành công");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/book");
    }
}
