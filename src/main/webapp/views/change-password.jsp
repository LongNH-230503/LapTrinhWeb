<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <html>

        <head>

            <title>Change Password</title>

            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base/base.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/change-password.css">

        </head>

        <body>

            <div class="container">

                <a href="${pageContext.request.contextPath}/home" class="back-btn">
                    ← Back to Home
                </a>

                <h2>🔐 Change Password</h2>

                <div>
                    <c:if test="${not empty error}">
                        <div class="error">
                            ${error}
                        </div>
                    </c:if>

                    <c:if test="${not empty success}">
                        <div class="success">
                            ${success}
                        </div>
                    </c:if>
                </div>
                
                <form method="post">

                    <input type="password" name="currentPassword" placeholder="Current Password" required>

                    <input type="password" name="newPassword" placeholder="New Password" required>

                    <input type="password" name="confirmPassword" placeholder="Confirm Password" required>

                    <button type="submit">
                        Update Password
                    </button>

                </form>

            </div>

        </body>

        </html>