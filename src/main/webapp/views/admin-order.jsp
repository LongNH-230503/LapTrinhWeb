<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <!DOCTYPE html>
            <html>

            <head>

                <head>

                    <title>Admin Panel</title>

                    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base/base.css">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/admin.css">

                </head>

            <body>

                <!-- SUCCESS/ERROR MESSAGES -->
                <c:if test="${not empty sessionScope.success}">
                    <div class="success-message">
                        ${sessionScope.success}
                    </div>
                    <c:remove var="success" scope="session" />
                </c:if>

                <c:if test="${not empty sessionScope.error}">
                    <div class="error-message">
                        ${sessionScope.error}
                    </div>
                    <c:remove var="error" scope="session" />
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

                                <a href="${pageContext.request.contextPath}/admin/order" class="nav-item active">
                                    📦 Order
                                </a>

                                <a href="${pageContext.request.contextPath}/admin/book" class="nav-item">
                                    📚 Book
                                </a>

                                <a href="${pageContext.request.contextPath}/admin/user" class="nav-item">
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
                                <h2>Order Management</h2>
                                <p>Manage customer orders</p>
                            </div>

                            <div class="header-actions">

                                <form action="${pageContext.request.contextPath}/admin/order" method="get"
                                    class="search-form">
                                    <input type="text" name="search" value="${param.search}"
                                        placeholder="Search orders...">
                                </form>

                                <select class="admin-filter" onchange="filterOrders(this.value)">

                                    <option value="all">All Status</option>
                                    <option value="Pending">Pending</option>
                                    <option value="Completed">Completed</option>
                                    <option value="Cancelled">Cancelled</option>

                                </select>

                            </div>

                        </div>


                        <!-- STATS -->
                        <div class="stats-grid">

                            <div class="stat-card">
                                <p>Total Orders</p>
                                <h3>${totalOrders}</h3>
                            </div>

                            <div class="stat-card">
                                <p>Pending Orders</p>
                                <h3>${pendingOrders}</h3>
                            </div>

                        </div>

                        <!-- ORDER TABLE -->
                        <table class="admin-table">

                            <thead>

                                <tr class="table-head">

                                    <th>Order ID</th>
                                    <th>User</th>
                                    <th>Total</th>
                                    <th>Status</th>
                                    <th>Date</th>
                                    <th>View</th>

                                </tr>

                            </thead>

                            <tbody id="orderTable">

                                <c:forEach var="order" items="${orders}">

                                    <tr data-status="${order.status}">

                                        <td>#${order.orderId}</td>

                                        <td>${order.username}</td>

                                        <td>$
                                            <fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00" />
                                        </td>

                                        <td>
                                            <span class="order-status ${order.status}"
                                                onclick="openStatusModal('${order.orderId}', '${order.status}')">

                                                ${order.status}

                                            </span>
                                        </td>

                                        <td>
                                            <fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd" />
                                        </td>

                                        <td class="actions-cell">

                                            <a href="${pageContext.request.contextPath}/admin/order/detail?id=${order.orderId}"
                                                class="icon-btn">
                                                👁
                                            </a>

                                        </td>

                                    </tr>

                                </c:forEach>

                                <form id="updateForm" method="post"
                                    action="${pageContext.request.contextPath}/admin/order">
                                    <input type="hidden" name="orderId" id="formOrderId">
                                    <input type="hidden" name="status" id="formStatus">
                                </form>

                            </tbody>

                        </table>


                        <!-- PAGINATION -->

                        <div class="pagination">

                            <c:if test="${currentPage > 1}">
                                <a
                                    href="${pageContext.request.contextPath}/admin/order?page=${currentPage - 1}&search=${param.search}">
                                    <button class="page-btn">← Prev</button>
                                </a>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="i">

                                <a href="${pageContext.request.contextPath}/admin/order?page=${i}&search=${param.search}"
                                    class="page-btn ${i == currentPage ? 'active-page' : ''}">
                                    ${i}
                                </a>

                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a
                                    href="${pageContext.request.contextPath}/admin/order?page=${currentPage + 1}&search=${param.search}">
                                    <button class="page-btn">Next →</button>
                                </a>
                            </c:if>

                        </div>

                    </main>

                </div>

                <div class="modal-overlay" id="statusModal">

                    <div class="modal small-modal">

                        <div class="modal-header">
                            <h3>Update Order Status</h3>
                            <button class="close-btn" onclick="closeStatusModal()">✕</button>
                        </div>

                        <div class="modal-body">

                            <p>Update status for this order?</p>

                        </div>

                        <div class="modal-footer">

                            <button class="cancel-btn" style="background: #ef4444; color: white;"
                                onclick="updateStatus('Cancelled')">
                                Cancel Order
                            </button>

                            <button class="save-btn" style="background: #22c55e; color: white;"
                                onclick="updateStatus('Completed')">
                                Approve Order
                            </button>

                        </div>

                    </div>

                </div>


                <script>

                    let currentOrderId = null;

                    function openStatusModal(orderId, status) {
                        if (status !== "Pending") {
                            alert("Only pending orders can be updated!");
                            return;
                        }
                        currentOrderId = orderId;
                        document.getElementById("statusModal").classList.add("active");
                    }

                    function closeStatusModal() {
                        document.getElementById("statusModal").classList.remove("active");
                    }

                    function updateStatus(newStatus) {
                        if (currentOrderId) {
                            document.getElementById("formOrderId").value = currentOrderId;
                            document.getElementById("formStatus").value = newStatus;
                            document.getElementById("updateForm").submit();
                        }
                    }

                    function searchOrders() {
                        let input = document.querySelector(".admin-search").value.toLowerCase();
                        let rows = document.querySelectorAll("#orderTable tr");

                        rows.forEach(row => {
                            let text = row.innerText.toLowerCase();
                            row.style.display = text.includes(input) ? "" : "none";
                        });
                    }

                    function filterOrders(status) {
                        let rows = document.querySelectorAll("#orderTable tr");
                        rows.forEach(row => {
                            if (status === "all") {
                                row.style.display = "";
                            } else {
                                row.style.display = (row.dataset.status === status) ? "" : "none";
                            }
                        });
                    }

                </script>

            </body>

            </html>