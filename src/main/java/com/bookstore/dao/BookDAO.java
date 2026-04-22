package com.bookstore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookstore.model.Book;
import com.bookstore.util.DBContext;

public class BookDAO {

    // ================= GET BY ID =================
    public Book getBookById(int id) {

        String sql = "SELECT b.*, c.category_name " +
                "FROM Book b " +
                "JOIN Category c ON b.category_id = c.category_id " +
                "WHERE b.book_id = ? AND b.status = 1";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= ADD =================
    public void addBook(Book book) {

        String sql = "INSERT INTO Book " +
                "(isbn, title, author, description, price, stock, image_url, category_id, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getDescription());
            ps.setDouble(5, book.getPrice());
            ps.setInt(6, book.getStock());
            ps.setString(7, book.getImageUrl());
            ps.setInt(8, book.getCategoryId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= UPDATE =================
    public void updateBook(Book book) {

        String sql = "UPDATE Book SET " +
                "isbn=?, title=?, author=?, description=?, price=?, image_url=?, category_id=? " +
                "WHERE book_id=? AND status = 1";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getDescription());
            ps.setDouble(5, book.getPrice());
            ps.setString(6, book.getImageUrl());
            ps.setInt(7, book.getCategoryId());
            ps.setInt(8, book.getBookId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE =================
    public void deleteBook(int id) {

        String sql = "UPDATE Book SET status = 0 WHERE book_id=? AND status = 1";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= MAP RESULT =================
    private Book mapResultSet(ResultSet rs) throws Exception {

        Book book = new Book();

        book.setBookId(rs.getInt("book_id"));
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setDescription(rs.getString("description"));
        book.setPrice(rs.getDouble("price"));
        book.setStock(rs.getInt("stock"));
        book.setImageUrl(rs.getString("image_url"));
        book.setCategoryId(rs.getInt("category_id"));
        book.setStatus(rs.getInt("status"));

        // lấy trực tiếp từ JOIN
        book.setCategoryName(rs.getString("category_name"));

        return book;
    }

    // ================= GET BOOKS BY PAGE =================
    public List<Book> getBooksByPage(int page, int pageSize) {

        List<Book> list = new ArrayList<>();

        String sql = "SELECT b.*, c.category_name " +
                "FROM Book b " +
                "JOIN Category c ON b.category_id = c.category_id " +
                "WHERE b.status = 1 " +
                "ORDER BY b.book_id " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, (page - 1) * pageSize);
            ps.setInt(2, pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= GET TOTAL BOOKS =================
    public int getTotalBooks() {

        String sql = "SELECT COUNT(*) FROM Book WHERE status = 1";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= SEARCH BOOKS BY PAGE =================
    public List<Book> searchBooksByPage(String keyword, int page, int pageSize) {

        keyword = keyword == null ? "" : keyword.trim();
        boolean isISBN = keyword.matches("\\d{10}|\\d{13}");
        boolean isId = keyword.matches("\\d+") && !isISBN;

        List<Book> list = new ArrayList<>();

        String sql = "SELECT b.*, c.category_name " +
                "FROM Book b " +
                "JOIN Category c ON b.category_id = c.category_id " +
                "WHERE b.status = 1 AND (";

        if (isId)
            sql += "b.book_id = ? OR ";
        if (isISBN)
            sql += "b.isbn = ? OR ";

        sql += "b.title COLLATE Latin1_General_CI_AI LIKE ? OR " +
                "b.author COLLATE Latin1_General_CI_AI LIKE ?) " +
                "ORDER BY b.book_id " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            if (isId)
                ps.setInt(index++, Integer.parseInt(keyword));
            if (isISBN)
                ps.setString(index++, keyword);
            ps.setString(index++, "%" + keyword + "%");
            ps.setString(index++, "%" + keyword + "%");
            ps.setInt(index++, (page - 1) * pageSize);
            ps.setInt(index++, pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= COUNT SEARCH BOOKS =================
    public int countSearchBooks(String keyword) {

        keyword = keyword == null ? "" : keyword.trim();

        boolean isISBN = keyword.matches("\\d{10}|\\d{13}");
        boolean isId = keyword.matches("\\d+") && !isISBN;

        String sql = "SELECT COUNT(*) " +
                "FROM Book b " +
                "WHERE b.status = 1 AND (";

        if (isId)
            sql += "b.book_id = ? OR ";
        if (isISBN)
            sql += "b.isbn = ? OR ";

        sql += "b.title COLLATE Latin1_General_CI_AI LIKE ? OR " +
                "b.author COLLATE Latin1_General_CI_AI LIKE ?)";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;

            if (isId)
                ps.setInt(index++, Integer.parseInt(keyword));
            if (isISBN)
                ps.setString(index++, keyword);

            ps.setString(index++, "%" + keyword + "%");
            ps.setString(index++, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ================= GET BOOKS BY CATEGORY PAGE =================
    public List<Book> getBooksByCategoryPage(int categoryId, int page, int pageSize) {

        List<Book> list = new ArrayList<>();

        String sql = "SELECT b.*, c.category_name " +
                "FROM Book b " +
                "JOIN Category c ON b.category_id = c.category_id " +
                "WHERE b.category_id=? AND b.status = 1 " +
                "ORDER BY b.book_id " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ps.setInt(2, (page - 1) * pageSize);
            ps.setInt(3, pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= COUNT BOOKS BY CATEGORY =================
    public int countBooksByCategory(int categoryId) {

        String sql = "SELECT COUNT(*) FROM Book WHERE category_id = ? AND status = 1";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= DECREASE STOCK =================
    public boolean decreaseStock(int bookId, int quantity, Connection conn) throws SQLException {

        String sql = "UPDATE Book SET stock = stock - ? WHERE book_id = ? AND stock >= ? AND status = 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, bookId);
            ps.setInt(3, quantity);

            int rows = ps.executeUpdate();

            return rows > 0; // false = không đủ hàng
        }
    }

    // ================= UPDATE STOCK =================
    public void updateStock(int bookId, int quantity, String action) {
        String sqlAdd = "UPDATE Book SET stock = stock + ? WHERE book_id = ? AND status = 1";
        String sqlMinus = "UPDATE Book SET stock = CASE " +
                "WHEN stock - ? < 0 THEN 0 " +
                "ELSE stock - ? END " +
                "WHERE book_id = ? AND status = 1";

        try (Connection conn = DBContext.getConnection()) {

            PreparedStatement ps;

            if ("add".equals(action)) {
                ps = conn.prepareStatement(sqlAdd);
                ps.setInt(1, quantity);
                ps.setInt(2, bookId);

            } else if ("minus".equals(action)) {
                ps = conn.prepareStatement(sqlMinus);
                ps.setInt(1, quantity);
                ps.setInt(2, quantity);
                ps.setInt(3, bookId);
            } else {
                return; // action sai → bỏ
            }

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}