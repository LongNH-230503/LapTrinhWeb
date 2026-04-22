package com.bookstore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.bookstore.model.Category;
import com.bookstore.util.DBContext;

public class CategoryDAO {

    public List<Category> getAllCategories() {

        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Category";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Category c = new Category();

                c.setCategoryId(rs.getInt("category_id"));
                c.setCategoryName(rs.getString("category_name"));

                list.add(c);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getTotalCategories() {
        String sql = "SELECT COUNT(*) FROM Category";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}