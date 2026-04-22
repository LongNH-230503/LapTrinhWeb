<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>${book.title} - Book Store</title>

            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base/base.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout/header.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/book-detail.css">
        </head>

        <body>

            <!-- HEADER -->
            <header class="header">
                <div class="container header-inner">
                    <a href="${pageContext.request.contextPath}/home" class="logo">
                        📚 <h1>Book Store</h1>
                    </a>
                </div>
            </header>

            <div class="container book-detail-wrapper">

                <!-- Back Button -->
                <a href="${pageContext.request.contextPath}/home" class="back-btn">
                    ← Back to Catalog
                </a>

                <div class="book-card">

                    <!-- TOP SECTION -->
                    <div class="book-top">

                        <!-- LEFT IMAGE -->
                        <div class="book-image-section">
                            <div class="image-box">
                                <img src="${pageContext.request.contextPath}/images/${book.imageUrl}">
                            </div>
                        </div>

                        <!-- RIGHT INFO -->
                        <div class="book-info">

                            <h1 class="book-title">${book.title}</h1>

                            <p class="book-author">
                                Author: ${book.author}
                            </p>

                            <p class="book-category">
                                Category: ${book.categoryName}
                            </p>

                            <p class="book-isbn">
                                ISBN: ${book.isbn}
                            </p>

                            <!-- PRICE -->
                            <div class="price-box">
                                <span class="price">$${book.price}</span>
                            </div>

                            <!-- CART ACTIONS -->
                            <c:choose>
                                <c:when test="${book.stock > 0}">
                                    <form id="addToCartForm" action="${pageContext.request.contextPath}/add-to-cart"
                                        method="post" class="cart-row">
                                        <input type="hidden" name="id" value="${book.bookId}">
                                        <input type="hidden" name="quantity" id="quantityInput" value="1">
                                        <input type="hidden" name="returnUrl"
                                            value="${param.returnUrl != null ? param.returnUrl : '/home'}">

                                        <div class="quantity-box">
                                            <button type="button" onclick="changeQty(-1, '${book.stock}')">−</button>
                                            <span id="quantityDisplay">1</span>
                                            <button type="button" onclick="changeQty(1, '${book.stock}')">+</button>
                                        </div>

                                        <button type="button" id="addBtn" class="add-cart-btn"
                                            onclick="addToCart('${book.stock}')">
                                            🛒 Add to Cart
                                        </button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <p class="stock-out" style="color: red; font-weight: bold;">❌ Temporarily Out of
                                        Stock</p>
                                </c:otherwise>
                            </c:choose>

                        </div>

                    </div>


                    <!-- DESCRIPTION -->
                    <div class="description-section">

                        <h2>Description</h2>

                        <p>
                            ${book.description}
                        </p>

                    </div>

                </div>
            </div>

            <script>
                let qty = 1;

                function changeQty(amount, maxStock) {
                    qty += amount;

                    if (qty < 1) qty = 1;
                    if (qty > maxStock) qty = maxStock;

                    document.getElementById("quantityDisplay").innerText = qty;
                    document.getElementById("quantityInput").value = qty;
                }

                function addToCart(maxStock) {
                    // Kiểm tra đăng nhập
                    var isLoggedIn = '${sessionScope.user != null}';

                    if (!isLoggedIn) {
                        alert('Please login to add items to cart!');
                        window.location.href = '${pageContext.request.contextPath}/login';
                        return;
                    }

                    // Kiểm tra stock
                    if (maxStock == 0) {
                        alert('This book is out of stock!');
                        return;
                    }

                    if (qty > maxStock) {
                        alert('Only ' + maxStock + ' books available in stock!');
                        return;
                    }

                    // Submit form
                    document.getElementById('addToCartForm').submit();
                }
            </script>

        </body>

        </html>