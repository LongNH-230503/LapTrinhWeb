package com.bookstore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookstore.model.OrderItems;
import com.bookstore.util.DBContext;

public class OrderItemsDAO {

    // ================= ADD ORDER ITEM =================
    public void addOrderItem(OrderItems item, Connection conn) throws SQLException {

        String sql = "INSERT INTO OrderItems(order_id, book_id, quantity, unit_price) VALUES(?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getBookId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getUnitPrice());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // ================= GET ITEMS BY ORDER =================
    public List<OrderItems> getItemsByOrderId(int orderId) {

        List<OrderItems> list = new ArrayList<>();

        String sql = """
                SELECT oi.order_item_id,
                       oi.order_id,
                       oi.book_id,
                       oi.quantity,
                       oi.unit_price,
                       b.title,
                       b.image_url
                FROM OrderItems oi
                JOIN Book b ON oi.book_id = b.book_id
                WHERE oi.order_id = ?
                """;

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= MAP RESULT =================
    private OrderItems mapResultSet(ResultSet rs) throws Exception {
        OrderItems item = new OrderItems();
        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setBookId(rs.getInt("book_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getDouble("unit_price"));

        // Gán trực tiếp vì Model đã có sẵn 2 trường này
        item.setTitle(rs.getString("title"));
        item.setImageUrl(rs.getString("image_url"));

        return item;
    }
}