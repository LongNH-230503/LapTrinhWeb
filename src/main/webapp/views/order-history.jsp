<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <html>

        <head>

            <title>Order History</title>

            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base/base.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/order-history.css">

        </head>

        <body>

            <c:if test="${not empty sessionScope.success}">
                <div class="success-message">
                    <c:if test="${sessionScope.success == 'cancelled'}">
                        ✅ Order cancelled successfully!
                    </c:if>
                    <c:if test="${sessionScope.success == 'order'}">
                        🎉 Order placed successfully!
                    </c:if>
                </div>
                <c:remove var="success" scope="session" />
            </c:if>

            <c:if test="${not empty sessionScope.error}">
                <div class="error-message">
                    <c:if test="${sessionScope.error == 'cancelled'}">
                        ❌ Failed to cancel order!
                    </c:if>
                    <c:if test="${sessionScope.error == 'order'}">
                        ❌ Failed to place order!
                    </c:if>
                </div>
                <c:remove var="error" scope="session" />
            </c:if>

            <div class="orders-container">

                <a class="back-btn" href="${pageContext.request.contextPath}/home">
                    ← Back to Home
                </a>

                <div class="orders-title">📦 Order History</div>

                <c:choose>

                    <c:when test="${empty orders}">

                        <div class="empty-orders">
                            You have not placed any orders yet.
                        </div>

                    </c:when>

                    <c:otherwise>

                        <table class="order-table">

                            <tr>
                                <th>Order ID</th>
                                <th>Date</th>
                                <th>Total</th>
                                <th>Status</th>
                                <th>Phone</th>
                                <th>Address</th>
                                <th></th>
                            </tr>

                            <c:forEach var="o" items="${orders}">

                                <tr>

                                    <td>#${o.orderId}</td>

                                    <td>${o.orderDate}</td>

                                    <td>$${o.totalAmount}</td>

                                    <td>
                                        <span class="status ${o.status}">
                                            ${o.status}
                                        </span>
                                    </td>

                                    <td>${o.phone}</td>

                                    <td>${o.address}</td>

                                    <td class="order-actions">

                                        <a href="${pageContext.request.contextPath}/order-detail?id=${o.orderId}"
                                            class="view-btn">View</a>

                                        <c:if test="${o.status == 'Pending'}">

                                            <form action="${pageContext.request.contextPath}/order-history"
                                                method="post">

                                                <input type="hidden" name="orderId" value="${o.orderId}">
                                                <input type="hidden" name="action" value="cancel">
                                                <input type="hidden" name="page" value="${currentPage}">

                                                <button class="cancel-btn" type="submit">
                                                    ❌
                                                </button>

                                            </form>

                                        </c:if>

                                    </td>

                                </tr>

                            </c:forEach>

                        </table>

                        <div class="pagination">

                            <c:if test="${currentPage > 1}">
                                <a href="order-history?page=${currentPage - 1}">
                                    <button class="page-btn">← Prev</button>
                                </a>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <a href="order-history?page=${i}"
                                    class="page-btn ${i == currentPage ? 'active' : ''}">
                                    ${i}
                                </a>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a href="order-history?page=${currentPage + 1}">
                                    <button class="page-btn">Next →</button>
                                </a>
                            </c:if>

                        </div>

                    </c:otherwise>

                </c:choose>

            </div>

        </body>

        </html>