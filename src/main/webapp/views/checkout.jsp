<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html>

            <head>
                <title>Shipping Information</title>

                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base/base.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout/header.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/checkout.css">
            </head>

            <body>
                <div class="checkout-page">
                    <div class="container">

                        <a href="cart" class="back-to-home">← Back to Cart</a>

                        <h1 class="checkout-title">Checkout</h1>

                        <c:if test="${not empty param.success}">
                            <div class="success-message">
                                🎉 Order placed successfully! You will be redirected to order history.
                            </div>
                        </c:if>

                        <c:if test="${not empty param.error}">
                            <div class="error-message">

                                <c:if test="${param.error == 'insufficient_stock'}">
                                    ⚠️ Some items are out of stock.
                                </c:if>

                                <c:if test="${param.error == 'missing_info'}">
                                    ⚠️ Please fill all required fields (address, city, district, phone).
                                </c:if>

                            </div>
                        </c:if>

                        <div class="checkout-layout">

                            <!-- LEFT -->
                            <div class="checkout-form">
                                <form id="checkoutForm" action="${pageContext.request.contextPath}/checkout"
                                    method="post">

                                    <div class="form-box">
                                        <h2>Shipping Information</h2>

                                        <label>Name</label>
                                        <input type="text" value="${sessionScope.user.name}" readonly>

                                        <label>Email</label>
                                        <input type="email" value="${sessionScope.user.email}" readonly>

                                        <label>Address</label>
                                        <input type="text" name="address" required>

                                        <label>Phone</label>
                                        <input type="text" name="phone" required>

                                        <div class="form-row">
                                            <div>
                                                <label>Tỉnh / Thành phố</label>
                                                <input type="hidden" name="city" id="cityInput">
                                                <select id="province" required>
                                                    <option value="">-- Chọn tỉnh --</option>
                                                </select>
                                            </div>

                                            <div>
                                                <label>Quận / Huyện</label>
                                                <input type="hidden" name="state" id="stateInput">
                                                <select id="district" required>
                                                    <option value="">-- Chọn quận --</option>
                                                </select>
                                            </div>
                                        </div>

                                    </div>

                                </form>
                            </div>

                            <!-- RIGHT -->
                            <div class="order-summary">
                                <h2>Order Summary</h2>

                                <c:forEach var="item" items="${sessionScope.cart}">
                                    <div class="summary-item">
                                        <img src="${pageContext.request.contextPath}/images/${item.book.imageUrl}">
                                        <div>
                                            <p>${item.book.title}</p>
                                            <span>Quantity: ${item.quantity}</span>
                                        </div>
                                        <p>$${item.total}</p>
                                    </div>
                                </c:forEach>

                                <c:set var="subtotal" value="0" />
                                <c:forEach var="item" items="${sessionScope.cart}">
                                    <c:set var="subtotal" value="${subtotal + item.total}" />
                                </c:forEach>

                                <div class="summary-pricing">
                                    <div class="total">
                                        <span>Total</span>
                                        <span>$
                                            <fmt:formatNumber value="${subtotal}" type="number" minFractionDigits="2"
                                                maxFractionDigits="2" />
                                        </span>
                                    </div>
                                </div>

                                <button type="submit" form="checkoutForm" class="primary-btn"
                                    onclick="validateAndSubmit(this)">
                                    Confirm
                                </button>
                            </div>

                        </div>

                    </div>

                </div>
                <script>
                    function validateAndSubmit(btn) {
                        const provinceSelect = document.getElementById("province");
                        const districtSelect = document.getElementById("district");

                        const selectedProvince = provinceSelect.options[provinceSelect.selectedIndex];
                        const selectedDistrict = districtSelect.options[districtSelect.selectedIndex];

                        document.getElementById("cityInput").value = selectedProvince?.value || "";
                        document.getElementById("stateInput").value = selectedDistrict?.value || "";

                        const city = document.getElementById("cityInput").value;
                        const state = document.getElementById("stateInput").value;
                        const address = document.querySelector("input[name='address']").value;
                        const phone = document.querySelector("input[name='phone']").value;

                        if (!address || !phone || !city || !state) {
                            alert("Vui lòng nhập đầy đủ thông tin!");
                            return;
                        }

                        btn.disabled = true;
                        btn.innerText = "Processing...";

                        // Submit sau khi đã disabled button
                        document.getElementById("checkoutForm").submit();
                    }

                    document.addEventListener("DOMContentLoaded", function () {

                        const api = "https://provinces.open-api.vn/api";

                        const provinceSelect = document.getElementById("province");
                        const districtSelect = document.getElementById("district");

                        const cityInput = document.getElementById("cityInput");
                        const stateInput = document.getElementById("stateInput");

                        // ===== LOAD TỈNH =====
                        fetch(api + "/p/")
                            .then(res => res.json())
                            .then(data => {
                                data.forEach(p => {
                                    const option = document.createElement("option");
                                    option.value = p.name;
                                    option.textContent = p.name;
                                    option.dataset.code = p.code;
                                    provinceSelect.appendChild(option);
                                });
                            });

                        // ===== CHỌN TỈNH =====
                        provinceSelect.addEventListener("change", function () {

                            const selected = this.options[this.selectedIndex];
                            const code = selected.dataset.code;

                            cityInput.value = selected.value; // bind

                            districtSelect.innerHTML = `<option value="">-- Chọn quận --</option>`;

                            if (!code) return;

                            fetch(api + "/p/" + code + "?depth=2")
                                .then(res => res.json())
                                .then(data => {
                                    data.districts.forEach(d => {
                                        const option = document.createElement("option");
                                        option.value = d.name;
                                        option.textContent = d.name;
                                        option.dataset.code = d.code;
                                        districtSelect.appendChild(option);
                                    });
                                });
                        });

                        // ===== CHỌN HUYỆN =====
                        districtSelect.addEventListener("change", function () {

                            const selected = this.options[this.selectedIndex];

                            stateInput.value = selected.value;

                        });
                    });
                </script>
            </body>