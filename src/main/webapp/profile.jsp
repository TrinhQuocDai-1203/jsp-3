<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Quản lý hồ sơ người dùng</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 700px;
            margin: 40px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        h1 {
            text-align: center;
            color: #333;
        }

        form p {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 6px;
            font-weight: bold;
        }

        input[type="email"],
        input[type="number"],
        select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        input[type="file"] {
            margin-top: 5px;
        }

        img {
            margin-top: 10px;
            border-radius: 5px;
        }

        .btn {
            text-align: center;
        }

        button {
            background-color: #007bff;
            border: none;
            padding: 10px 20px;
            color: white;
            border-radius: 5px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }

        .error {
            color: red;
            text-align: center;
        }

        .back-link {
            display: block;
            margin-top: 20px;
            text-align: center;
            color: #555;
        }

        .back-link:hover {
            color: #000;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Quản lý hồ sơ người dùng</h1>
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>
    <form action="profile" method="post" enctype="multipart/form-data">
        <p><strong>Tên người dùng:</strong> ${user.username}</p>
        <p>
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" value="${user.email}" required>
        </p>
        <p>
            <label for="birthYear">Năm sinh:</label>
            <input type="number" id="birthYear" name="birthYear" value="${user.birthYear}" required>
        </p>
        <p>
            <label for="provinceId">Tỉnh/Thành phố:</label>
            <select id="provinceId" name="provinceId" required>
                <c:forEach items="${provinces}" var="province">
                    <option value="${province.id}" ${user.provinceId == province.id ? 'selected' : ''}>${province.name}</option>
                </c:forEach>
            </select>
        </p>
        <p>
            <label for="avatar">Ảnh đại diện:</label>

            <input type="file" id="avatar" name="avatar" accept="image/jpeg,image/png">
            <c:if test="${not empty user.avatar}">
                <img src="${user.avatar}" alt="Avatar" width="100">
            </c:if>
        </p>
        <div class="btn">
            <button type="submit">Lưu thay đổi</button>
        </div>
    </form>
    <a class="back-link" href="home">← Quay lại trang chủ</a>
</div>
</body>
</html>
