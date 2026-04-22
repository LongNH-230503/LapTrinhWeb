package com.bookstore.controller.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.BookDAO;
import com.bookstore.dao.CategoryDAO;
import com.bookstore.model.Book;

@WebServlet("/admin/book/add-edit-book")
public class AdminBookAddEditServlet extends HttpServlet {

    private BookDAO bookDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        bookDAO = new BookDAO();
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        // Nếu có id → là Edit
        if (id != null) {
            try {
                int bookId = Integer.parseInt(id);
                Book book = bookDAO.getBookById(bookId);
                
                if (book != null) {
                    request.setAttribute("mode", "edit");
                    request.setAttribute("book", book);
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/book");
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/book");
                return;
            }
        } else {
            request.setAttribute("mode", "add");
        }

        // Load categories cho dropdown
        request.setAttribute("categories", categoryDAO.getAllCategories());

        request.getRequestDispatcher("/views/admin-book.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setCharacterEncoding("UTF-8");

            String bookIdStr = request.getParameter("bookId");
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String categoryIdStr = request.getParameter("categoryId");
            String priceStr = request.getParameter("price");
            String isbn = request.getParameter("isbn");
            String imageUrl = request.getParameter("imageUrl");
            String description = request.getParameter("description");

            // Validate input
            if (title == null || title.trim().isEmpty() ||
                author == null || author.trim().isEmpty() ||
                categoryIdStr == null || categoryIdStr.trim().isEmpty() ||
                priceStr == null || priceStr.trim().isEmpty() ||
                isbn == null || isbn.trim().isEmpty() ||
                description == null || description.trim().isEmpty()) {
                
                request.setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc");
                doGet(request, response);
                return;
            }

            Book book = new Book();
            book.setTitle(title.trim());
            book.setAuthor(author.trim());
            book.setCategoryId(Integer.parseInt(categoryIdStr));
            book.setPrice(Double.parseDouble(priceStr));
            book.setIsbn(isbn);
            book.setImageUrl(imageUrl != null ? imageUrl.trim() : "");
            book.setDescription(description);

            if (bookIdStr != null && !bookIdStr.isEmpty()) {
                // Update existing book
                book.setBookId(Integer.parseInt(bookIdStr));
                bookDAO.updateBook(book);
            } else {
                // Add new book
                bookDAO.addBook(book);
            }

            response.sendRedirect(request.getContextPath() + "/admin/book");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ");
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            doGet(request, response);
        }
    }
}