<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page contentType="text/html; charset=UTF-8" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <!DOCTYPE html>

            <html>

            <head>
                <title>Shopping Cart</title>

                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base/base.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout/header.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/cart.css">

            </head>

            <body>

                <c:choose>

                    <c:when test="${not empty sessionScope.cart}">

                        <div class="cart-page">

                            <div class="container">

                                <a href="${pageContext.request.contextPath}/home" class="back-link">
                                    ← Continue Shopping
                                </a>

                                <h1 class="cart-title">Shopping Cart</h1>

                                <div class="cart-layout">

                                    <!-- CART ITEMS -->
                                    <div class="cart-items">

                                        <c:set var="total" value="0" />
                                        <c:set var="totalItems" value="0" />

                                        <c:forEach var="item" items="${sessionScope.cart}">

                                            <c:set var="total" value="${total + item.total}" />
                                            <c:set var="totalItems" value="${totalItems + item.quantity}" />

                                            <div class="cart-item">

                                                <div class="cart-image">
                                                    <img
                                                        src="${pageContext.request.contextPath}/images/${item.book.imageUrl}">
                                                </div>

                                                <div class="cart-info">
                                                    <h3>${item.book.title}</h3>
                                                    <p class="author">${item.book.author}</p>
                                                    <p class="price">$${item.book.price}</p>
                                                </div>

                                                <div class="cart-actions">

                                                    <div>
                                                        <form action="${pageContext.request.contextPath}/update-cart"
                                                            method="post">

                                                            <input type="hidden" name="id" value="${item.book.bookId}">
                                                            <input type="hidden" name="quantity" value="0">
                                                            <button type="submit" class="delete-btn">
                                                                🗑
                                                            </button>
                                                        </form>
                                                    </div>

                                                    <div class="quantity-control">

                                                        <!-- decrease -->
                                                        <form action="${pageContext.request.contextPath}/update-cart"
                                                            method="post">

                                                            <input type="hidden" name="id" value="${item.book.bookId}">
                                                            <input type="hidden" name="quantity"
                                                                value="${item.quantity - 1}">

                                                            <button type="submit">-</button>

                                                        </form>

                                                        <span>${item.quantity}</span>

                                                        <!-- increase -->
                                                        <form action="${pageContext.request.contextPath}/update-cart"
                                                            method="post">

                                                            <input type="hidden" name="id" value="${item.book.bookId}">
                                                            <input type="hidden" name="quantity"
                                                                value="${item.quantity + 1}">

                                                            <button type="submit">+</button>

                                                        </form>

                                                    </div>

                                                    <p class="item-total">
                                                        $
                                                        <fmt:formatNumber value="${item.total}" type="number"
                                                            minFractionDigits="2" maxFractionDigits="2" />
                                                    </p>

                                                </div>

                                            </div>

                                        </c:forEach>

                                    </div>

                                    <!-- ORDER SUMMARY -->
                                    <div class="order-summary">
                                        <h2>Subtotal (${totalItems} items):
                                            <span class="total-amount">
                                                $
                                                <fmt:formatNumber value="${total}" type="number" minFractionDigits="2"
                                                    maxFractionDigits="2" />
                                            </span>
                                        </h2>

                                        <a href="${pageContext.request.contextPath}/checkout" class="checkout-btn">
                                            Proceed to Checkout
                                        </a>
                                    </div>

                                </div>

                            </div>

                        </div>

                    </c:when>

                    <c:otherwise>

                        <div class="cart-empty">

                            <div class="container">

                                <a href="${pageContext.request.contextPath}/home" class="back-link">
                                    ← Back to Books
                                </a>

                                <div class="empty-box">
                                    <h2>Your Cart is Empty</h2>
                                    <p>Add some books to get started!</p>

                                    <a href="${pageContext.request.contextPath}/home" class="browse-btn">
                                        Browse Books
                                    </a>
                                </div>

                            </div>

                        </div>

                    </c:otherwise>

                </c:choose>

            </body>

            </html>