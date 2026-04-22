package com.bookstore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bookstore.model.Orders;
import com.bookstore.util.DBContext;

public class OrdersDAO {

    // ================= CREATE ORDER =================
    public int createOrder(Orders order, Connection conn) throws SQLException {

        String sql = "INSERT INTO Orders(order_date,total_amount,user_id,status,address,phone) " +
                "VALUES(GETDATE(),?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDouble(1, order.getTotalAmount());
            ps.setInt(2, order.getUserId());
            ps.setString(3, order.getStatus());
            ps.setString(4, order.getAddress());
            ps.setString(5, order.getPhone());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return -1;
    }

    // ================= GET ORDER BY ID =================
    public Orders getOrderById(int id) {

        String sql = "SELECT o.*, u.name, o.phone " +
                "FROM Orders o " +
                "LEFT JOIN Users u ON o.user_id = u.user_id " +
                "WHERE o.order_id=?";

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

    // ================= GET ORDERS WITH PAGING =================
    public List<Orders> getOrdersByUserPaging(int userId, int start, int total) {
        List<Orders> list = new ArrayList<>();

        String sql = "SELECT * FROM Orders WHERE user_id = ? " +
                "ORDER BY order_date DESC " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, start);
            ps.setInt(3, total);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Orders o = new Orders();
                o.setOrderId(rs.getInt("order_id"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                o.setTotalAmount(rs.getDouble("total_amount"));
                o.setStatus(rs.getString("status"));
                o.setPhone(rs.getString("phone"));
                o.setAddress(rs.getString("address"));
                o.setUserId(rs.getInt("user_id"));

                list.add(o);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= COUNT ORDERS BY USER =================
    public int countOrdersByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM Orders WHERE user_id = ?";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= SEARCH ORDERS =================
    public List<Orders> searchOrdersByPage(String keyword, int page, int pageSize) {

        keyword = keyword == null ? "" : keyword.trim();

        List<Orders> list = new ArrayList<>();

        String sql = "SELECT o.*, u.name " +
                "FROM Orders o " +
                "JOIN Users u ON o.user_id = u.user_id " +
                "WHERE (" +
                (keyword.matches("\\d+") ? "o.order_id LIKE ? OR " : "") +
                "u.name COLLATE Latin1_General_CI_AI LIKE ? OR " +
                "o.phone LIKE ?" +
                ") " +
                "ORDER BY o.order_date DESC " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            if (keyword.matches("\\d+")) {
                ps.setInt(index++, Integer.parseInt(keyword));
            }
            ps.setString(index++, "%" + keyword + "%");
            ps.setString(index++, keyword);
            ps.setInt(index++, (page - 1) * pageSize);
            ps.setInt(index++, pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Orders b = mapResultSet(rs);
                list.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= COUNT SEARCH ORDERS =================
    public int countSearchOrders(String keyword) {

        keyword = keyword == null ? "" : keyword.trim();

        String sql = "SELECT COUNT(*) " +
                "FROM Orders o " +
                "JOIN Users u ON o.user_id = u.user_id " +
                "WHERE (" +
                (keyword.matches("\\d+") ? "o.order_id LIKE ? OR " : "") +
                "u.name COLLATE Latin1_General_CI_AI LIKE ? OR " +
                "o.phone LIKE ?" +
                ")";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            if (keyword.matches("\\d+")) {
                ps.setInt(index++, Integer.parseInt(keyword));
            }
            ps.setString(index++, "%" + keyword + "%");
            ps.setString(index++, keyword);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= UPDATE STATUS =================
    public void updateOrderStatus(int orderId, String status) {

        String sql = "UPDATE Orders SET status=? WHERE order_id=?";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, orderId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= GET TOTAL ORDERS =================
    public int getTotalOrders() {
        String sql = "SELECT COUNT(*) FROM Orders";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next())
                return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ================= GET TOTAL REVENUE =================
    public double getTotalRevenue() {
        String sql = "SELECT SUM(total_amount) FROM Orders WHERE status = 'Completed'";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next())
                return rs.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ================= GET RECENT ORDERS =================
    public List<Orders> getRecentOrders() {
        List<Orders> list = new ArrayList<>();

        String sql = "SELECT TOP 5 o.*, u.name " +
                "FROM Orders o JOIN Users u ON o.user_id = u.user_id " +
                "ORDER BY o.order_date DESC";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= GET REVENUE LAST 7 DAYS =================
    public List<Double> getRevenueLast7Days() {
        List<Double> list = new ArrayList<>();

        String sql = "SELECT CAST(order_date AS DATE) as d, SUM(total_amount) as total " +
                "FROM Orders " +
                "WHERE order_date >= DATEADD(DAY, -6, GETDATE()) " +
                "GROUP BY CAST(order_date AS DATE) " +
                "ORDER BY d";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getDouble("total"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= GET ORDER STATUS STATS =================
    public Map<String, Integer> getOrderStatusStats() {
        Map<String, Integer> map = new LinkedHashMap<>();

        String sql = "SELECT status, COUNT(*) as total FROM Orders GROUP BY status";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getString("status"), rs.getInt("total"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    // ================= MAP RESULT =================
    private Orders mapResultSet(ResultSet rs) throws Exception {
        Orders order = new Orders();

        order.setOrderId(rs.getInt("order_id"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setUserId(rs.getInt("user_id"));
        order.setStatus(rs.getString("status"));
        order.setAddress(rs.getString("address"));
        order.setPhone(rs.getString("phone"));

        // Lấy username từ câu lệnh JOIN (nếu có)
        try {
            order.setUsername(rs.getString("name"));
        } catch (Exception e) {
            order.setUsername("User " + order.getUserId());
        }

        return order;
    }
}