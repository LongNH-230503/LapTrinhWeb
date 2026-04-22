<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Book Store</title>

            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base/base.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout/header.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/home.css">
        </head>

        <body>

            <c:set var="currentUrl"
                value="/home${not empty param.categoryId ? '?categoryId=' : ''}${param.categoryId}" />

            <c:if test="${orderSuccess}">
                <div class="container" style="margin-top:20px;">
                    <div class="success">
                        🎉 Order placed successfully!
                    </div>
                </div>
            </c:if>

            <c:if test="${not empty sessionScope.successMessage}">
                <div class="success">
                    ${sessionScope.successMessage}
                </div>
                <c:remove var="successMessage" scope="session" />
            </c:if>

            <!-- HEADER -->
            <header class="header">
                <div class="container header-inner">

                    <a href="${pageContext.request.contextPath}/home" class="logo">
                        📚
                        <h1>Book Store</h1>
                    </a>

                    <div class="search-wrapper">
                        <span class="search-icon">🔍</span>
                        <form action="${pageContext.request.contextPath}/home" method="get">

                            <input type="text" name="keyword" class="input" placeholder="Search books..."
                                value="${param.keyword}" autocomplete="off">

                        </form>
                    </div>

                    <div class="header-actions">

                        <a href="${pageContext.request.contextPath}/cart" class="cart-btn">
                            🛒
                            <c:set var="cartSize" value="0" />

                            <c:if test="${not empty sessionScope.cart}">
                                <c:set var="cartSize" value="${sessionScope.cart.size()}" />
                            </c:if>

                            <span class="cart-badge">${cartSize}</span>
                        </a>

                        <a href="${pageContext.request.contextPath}/order-history" class="sidebar-link">
                            <h4>Order History</h4>
                        </a>

                    </div>

                </div>
            </header>

            <!-- HOME LAYOUT -->
            <div class="home-layout">

                <!-- SIDEBAR -->
                <aside class="sidebar">
                    <h2 class="sidebar-title">Categories</h2>

                    <nav class="category-list">

                        <a href="${pageContext.request.contextPath}/home">
                            <button class="category-btn ${empty activeCategoryId ? 'active' : ''}">
                                All Books
                            </button>
                        </a>

                        <c:if test="${not empty categories}">
                            <c:forEach var="c" items="${categories}">
                                <a href="${pageContext.request.contextPath}/home?categoryId=${c.categoryId}">
                                    <button class="category-btn ${activeCategoryId == c.categoryId ? 'active' : ''}">
                                        ${c.categoryName}
                                    </button>
                                </a>
                            </c:forEach>
                        </c:if>

                    </nav>

                    <!-- HIỂN THỊ KHI ĐÃ LOGIN -->
                    <div class="sidebar-user-section">

                        <c:choose>
                            <c:when test="${sessionScope.user == null}">
                                <a href="${pageContext.request.contextPath}/login" class="sidebar-link">
                                    <h4>Login</h4>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/logout" class="sidebar-link">
                                    <h4>Logout</h4>
                                </a>
                                <a href="${pageContext.request.contextPath}/change-password" class="sidebar-link">
                                    <h4>Change Password</h4>
                                </a>
                            </c:otherwise>
                        </c:choose>

                    </div>

                </aside>

                <!-- MAIN CONTENT -->
                <main class="main-content">

                    <div class="results-header">
                        <h1 class="page-title">${activeCategoryName}</h1>
                        <p class="result-count">
                            Showing ${(currentPage - 1) * 10 + 1}
                            -
                            ${(currentPage - 1) * 10 + books.size()}
                            of ${totalBooks} books
                        </p>
                    </div>

                    <div class="book-grid">

                        <c:forEach var="book" items="${books}">

                            <div class="book-card">

                                <a
                                    href="${pageContext.request.contextPath}/book-detail?id=${book.bookId}&returnUrl=${currentUrl}">
                                    <img src="${pageContext.request.contextPath}/images/${book.imageUrl}"
                                        alt="${book.title}" class="book-image">
                                </a>

                                <h3 class="book-title">${book.title}</h3>
                                <p class="book-author">${book.author}</p>
                                <span class="book-price">$${book.price}</span>
                                <a
                                    href="${pageContext.request.contextPath}/book-detail?id=${book.bookId}&returnUrl=${currentUrl}">
                                    <button class="book-btn">
                                        View Details
                                    </button>
                                </a>

                            </div>

                        </c:forEach>

                    </div>

                    <div class="pagination">

                        <!-- PREVIOUS -->
                        <c:if test="${currentPage > 1}">
                            <c:url var="prevPageUrl" value="/home">
                                <c:param name="page" value="${currentPage - 1}" />
                                <c:if test="${not empty param.categoryId}">
                                    <c:param name="categoryId" value="${param.categoryId}" />
                                </c:if>
                                <c:if test="${not empty param.keyword}">
                                    <c:param name="keyword" value="${param.keyword}" />
                                </c:if>
                            </c:url>
                            <a href="${prevPageUrl}">
                                <button class="page-btn">← Prev</button>
                            </a>
                        </c:if>

                        <!-- PAGE NUMBERS -->
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:url var="pageUrl" value="/home">
                                <c:param name="page" value="${i}" />
                                <c:if test="${not empty param.categoryId}">
                                    <c:param name="categoryId" value="${param.categoryId}" />
                                </c:if>
                                <c:if test="${not empty param.keyword}">
                                    <c:param name="keyword" value="${param.keyword}" />
                                </c:if>
                            </c:url>
                            <a href="${pageUrl}">
                                <button class="page-btn ${i == currentPage ? 'active' : ''}">${i}</button>
                            </a>
                        </c:forEach>

                        <!-- NEXT -->
                        <c:if test="${currentPage < totalPages}">
                            <c:url var="nextPageUrl" value="/home">
                                <c:param name="page" value="${currentPage + 1}" />
                                <c:if test="${not empty param.categoryId}">
                                    <c:param name="categoryId" value="${param.categoryId}" />
                                </c:if>
                                <c:if test="${not empty param.keyword}">
                                    <c:param name="keyword" value="${param.keyword}" />
                                </c:if>
                            </c:url>
                            <a href="${nextPageUrl}">
                                <button class="page-btn">Next →</button>
                            </a>
                        </c:if>

                    </div>

                </main>
            </div>

        </body>

        </html>