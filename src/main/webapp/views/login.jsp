<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Login - Book Store</title>

            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base/base.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/login.css">
        </head>

        <body>

            <c:if test="${not empty sessionScope.error}">
                <div class="error">${sessionScope.error}</div>
                <c:remove var="error" scope="session" />
            </c:if>

            <div class="login-wrapper">

                <div class="login-box">

                    <a class="back-btn" href="${pageContext.request.contextPath}/home">
                        ← Back to Home
                    </a>

                    <!-- Logo & Title -->
                    <div class="login-header">
                        <div class="logo-icon">📚</div>
                        <h1>Welcome Back</h1>
                        <p>Sign in to your account</p>
                    </div>

                    <!-- Login Form -->
                    <form action="login" method="post" class="login-form">

                        <!-- Email -->
                        <div class="form-group">
                            <label>Email Address</label>
                            <div class="input-wrapper">
                                <span class="input-icon">✉</span>
                                <input type="email" name="email" placeholder="you@example.com" required>
                            </div>
                        </div>

                        <!-- Password -->
                        <div class="form-group">
                            <label>Password</label>
                            <div class="input-wrapper">
                                <span class="input-icon">🔒</span>
                                <input type="password" name="password" placeholder="••••••••" required>
                            </div>
                        </div>

                        <!-- Button -->
                        <button type="submit" class="login-btn">
                            Sign In
                        </button>

                    </form>

                    <!-- Register -->
                    <div class="register-link">
                        <p>
                            Don't have an account?
                            <a href="register">Create Account</a>
                        </p>
                    </div>

                </div>

            </div>

        </body>

        </html>