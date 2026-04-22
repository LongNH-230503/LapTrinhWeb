package com.bookstore.controller.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.UsersDAO;
import com.bookstore.model.Users;

@WebServlet("/admin/user/add-update-user")
public class AdminUserAddUpdateServlet extends HttpServlet {
    private UsersDAO userDAO;

    @Override
    public void init() {
        userDAO = new UsersDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String userIdStr = request.getParameter("userId");
            boolean isUpdate = userIdStr != null && !userIdStr.isEmpty();

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String roleIdStr = request.getParameter("roleId");

            String password = isUpdate
                    ? request.getParameter("newPassword")
                    : request.getParameter("password");

            // ✅ NULL SAFE
            if (name == null || name.trim().isEmpty() ||
                    email == null || email.trim().isEmpty()) {

                request.getSession().setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
                response.sendRedirect(request.getContextPath() + "/admin/user");
                return;
            }

            // 👉 check role khi ADD
            if (!isUpdate && (roleIdStr == null || roleIdStr.trim().isEmpty())) {
                request.getSession().setAttribute("error", "Thiếu vai trò");
                response.sendRedirect(request.getContextPath() + "/admin/user");
                return;
            }

            Users user = new Users();
            user.setName(name.trim());
            user.setEmail(email.trim());

            if (isUpdate) {

                int userId = Integer.parseInt(userIdStr);
                Users existingUser = userDAO.getUserById(userId);

                if (existingUser == null) {
                    request.getSession().setAttribute("error", "User không tồn tại");
                    response.sendRedirect(request.getContextPath() + "/admin/user");
                    return;
                }

                user.setRoleId(existingUser.getRoleId());

                // 🚨 Nếu là ADMIN → check password
                if (existingUser.getRoleId() == 1) {

                    String adminPassword = request.getParameter("adminPassword");

                    if (adminPassword == null || adminPassword.isEmpty()) {
                        request.getSession().setAttribute("error", "Nhập mật khẩu admin để chỉnh sửa");
                        response.sendRedirect(request.getContextPath() + "/admin/user");
                        return;
                    }

                    boolean valid = userDAO.checkPassword(userId, adminPassword);

                    if (!valid) {
                        request.getSession().setAttribute("error", "Mật khẩu admin không đúng");
                        response.sendRedirect(request.getContextPath() + "/admin/user");
                        return;
                    }
                }

                user.setUserId(userId);

                if (userDAO.checkEmailExistForOther(email, userId)) {
                    request.getSession().setAttribute("error", "Email đã được sử dụng");
                    response.sendRedirect(request.getContextPath() + "/admin/user");
                    return;
                }

                userDAO.updateUserAdmin(user, password);
                request.getSession().setAttribute("success", "Cập nhật người dùng thành công");

            } else {
                // ✅ ADD
                user.setRoleId(Integer.parseInt(roleIdStr));

                boolean success = userDAO.addUser(user, password);

                if (!success) {
                    request.getSession().setAttribute("error", "Email đã tồn tại");
                    response.sendRedirect(request.getContextPath() + "/admin/user");
                    return;
                } else {
                    request.getSession().setAttribute("success", "Thêm người dùng thành công");
                }
            }

            response.sendRedirect(request.getContextPath() + "/admin/user");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/admin/user");

        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage();
            if (msg != null && msg.contains("UNIQUE KEY")) {
                request.getSession().setAttribute("error", "Email đã tồn tại");
            } else {
                request.getSession().setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại");
            }
            response.sendRedirect(request.getContextPath() + "/admin/user");
        }
    }
}