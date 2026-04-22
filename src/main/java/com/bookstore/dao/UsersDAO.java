package com.bookstore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.bookstore.model.Users;
import com.bookstore.util.DBContext;

public class UsersDAO {

    // ================= LOGIN =================
    public Users login(String email, String password) {

        String sql = "SELECT * FROM Users WHERE email=? AND status=1";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");

                if (BCrypt.checkpw(password, hashedPassword)) {
                    return mapResultSet(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= CHECK EMAIL EXIST =================
    public boolean checkEmailExist(String email) {

        String sql = "SELECT COUNT(*) FROM Users WHERE email=? AND status=1";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= REGISTER =================
    public void register(String name, String email, String password) {

        String sql = "INSERT INTO Users(name, email, password, role_id) VALUES(?,?,?,2)";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
            ps.setString(3, hashed);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= GET TOTAL USERS =================
    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) FROM Users WHERE status = 1 AND role_id = 2";
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

    // ================= SEARCH USERS BY PAGE=================
    public List<Users> searchUsersByPage(String keyword, Integer role, int start, int total) {

        keyword = keyword == null ? "" : keyword.trim();

        List<Users> list = new ArrayList<>();

        String sql = "SELECT u.*, r.role_name " +
                "FROM Users u " +
                "JOIN Role r ON u.role_id = r.role_id " +
                "WHERE u.status = 1 " +
                (role != null ? " AND u.role_id = ? " : "") +
                " AND (" +
                (keyword.matches("\\d+") ? "u.user_id LIKE ? OR " : "") +
                "u.name COLLATE Latin1_General_CI_AI LIKE ? OR " +
                "u.email COLLATE Latin1_General_CI_AI LIKE ?) " +
                "ORDER BY u.user_id " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            if (role != null) ps.setInt(index++, role);
            if (keyword.matches("\\d+")) ps.setString(index++, keyword);

            ps.setString(index++, "%" + keyword + "%");
            ps.setString(index++, "%" + keyword + "%");
            ps.setInt(index++, start);
            ps.setInt(index++, total);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Users b = mapResultSet(rs);
                list.add(b);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= COUNT SEARCH USERS =================
    public int countSearchUsers(String keyword) {
        keyword = keyword == null ? "" : keyword.trim();
        String sql = "SELECT COUNT(*) " +
                "FROM Users " +
                "WHERE status = 1 AND role_id = 2 AND (" +
                (keyword.matches("\\d+") ? "user_id LIKE ? OR " : "") +
                "name COLLATE Latin1_General_CI_AI LIKE ? OR " +
                "email COLLATE Latin1_General_CI_AI LIKE ?)";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            if (keyword.matches("\\d+")) {
                ps.setString(index++, keyword);
            }

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

    // ================= COUNT SEARCH ADMINS =================
    public int countSearchAdmins(String keyword) {
        keyword = keyword == null ? "" : keyword.trim();
        int count = 0;
        String sql = "SELECT COUNT(*) " +
                "FROM Users " +
                "WHERE status = 1 AND role_id = 1 AND (" +
                (keyword.matches("\\d+") ? "user_id LIKE ? OR " : "") +
                "name COLLATE Latin1_General_CI_AI LIKE ? OR " +
                "email COLLATE Latin1_General_CI_AI LIKE ?)";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            if (keyword.matches("\\d+")) {
                ps.setString(index++, keyword);
            }

            ps.setString(index++, "%" + keyword + "%");
            ps.setString(index++, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    // ================= GET USER BY ID =================
    public Users getUserById(int id) throws Exception {
        String sql = "SELECT * FROM Users WHERE user_id = ?";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Users u = new Users();
                u.setUserId(rs.getInt("user_id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRoleId(rs.getInt("role_id"));
                return u;
            }
        }
        return null;
    }

    // ================= DELETE USER =================
    public void deleteUser(int id) {

        String sql = "UPDATE Users SET status = 0 WHERE user_id = ?";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= MAP RESULT =================
    private Users mapResultSet(ResultSet rs) throws Exception {

        Users user = new Users();

        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRoleId(rs.getInt("role_id"));
        user.setStatus(rs.getInt("status"));

        return user;
    }

    // ================= CHECK PASSWORD =================
    public boolean checkPassword(int userId, String password) {

        String sql = "SELECT password FROM users WHERE user_id=?";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashed = rs.getString("password");
                return BCrypt.checkpw(password, hashed);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= UPDATE PASSWORD =================
    public void updatePassword(int userId, String newPassword) {

        String sql = "UPDATE users SET password=? WHERE user_id=? AND status=1";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            ps.setString(1, hashed);
            ps.setInt(2, userId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= UPDATE USER FOR ADMIN =================
    public void updateUserAdmin(Users user, String newPassword) throws Exception {
        String sql;

        boolean hasPassword = newPassword != null && !newPassword.trim().isEmpty();

        if (hasPassword) {
            sql = "UPDATE Users SET name=?, email=?, password=? WHERE user_id=?";
        } else {
            sql = "UPDATE Users SET name=?, email=? WHERE user_id=?";
        }

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());

            if (hasPassword) {
                String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                ps.setString(3, hashed);
                ps.setInt(4, user.getUserId());
            } else {
                ps.setInt(3, user.getUserId());
            }

            ps.executeUpdate();
        }
    }

    // ================= CHECK EMAIL EXIST FOR OTHER =================
    public boolean checkEmailExistForOther(String email, int userId) {
        String sql = "SELECT COUNT(*) FROM Users WHERE email=? AND user_id<>? AND status=1";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setInt(2, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= ADD USER FOR ADMIN =================
    public boolean addUser(Users user, String password) throws Exception {
        String checkSql = "SELECT COUNT(*) FROM Users WHERE email = ?";
        String insertSql = "INSERT INTO Users(name, email, password, role_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection()) {

            // 🔍 Check email tồn tại
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, user.getEmail());
                ResultSet rs = checkPs.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // email đã tồn tại
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
                ps.setString(3, hashed);
                ps.setInt(4, user.getRoleId());

                ps.executeUpdate();
            }
        }

        return true;
    }
}