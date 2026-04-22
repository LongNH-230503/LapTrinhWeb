<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html>

        <head>

            <title>Admin Panel</title>

            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base/base.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/admin.css">

        </head>

        <body>

            <c:if test="${not empty sessionScope.error}">
                <div class="error-message">
                    ${sessionScope.error}
                </div>
                <c:remove var="error" scope="session" />
            </c:if>

            <c:if test="${not empty sessionScope.success}">
                <div class="success-message">
                    ${sessionScope.success}
                </div>
                <c:remove var="success" scope="session" />
            </c:if>

            <div class="admin-layout">

                <!-- SIDEBAR -->

                <aside class="admin-sidebar">

                    <div>

                        <div class="admin-logo">
                            <h1>Book Store</h1>
                            <p>Admin Panel</p>
                        </div>

                        <nav class="admin-nav">

                            <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-item">
                                📊 Dashboard
                            </a>

                            <a href="${pageContext.request.contextPath}/admin/order" class="nav-item">
                                📦 Order
                            </a>

                            <a href="${pageContext.request.contextPath}/admin/book" class="nav-item">
                                📚 Book
                            </a>

                            <a href="${pageContext.request.contextPath}/admin/user" class="nav-item active">
                                👤 User
                            </a>

                        </nav>

                    </div>

                    <div class="sidebar-bottom">

                        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">
                            🚪 Logout
                        </a>

                    </div>

                </aside>


                <!-- MAIN -->

                <main class="admin-main">

                    <!-- HEADER -->

                    <div class="admin-header">

                        <div>
                            <h2>Users Management</h2>
                            <p>Manage system users</p>
                        </div>

                        <div class="header-actions">

                            <form action="${pageContext.request.contextPath}/admin/user" method="get"
                                class="search-form">
                                <input type="text" name="search" value="${param.search}" placeholder="Search users...">
                                <input type="hidden" name="role" value="${param.role}">
                            </form>

                            <button class="admin-btn" onclick="openAddUserModal()">
                                Add User
                            </button>


                            <select class="admin-filter" onchange="applyFilter(this.value)">

                                <option value="all" ${empty param.role || param.role=='all' ? 'selected' : '' }>
                                    All Role
                                </option>

                                <option value="1" ${param.role=='1' ? 'selected' : '' }>
                                    Admin
                                </option>

                                <option value="2" ${param.role=='2' ? 'selected' : '' }>
                                    Customer
                                </option>

                            </select>

                        </div>

                    </div>


                    <!-- STATS -->

                    <div class="stats-grid">

                        <div class="stat-card">
                            <p>Users</p>
                            <h3>${totalUsers}</h3>
                        </div>

                        <div class="stat-card">
                            <p>Admins</p>
                            <h3>${totalAdmins}</h3>
                        </div>

                    </div>


                    <!-- TABLE -->

                    <table class="admin-table">

                        <thead>

                            <tr class="table-head">

                                <th>ID</th>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Role</th>
                                <th>Actions</th>

                            </tr>

                        </thead>

                        <tbody id="userTable">

                            <c:forEach var="user" items="${users}">

                                <tr>

                                    <td>${user.userId}</td>

                                    <td>${user.name}</td>

                                    <td>${user.email}</td>

                                    <td>

                                        <span class="role-badge">

                                            <c:choose>

                                                <c:when test="${user.roleId == 1}">ADMIN</c:when>

                                                <c:otherwise>CUSTOMER</c:otherwise>

                                            </c:choose>

                                        </span>

                                    </td>

                                    <td class="actions-cell" style="gap: 12px;">

                                        <!-- Edit luôn có -->
                                        <button class="icon-btn edit-btn" onclick="openEditUser(this)"
                                            data-id="${user.userId}" data-username="${user.name}"
                                            data-email="${user.email}" data-role="${user.roleId}">
                                            ✏️
                                        </button>

                                        <!-- Chỉ customer mới có delete -->
                                        <c:if test="${user.roleId != 1}">
                                            <button class="icon-btn delete-btn" onclick="deleteUser('${user.userId}')">
                                                🗑
                                            </button>
                                        </c:if>

                                    </td>

                                </tr>

                            </c:forEach>

                        </tbody>

                    </table>

                    <!-- PAGINATION -->

                    <div class="pagination">

                        <c:if test="${currentPage > 1}">
                            <a
                                href="${pageContext.request.contextPath}/admin/user?page=${i}&search=${param.search}&role=${param.role}">
                                <button class="page-btn">← Prev</button>
                            </a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="i">

                            <a href="${pageContext.request.contextPath}/admin/user?page=${i}&search=${param.search}&role=${param.role}"
                                class="page-btn ${i == currentPage ? 'active-page' : ''}">
                                ${i}
                            </a>

                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a
                                href="${pageContext.request.contextPath}/admin/user?page=${currentPage + 1}&search=${param.search}&role=${param.role}">
                                <button class="page-btn">Next →</button>
                            </a>
                        </c:if>

                    </div>

                    <!-- ADD USER MODAL -->
                    <div id="addUserModal" class="user-modal-overlay">

                        <div class="user-modal">

                            <div class="modal-content">

                                <div class="modal-header">
                                    <h3>Add User</h3>
                                    <button class="close-btn" onclick="closeAddUserModal()">✕</button>
                                </div>

                                <form action="${pageContext.request.contextPath}/admin/user/add-update-user"
                                    method="post">

                                    <div class="form-group">
                                        <label>Username</label>
                                        <input type="text" name="name" required>
                                    </div>

                                    <div class="form-group">
                                        <label>Email</label>
                                        <input type="email" name="email" required>
                                    </div>

                                    <div class="form-group">
                                        <label>Password</label>
                                        <input type="password" name="password" required>
                                    </div>

                                    <div class="form-group">
                                        <label>Role</label>

                                        <select name="roleId">
                                            <option value="2">Customer</option>
                                            <option value="1">Admin</option>
                                        </select>

                                    </div>

                                    <div class="modal-actions">

                                        <button type="submit" class="admin-btn">Add User</button>

                                        <button type="button" class="cancel-btn"
                                            onclick="closeAddUserModal()">Cancel</button>

                                    </div>

                                </form>

                            </div>

                        </div>

                    </div>

                    <!-- EDIT USER MODAL -->
                    <div id="editUserModal" class="user-modal-overlay">

                        <div class="user-modal">

                            <div class="modal-content">

                                <div class="modal-header">
                                    <h3>Edit User</h3>
                                    <button class="close-btn" onclick="closeEditUserModal()">✕</button>
                                </div>

                                <form action="${pageContext.request.contextPath}/admin/user/add-update-user"
                                    method="post">

                                    <input type="hidden" name="userId" id="editUserId">

                                    <div class="form-group" id="adminPasswordGroup" style="display:none;">
                                        <label>Admin Password (required)</label>
                                        <input type="password" name="adminPassword" id="adminPassword">
                                    </div>

                                    <div class="form-group">
                                        <label>Username</label>
                                        <input type="text" name="name" id="editUsername" required>
                                    </div>

                                    <div class="form-group">
                                        <label>Email</label>
                                        <input type="email" name="email" id="editEmail" required>
                                    </div>

                                    <div class="form-group">
                                        <label>New Password</label>
                                        <input type="password" name="newPassword" id="editPassword"
                                            placeholder="Leave blank to keep current password">
                                    </div>

                                    <div class="modal-actions">

                                        <button type="submit" class="admin-btn">Save</button>

                                        <button type="button" class="cancel-btn"
                                            onclick="closeEditUserModal()">Cancel</button>

                                    </div>

                                </form>

                            </div>

                        </div>

                    </div>

                </main>

            </div>

            <script>
                function openAddUserModal() {
                    document.getElementById("addUserModal").style.display = "flex";
                }

                function closeAddUserModal() {
                    document.getElementById("addUserModal").style.display = "none";
                }

                function openEditUser(btn) {
                    document.getElementById("editUserModal").style.display = "flex";
                    document.getElementById("editUserId").value = btn.dataset.id;
                    document.getElementById("editUsername").value = btn.dataset.username;
                    document.getElementById("editEmail").value = btn.dataset.email;
                    let role = btn.dataset.role;
                    // Nếu là admin → hiện field password
                    if (role == "1") {
                        document.getElementById("adminPasswordGroup").style.display = "block";
                    } else {
                        document.getElementById("adminPasswordGroup").style.display = "none";
                    }
                }

                function closeEditUserModal() {
                    document.getElementById("editUserModal").style.display = "none";
                }

                function deleteUser(userId) {
                    if (confirm("Are you sure you want to delete this user?")) {
                        document.getElementById("deleteUserId").value = userId;
                        document.getElementById("deleteForm").submit();
                    }
                }

                function searchUsers() {
                    let input = document.querySelector(".admin-search").value.toLowerCase();
                    let rows = document.querySelectorAll("#userTable tr");
                    rows.forEach(row => {
                        let text = row.innerText.toLowerCase();
                        row.style.display = text.includes(input) ? "" : "none";
                    });
                }

                function applyFilter(role) {
                    let url = new URL(window.location.href);
                    url.searchParams.set("role", role);
                    url.searchParams.set("page", 1);
                    window.location.href = url.toString();
                }
            </script>
            <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/admin/user/delete">
                <input type="hidden" name="id" id="deleteUserId">
            </form>
        </body>

        </html>