<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <title>Register - Academic Books</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base/base.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/register.css">
    </head>

    <body>

        <!-- Error Message -->
        <% if(request.getAttribute("error") !=null) { %>
            <div class="error-box">
                <%= request.getAttribute("error") %>
            </div>
            <% } %>

                <div class="register-wrapper">

                    <div class="register-box">

                        <a class="back-btn" href="${pageContext.request.contextPath}/home">
                            ← Back to Home
                        </a>

                        <!-- Logo & Title -->
                        <div class="register-header">
                            <div class="logo-icon">📚</div>
                            <h1>Create Account</h1>
                            <p>Join our bookstore community</p>
                        </div>

                        <!-- Register Form -->
                        <form action="register" method="post" class="register-form">

                            <!-- Full Name -->
                            <div class="form-group">
                                <label>Full Name</label>
                                <div class="input-wrapper">
                                    <span class="input-icon">👤</span>
                                    <input type="text" name="fullName" placeholder="John Doe" required>
                                </div>
                            </div>

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
                                    <input type="password" name="password" placeholder="••••••••" required
                                        minlength="6">
                                </div>
                            </div>

                            <!-- Confirm Password -->
                            <div class="form-group">
                                <label>Confirm Password</label>
                                <div class="input-wrapper">
                                    <span class="input-icon">🔒</span>
                                    <input type="password" name="confirmPassword" placeholder="••••••••" required
                                        minlength="6">
                                </div>
                            </div>

                            <!-- Button -->
                            <button type="submit" class="register-btn">
                                Create Account
                            </button>

                        </form>

                        <!-- Login Link -->
                        <div class="login-link">
                            <p>
                                Already have an account?
                                <a href="login">Sign In</a>
                            </p>
                        </div>

                    </div>

                </div>

    </body>

    </html>