package com.bookstore.controller.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.UsersDAO;
import com.bookstore.model.Users;

@WebServlet("/admin/user")
public class AdminUserServlet extends HttpServlet {

    private UsersDAO usersDAO;

    @Override
    public void init() {
        usersDAO = new UsersDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int page = 1;
        int recordsPerPage = 5;

        String pageParam = req.getParameter("page");
        String search = req.getParameter("search");

        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        String roleParam = req.getParameter("role");
        Integer role = null;

        if (roleParam != null && !roleParam.equals("all")) {
            try {
                role = Integer.parseInt(roleParam);
            } catch (Exception e) {
                role = null;
            }
        }

        int start = (page - 1) * recordsPerPage;

        List<Users> users = usersDAO.searchUsersByPage(search, role, start, recordsPerPage);
        int totalAdmins, totalUsers;

        if (role != null) {
            totalAdmins = role == 2 ? 0 : usersDAO.countSearchAdmins(search);
            totalUsers = role == 1 ? 0 : usersDAO.countSearchUsers(search);
        } else {
            totalAdmins = usersDAO.countSearchAdmins(search);
            totalUsers = usersDAO.countSearchUsers(search);
        }

        int totalPages = (int) Math.ceil((double) totalUsers / recordsPerPage);

        req.setAttribute("users", users);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("totalAdmins", totalAdmins);
        req.setAttribute("search", search);

        req.getRequestDispatcher("/views/admin-user.jsp").forward(req, resp);
    }
}