<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <html>

        <head>

            <title>Admin Dashboard</title>

            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base/base.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/admin.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/admin-dashboard.css">

        </head>

        <body>

            <div class="admin-layout">

                <!-- SIDEBAR -->

                <aside class="admin-sidebar">

                    <div>

                        <div class="admin-logo">
                            <h1>Book Store</h1>
                            <p>Admin Panel</p>
                        </div>

                        <nav class="admin-nav">

                            <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-item active">
                                📊 Dashboard
                            </a>

                            <a href="${pageContext.request.contextPath}/admin/order" class="nav-item">
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
                            <h2>Admin Dashboard</h2>
                            <p>Overview of your store</p>
                        </div>

                    </div>


                    <!-- KPI CARDS -->

                    <div class="dashboard-cards">

                        <div class="dashboard-card orders">
                            <div class="card-icon">📦</div>
                            <div class="card-info">
                                <div class="card-title">Total Orders</div>
                                <div class="card-value">${totalOrders}</div>
                            </div>
                        </div>

                        <div class="dashboard-card revenue">
                            <div class="card-icon">💰</div>
                            <div class="card-info">
                                <div class="card-title">Revenue</div>
                                <div class="card-value">$${totalRevenue}</div>
                            </div>
                        </div>

                        <div class="dashboard-card users">
                            <div class="card-icon">👤</div>
                            <div class="card-info">
                                <div class="card-title">Users</div>
                                <div class="card-value">${totalUsers}</div>
                            </div>
                        </div>

                        <div class="dashboard-card books">
                            <div class="card-icon">📚</div>
                            <div class="card-info">
                                <div class="card-title">Books</div>
                                <div class="card-value">${totalBooks}</div>
                            </div>
                        </div>

                    </div>


                    <!-- CHARTS -->

                    <div class="dashboard-charts">

                        <!-- SALES -->

                        <div class="dashboard-panel">

                            <h3>Sales (Last 7 Days)</h3>

                            <canvas id="salesChart"></canvas>

                        </div>


                        <!-- ORDER STATUS -->

                        <div class="dashboard-panel">

                            <h3>Order Status Ratio</h3>

                            <canvas id="orderStatusChart"></canvas>

                        </div>


                        <!-- CATEGORY REVENUE -->

                        <div class="dashboard-panel">

                            <h3>Revenue by Category</h3>

                            <canvas id="categoryChart"></canvas>

                        </div>

                    </div>


                    <!-- RECENT ORDERS -->

                    <div class="dashboard-section">

                        <h3 style="margin-bottom:15px;">Recent Orders</h3>

                        <table class="admin-table">

                            <thead>
                                <tr class="table-head">
                                    <th>ID</th>
                                    <th>Customer</th>
                                    <th>Date</th>
                                    <th>Total</th>
                                    <th>Status</th>
                                </tr>
                            </thead>

                            <tbody>

                                <c:forEach var="o" items="${recentOrders}">
                                    <tr>
                                        <td>#${o.orderId}</td>
                                        <td>${o.username}</td>
                                        <td>${o.orderDate}</td>
                                        <td>$${o.totalAmount}</td>
                                        <td>
                                            <div class="order-status ${o.status}">
                                                ${o.status}
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>

                            </tbody>

                        </table>

                    </div>

                </main>

            </div>


            <!-- CHART.JS -->

            <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

            <script>

                const revenueData = [
                    <c:forEach var="r" items="${revenue7Days}" varStatus="loop">
                        ${r}${!loop.last ? ',' : ''}
                    </c:forEach>
                ];

                const statusLabels = [
                    <c:forEach var="s" items="${orderStatusStats}" varStatus="loop">
                        '${s.key}'${!loop.last ? ',' : ''}
                    </c:forEach>
                ];

                const statusData = [
                    <c:forEach var="s" items="${orderStatusStats}" varStatus="loop">
                        ${s.value}${!loop.last ? ',' : ''}
                    </c:forEach>
                ];

                /* SALES 7 DAYS */

                new Chart(
                    document.getElementById('salesChart'),
                    {
                        type: 'bar',
                        data: {
                            labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                            datasets: [{
                                label: 'Revenue',
                                data: revenueData,
                                backgroundColor: '#3b82f6'
                            }]
                        },
                        options: {
                            responsive: true,
                            plugins: {
                                legend: { display: false }
                            }
                        }
                    });


                /* ORDER STATUS */

                new Chart(
                    document.getElementById('orderStatusChart'),
                    {
                        type: 'pie',
                        data: {
                            labels: statusLabels,
                            datasets: [{
                                data: statusData,
                                backgroundColor: ['#22c55e', '#facc15', '#ef4444']
                            }]
                        }
                    });


                /* CATEGORY REVENUE */

                new Chart(
                    document.getElementById('categoryChart'),
                    {
                        type: 'doughnut',
                        data: {
                            labels: [
                                'Programming',
                                'Business',
                                'Self Development',
                                'Technology'
                            ],
                            datasets: [{
                                data: [1500, 900, 700, 1200],
                                backgroundColor: [
                                    '#6366f1',
                                    '#22c55e',
                                    '#f59e0b',
                                    '#06b6d4'
                                ]
                            }]
                        }
                    });

            </script>

        </body>

        </html>