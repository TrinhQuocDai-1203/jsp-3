package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.ProvinceDAO;
import org.example.DAO.ProvinceDAOImpl;
import org.example.DAO.UserDAO;
import org.example.DAO.UserDAOImpl;
import org.example.model.Province;
import org.example.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO;
    private ProvinceDAO provinceDAO;

    // Khởi tạo các DAO trong phương thức init
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        provinceDAO = new ProvinceDAOImpl();
    }

    // Phương thức doGet xử lý yêu cầu GET để hiển thị trang đăng ký
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Load danh sách tỉnh/thành phố từ bảng provinces
            List<Province> provinces = provinceDAO.getAllProvinces();
            request.setAttribute("provinces", provinces);
            // Chuyển tiếp (forward) yêu cầu đến file register.jsp để hiển thị giao diện đăng ký
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Lỗi khi lấy danh sách tỉnh/thành phố", e);
        }
    }

    // Phương thức doPost xử lý yêu cầu POST khi người dùng gửi form đăng ký
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy thông tin từ form đăng ký mà người dùng gửi lên
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            int birthYear = Integer.parseInt(request.getParameter("birthYear"));
            int provinceId = Integer.parseInt(request.getParameter("provinceId"));

            // Kiểm tra username đã tồn tại
            User existingUser = userDAO.findByUsername(username);
            if (existingUser != null) {
                request.setAttribute("error", "Username đã tồn tại!");
                doGet(request, response);
                return;
            }

            // Kiểm tra email hợp lệ
            if (!userDAO.isEmailValid(email)) {
                request.setAttribute("error", "Email không hợp lệ!");
                doGet(request, response);
                return;
            }

            // Kiểm tra email đã tồn tại
            if (userDAO.isEmailExists(email, null)) {
                request.setAttribute("error", "Email đã được sử dụng!");
                doGet(request, response);
                return;
            }

            // Kiểm tra tuổi (trên 15 tuổi tính đến ngày 17/04/2025)
            int currentYear = 2025; // Ngày hiện tại là 17/04/2025
            int age = currentYear - birthYear;
            if (age < 15) {
                request.setAttribute("error", "Bạn phải trên 15 tuổi để đăng ký!");
                doGet(request, response);
                return;
            }

            // Tạo một đối tượng User mới để lưu thông tin người dùng vừa đăng ký
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.setBirthYear(birthYear);
            newUser.setProvinceId(provinceId);
            newUser.setRole("USER"); // Gán vai trò mặc định là "USER"
            newUser.setCreatedAt(LocalDateTime.now()); // Gán thời gian tạo tài khoản

            // Lưu thông tin người dùng mới vào cơ sở dữ liệu
            userDAO.save(newUser);

            // Chuyển hướng người dùng đến trang đăng nhập sau khi đăng ký thành công
            response.sendRedirect(request.getContextPath() + "/login");

        } catch (Exception e) {
            // Xử lý lỗi nếu có vấn đề trong quá trình đăng ký
            request.setAttribute("error", "Đã xảy ra lỗi khi đăng ký!");
            doGet(request, response);
            e.printStackTrace();
        }
    }
}