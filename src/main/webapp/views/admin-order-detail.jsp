<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <html>

            <head>

                <title>Order Detail</title>

                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base/base.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/order-detail.css">

            </head>

            <body>

                <div class="order-container">

                    <a class="back-btn" href="${pageContext.request.contextPath}/admin/order">
                        ← Back to Order
                    </a>

                    <h2>📦 Order #${order.orderId}</h2>

                    <div class="admin-order-header">

                        <div class="order-info">

                            <div><b>Name:</b> ${order.username}</div>
                            <div><b>Phone:</b> ${order.phone}</div>
                            <div><b>Address:</b> ${order.address}</div>
                            <div><b>Date:</b> ${order.orderDate}</div>

                            <div>
                                <b>Status:</b>
                                <span class="order-status ${order.status}">${order.status}</span>
                            </div>

                        </div>

                        <div class="admin-summary">

                            <c:set var="total" value="0" />
                            <c:forEach var="i" items="${items}">
                                <c:set var="total" value="${total + (i.unitPrice * i.quantity)}" />
                            </c:forEach>

                            <div class="summary-row">
                                <span>Total</span>
                                <span>$
                                    <fmt:formatNumber value="${total}" pattern="#,##0.00" />
                                </span>
                            </div>

                        </div>

                    </div>

                    <table class="order-table">

                        <tr>
                            <th>Book</th>
                            <th>Name</th>
                            <th>Price</th>
                            <th>Qty</th>
                            <th>Subtotal</th>
                        </tr>

                        <c:forEach var="i" items="${items}">
                            <tr>
                                <td>
                                    <img class="book-thumb"
                                        src="${pageContext.request.contextPath}/images/${i.imageUrl}">
                                </td>
                                <td>${i.title}</td>
                                <td>$
                                    <fmt:formatNumber value="${i.unitPrice}" pattern="#,##0.00" />
                                </td>
                                <td>${i.quantity}</td>
                                <td>$
                                    <fmt:formatNumber value="${i.unitPrice * i.quantity}" pattern="#,##0.00" />
                                </td>
                            </tr>
                        </c:forEach>

                    </table>

                </div>

            </body>

            </html>