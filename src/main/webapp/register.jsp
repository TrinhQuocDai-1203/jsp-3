<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng ký</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 500px;
            margin: 50px auto;
            padding: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
        }
        .form-group input,
        .form-group select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .error {
            color: red;
            margin-bottom: 15px;
        }
        .btn {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .btn:hover {
            background-color: #45a049;
        }
        .link {
            margin-top: 15px;
            text-align: center;
        }
    </style>
</head>
<body>
<h2>Đăng ký tài khoản</h2>
<!-- Tác dụng: Hiển thị chữ lớn "Đăng ký tài khoản" trên trang -->
<!-- Đây là tiêu đề để bạn biết trang này dùng để làm gì -->

<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>
<!-- Tác dụng: Kiểm tra xem máy chủ có gửi thông báo lỗi không -->
<!-- Nếu có lỗi (ví dụ: "Tên đăng nhập đã tồn tại"), nó sẽ hiện lên bằng chữ đỏ để bạn biết -->

<form action="register" method="post">
    <!-- Tác dụng: Tạo biểu mẫu để bạn nhập thông tin đăng ký -->
    <!-- "action='register'": Khi bạn nhấn "Đăng ký", dữ liệu được gửi đến "/register" trên máy chủ -->
    <!-- "method='post'": Dữ liệu được gửi kín đáo qua POST, không hiện trên URL, để bảo mật thông tin -->

    <div class="form-group">
        <label for="username">Tên đăng nhập:</label>
        <!-- Tác dụng: Hiển thị chữ "Tên đăng nhập:" để bạn biết ô này nhập gì -->
        <input type="text" id="username" name="username" required>
        <!-- Tác dụng: Tạo ô để bạn gõ tên đăng nhập, ví dụ: "Nam123" -->
        <!-- "name='username'": Đặt tên "username" cho dữ liệu này để máy chủ nhận biết -->
        <!-- "required": Bắt buộc phải nhập, nếu không nhập thì form không gửi được -->
    </div>

    <div class="form-group">
        <label for="password">Mật khẩu:</label>
        <!-- Tác dụng: Hiển thị chữ "Mật khẩu:" để bạn biết ô này nhập gì -->
        <input type="password" id="password" name="password" required>
        <!-- Tác dụng: Tạo ô để bạn gõ mật khẩu, ví dụ: "abc123" -->
        <!-- "type='password'": Ẩn chữ bạn gõ thành dấu chấm (•••••) để bảo mật -->
        <!-- "name='password'": Đặt tên "password" cho dữ liệu này để máy chủ nhận biết -->
        <!-- "required": Bắt buộc phải nhập, không để trống -->
    </div>

    <div class="form-group">
        <label for="email">Email:</label>
        <!-- Tác dụng: Hiển thị chữ "Email:" để bạn biết ô này nhập gì -->
        <input type="email" id="email" name="email" required>
        <!-- Tác dụng: Tạo ô để bạn gõ email, ví dụ: "test@example.com" -->
        <!-- "type='email'": Đảm bảo định dạng email cơ bản (có @ và domain) -->
        <!-- "name='email'": Đặt tên "email" cho dữ liệu này để máy chủ nhận biết -->
        <!-- "required": Bắt buộc phải nhập -->
    </div>

    <div class="form-group">
        <label for="birthYear">Năm sinh:</label>
        <!-- Tác dụng: Hiển thị chữ "Năm sinh:" để bạn biết ô này nhập gì -->
        <input type="number" id="birthYear" name="birthYear" required>
        <!-- Tác dụng: Tạo ô để bạn gõ năm sinh, ví dụ: "2005" -->
        <!-- "type='number'": Chỉ cho phép nhập số -->
        <!-- "name='birthYear'": Đặt tên "birthYear" cho dữ liệu này để máy chủ nhận biết -->
        <!-- "required": Bắt buộc phải nhập -->
    </div>

    <div class="form-group">
        <label for="provinceId">Nơi ở:</label>
        <!-- Tác dụng: Hiển thị chữ "Nơi ở:" để bạn biết ô này chọn gì -->
        <select id="provinceId" name="provinceId" required>
            <!-- Tác dụng: Tạo danh sách thả xuống (combo box) để bạn chọn tỉnh/thành phố -->
            <!-- "name='provinceId'": Đặt tên "provinceId" cho dữ liệu này để máy chủ nhận biết -->
            <!-- "required": Bắt buộc phải chọn một giá trị -->
            <option value="">Chọn tỉnh/thành phố</option>
            <!-- Tác dụng: Hiển thị lựa chọn mặc định, không có giá trị -->
            <c:forEach items="${provinces}" var="province">
                <!-- Tác dụng: Lặp qua danh sách tỉnh/thành phố từ máy chủ -->
                <option value="${province.id}">${province.name}</option>
                <!-- Tác dụng: Tạo một mục trong danh sách thả xuống -->
                <!-- "value='${province.id}'": Gửi ID của tỉnh/thành phố khi bạn chọn -->
                <!-- "${province.name}": Hiển thị tên tỉnh/thành phố để bạn thấy -->
            </c:forEach>
        </select>
    </div>

    <button type="submit" class="btn">Đăng ký</button>
    <!-- Tác dụng: Tạo nút "Đăng ký" để gửi dữ liệu bạn vừa nhập -->
    <!-- Khi nhấp, dữ liệu được gửi đến "/register" để máy chủ tạo tài khoản mới -->
</form>

<div class="link">
    <p>Đã có tài khoản? <a href="login">Đăng nhập</a></p>
    <!-- Tác dụng: Hiển thị dòng chữ "Đã có tài khoản?" và nút liên kết "Đăng nhập" -->
    <!-- Nếu bạn đã có tài khoản, nhấp "Đăng nhập" sẽ chuyển đến trang đăng nhập ("/login") -->
</div>
</body>
</html>