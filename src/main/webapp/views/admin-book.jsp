<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

            <!DOCTYPE html>
            <html>

            <head>
                <title>Admin Panel</title>

                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base/base.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/admin.css">
            </head>

            <body data-context-path="${pageContext.request.contextPath}">

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
                                <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-item">📊
                                    Dashboard</a>
                                <a href="${pageContext.request.contextPath}/admin/order" class="nav-item">📦 Order</a>
                                <a href="${pageContext.request.contextPath}/admin/book" class="nav-item active">📚
                                    Book</a>
                                <a href="${pageContext.request.contextPath}/admin/user" class="nav-item">👤 User</a>
                            </nav>
                        </div>

                        <div class="sidebar-bottom">
                            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">🚪 Logout</a>
                        </div>
                    </aside>

                    <!-- MAIN -->
                    <main class="admin-main">

                        <div class="admin-header">
                            <div>
                                <h2>Books Management</h2>
                                <p>Manage your book inventory</p>
                            </div>

                            <div class="header-actions">
                                <form action="${pageContext.request.contextPath}/admin/book" method="get"
                                    class="search-form">
                                    <input type="text" name="search" value="${param.search}"
                                        placeholder="Search books...">
                                </form>
                                <button class="admin-btn" onclick="openAddModal()">Add New Book</button>
                            </div>
                        </div>

                        <!-- TABLE -->
                        <table class="admin-table">
                            <thead>
                                <tr class="table-head">
                                    <th>Book</th>
                                    <th>Name</th>
                                    <th>Author</th>
                                    <th>Category</th>
                                    <th>Price</th>
                                    <th>Quantity</th>
                                    <th>ISBN</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="book" items="${books}">
                                    <tr>
                                        <td>
                                            <img src="${pageContext.request.contextPath}/images/${book.imageUrl}"
                                                class="book-thumb">
                                        </td>

                                        <td>
                                            <c:out value="${book.title}" />
                                        </td>
                                        <td>
                                            <c:out value="${book.author}" />
                                        </td>
                                        <td>
                                            <c:out value="${book.categoryName}" />
                                        </td>

                                        <td>$
                                            <c:out value="${book.price}" />
                                        </td>

                                        <td>
                                            <span class="stock-number">${book.stock}</span>
                                        </td>

                                        <td>
                                            <c:out value="${book.isbn}" />
                                        </td>

                                        <td class="actions-cell">
                                            <div class="actions">

                                                <button class="icon-btn edit-btn" onclick="openEditModal(this)"
                                                    data-id="${book.bookId}" data-title="${fn:escapeXml(book.title)}"
                                                    data-author="${fn:escapeXml(book.author)}"
                                                    data-category="${book.categoryId}" data-price="${book.price}"
                                                    data-stock="${book.stock}" data-isbn="${fn:escapeXml(book.isbn)}"
                                                    data-image="${fn:escapeXml(book.imageUrl)}"
                                                    data-description="${fn:escapeXml(book.description)}">
                                                    ✏️
                                                </button>

                                                <button class="icon-btn adjust-btn" onclick="openStockModal(this)"
                                                    data-id="${book.bookId}" data-stock="${book.stock}">
                                                    ±
                                                </button>

                                                <button class="icon-btn delete-btn" onclick="openDeleteModal(this)"
                                                    data-id="${book.bookId}" data-title="${fn:escapeXml(book.title)}"
                                                    data-author="${fn:escapeXml(book.author)}"
                                                    data-category="${fn:escapeXml(book.categoryName)}"
                                                    data-isbn="${fn:escapeXml(book.isbn)}">
                                                    🗑
                                                </button>

                                            </div>
                                        </td>

                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <!-- PAGINATION -->
                        <div class="pagination">

                            <c:if test="${currentPage > 1}">
                                <a
                                    href="${pageContext.request.contextPath}/admin/book?page=${currentPage - 1}&search=${param.search}">
                                    <button class="page-btn">← Prev</button>
                                </a>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <a href="${pageContext.request.contextPath}/admin/book?page=${i}&search=${param.search}"
                                    class="page-btn ${i == currentPage ? 'active-page' : ''}">
                                    ${i}
                                </a>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a
                                    href="${pageContext.request.contextPath}/admin/book?page=${currentPage + 1}&search=${param.search}">
                                    <button class="page-btn">Next →</button>
                                </a>
                            </c:if>

                        </div>

                        <!-- BOOK MODAL -->
                        <div class="modal-overlay" id="bookModal">
                            <div class="modal large-modal">
                                <div class="modal-header">
                                    <h3 id="modalTitle">Add New Book</h3> <button class="close-btn"
                                        onclick="closeModal()">✕</button>
                                </div>
                                <form action="${pageContext.request.contextPath}/admin/book/add-edit-book" method="post"
                                    id="bookForm">
                                    <div class="modal-body"> <input type="hidden" id="bookId" name="bookId">
                                        <div class="form-group"> <label>Book Title</label> <input type="text"
                                                id="bookTitle" name="title" required> </div>
                                        <div class="form-group"> <label>Author</label> <input type="text"
                                                id="bookAuthor" name="author" required> </div>
                                        <div class="form-row">
                                            <div class="form-group"> <label>Category</label> <select id="bookCategory"
                                                    name="categoryId">
                                                    <c:forEach var="category" items="${categories}">
                                                        <option value="${category.categoryId}">${category.categoryName}
                                                        </option>
                                                    </c:forEach>
                                                </select> </div>
                                            <div class="form-group"> <label>Price</label> <input type="number"
                                                    id="bookPrice" name="price" step="0.01" required> </div>
                                        </div>
                                        <div class="form-group"> <label>ISBN</label> <input type="text" id="bookISBN"
                                                name="isbn"> </div>
                                        <div class="form-group" id="stockGroup">
                                            <label>Stock Quantity</label>
                                            <input type="number" id="bookStock" name="stock" min="0">
                                        </div>
                                        <div class="form-group"> <label>Image URL</label> <input type="text"
                                                id="bookImageURL" name="imageUrl"> </div>
                                        <div class="form-group"> <label>Description</label> <textarea rows="4"
                                                id="bookDescription" name="description"></textarea> </div>
                                    </div>
                                    <div class="modal-footer"> <button type="button" class="cancel-btn"
                                            onclick="closeModal()">Cancel</button> <button type="submit"
                                            class="save-btn" id="modalActionBtn">Add Book</button> </div>
                                </form>
                            </div>
                        </div>

                        <!-- STOCK MODAL -->
                        <div class="modal-overlay" id="stockModal">
                            <div class="modal small-modal">
                                <div class="modal-header">
                                    <h3>Update Quantity</h3>
                                    <button class="close-btn" onclick="closeStockModal()">✕</button>
                                </div>

                                <form id="stockForm" method="post"
                                    action="${pageContext.request.contextPath}/admin/book/update-stock">

                                    <div class="modal-body">
                                        <p>Current Stock: <strong id="currentStock">0</strong></p>

                                        <input type="hidden" name="bookId" id="stockBookId">

                                        <div class="form-group">
                                            <input type="number" id="stockInput" name="quantity"
                                                placeholder="Update quantity" required>
                                        </div>
                                    </div>

                                    <div class="modal-footer">
                                        <button type="button" onclick="closeStockModal()"
                                            class="cancel-btn">Cancel</button>

                                        <button type="button" class="save-btn" onclick="submitStock('add')">+</button>
                                        <button type="button" class="save-btn" onclick="submitStock('minus')">-</button>
                                    </div>

                                </form>
                            </div>
                        </div>

                        <!-- DELETE MODAL -->
                        <div class="modal-overlay" id="deleteModal">
                            <div class="modal small-modal">
                                <div class="modal-header">
                                    <h3>Delete Book</h3> <button class="close-btn"
                                        onclick="closeDeleteModal()">✕</button>
                                </div>
                                <div class="modal-body">
                                    <p style="margin-bottom:15px;"> Are you sure you want to delete this book? </p>
                                    <div class="form-group"> <label>Title</label> <input id="deleteTitle" readonly>
                                    </div>
                                    <div class="form-group"> <label>Author</label> <input id="deleteAuthor" readonly>
                                    </div>
                                    <div class="form-group"> <label>Category</label> <input id="deleteCategory"
                                            readonly> </div>
                                    <div class="form-group"> <label>ISBN</label> <input id="deleteISBN" readonly> </div>
                                </div>
                                <div class="modal-footer"> <button class="cancel-btn"
                                        onclick="closeDeleteModal()">Cancel</button> <button class="save-btn"
                                        onclick="deleteBook()">Delete Book</button> </div>
                            </div>
                        </div>

                    </main>
                </div>

                <script>
                    const contextPath = "${pageContext.request.contextPath}";
                    let deleteBookId = null;

                    function openAddModal() {
                        const modal = document.getElementById("bookModal");

                        document.getElementById("modalTitle").innerText = "Add New Book";
                        document.getElementById("modalActionBtn").innerText = "Add Book";

                        document.getElementById("bookId").value = "";
                        document.getElementById("bookTitle").value = "";
                        document.getElementById("bookAuthor").value = "";
                        document.getElementById("bookPrice").value = "";
                        document.getElementById("bookISBN").value = "";
                        document.getElementById("stockGroup").style.display = "block";
                        document.getElementById("bookDescription").value = "";

                        modal.classList.add("active");
                    }

                    function openEditModal(btn) {
                        const modal = document.getElementById("bookModal");

                        document.getElementById("modalTitle").innerText = "Edit Book";
                        document.getElementById("modalActionBtn").innerText = "Update Book";

                        document.getElementById("bookId").value = btn.dataset.id;
                        document.getElementById("bookTitle").value = btn.dataset.title;
                        document.getElementById("bookAuthor").value = btn.dataset.author;
                        document.getElementById("bookCategory").value = btn.dataset.category;
                        document.getElementById("bookPrice").value = btn.dataset.price;
                        document.getElementById("bookISBN").value = btn.dataset.isbn;
                        document.getElementById("stockGroup").style.display = "none";
                        document.getElementById("bookImageURL").value = btn.dataset.image;
                        document.getElementById("bookDescription").value = btn.dataset.description;

                        modal.classList.add("active");
                    }

                    function closeModal() {
                        document.getElementById("bookModal").classList.remove("active");
                    }

                    function openStockModal(btn) {
                        const modal = document.getElementById("stockModal");

                        const stock = btn.dataset.stock;
                        const bookId = btn.dataset.id;

                        document.getElementById("currentStock").innerText = stock;
                        document.getElementById("stockBookId").value = bookId;
                        document.getElementById("stockInput").value = "";

                        modal.classList.add("active");
                    }

                    function closeStockModal() {
                        document.getElementById("stockModal").classList.remove("active");
                    }

                    function submitStock(type) {
                        const input = document.getElementById("stockInput");

                        if (!input.value || input.value <= 0) {
                            alert("Please enter valid quantity!");
                            return;
                        }

                        let actionInput = document.getElementById("stockAction");

                        if (!actionInput) {
                            actionInput = document.createElement("input");
                            actionInput.type = "hidden";
                            actionInput.name = "action";
                            actionInput.id = "stockAction";
                            document.getElementById("stockForm").appendChild(actionInput);
                        }

                        actionInput.value = type;

                        document.getElementById("stockForm").submit();
                    }

                    function openDeleteModal(btn) {
                        const modal = document.getElementById("deleteModal");

                        deleteBookId = btn.dataset.id;

                        document.getElementById("deleteTitle").value = btn.dataset.title;
                        document.getElementById("deleteAuthor").value = btn.dataset.author;
                        document.getElementById("deleteCategory").value = btn.dataset.category;
                        document.getElementById("deleteISBN").value = btn.dataset.isbn;

                        modal.classList.add("active");
                    }

                    function closeDeleteModal() {
                        document.getElementById("deleteModal").classList.remove("active");
                    }

                    function deleteBook() {
                        if (deleteBookId) {
                            const form = document.createElement('form');
                            form.method = 'POST';
                            form.action = contextPath + "/admin/book/delete";

                            const idInput = document.createElement('input');
                            idInput.type = 'hidden';
                            idInput.name = 'bookId';
                            idInput.value = deleteBookId;

                            form.appendChild(idInput);
                            document.body.appendChild(form);
                            form.submit();
                        }
                    }
                </script>

            </body>

            </html>