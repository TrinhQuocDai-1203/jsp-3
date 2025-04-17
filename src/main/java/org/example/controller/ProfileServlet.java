package org.example.controller;

import org.example.DAO.ProvinceDAO;
import org.example.DAO.ProvinceDAOImpl;
import org.example.DAO.UserDAO;
import org.example.DAO.UserDAOImpl;
import org.example.model.Province;
import org.example.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/profile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 200, maxRequestSize = 1024 * 1024 * 5)
public class ProfileServlet extends HttpServlet {
    private UserDAO userDAO;
    private ProvinceDAO provinceDAO;
    private static final String UPLOAD_DIR = "uploads";

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        provinceDAO = new ProvinceDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("login");
                return;
            }

            List<Province> provinces = provinceDAO.getAllProvinces();
            request.setAttribute("provinces", provinces);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Lỗi khi lấy danh sách tỉnh/thành phố", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            String email = request.getParameter("email");
            int birthYear = Integer.parseInt(request.getParameter("birthYear"));
            int provinceId = Integer.parseInt(request.getParameter("provinceId"));

            // Kiểm tra email hợp lệ
            if (!userDAO.isEmailValid(email)) {
                request.setAttribute("error", "Email không hợp lệ!");
                doGet(request, response);
                return;
            }

            // Kiểm tra email đã tồn tại (bỏ qua chính người dùng hiện tại)
            if (userDAO.isEmailExists(email, user.getId())) {
                request.setAttribute("error", "Email đã được sử dụng!");
                doGet(request, response);
                return;
            }

            // Kiểm tra tuổi (trên 15 tuổi tính đến ngày 17/04/2025)
            int currentYear = 2025; // Ngày hiện tại là 17/04/2025
            int age = currentYear - birthYear;
            if (age < 15) {
                request.setAttribute("error", "Bạn phải trên 15 tuổi!");
                doGet(request, response);
                return;
            }

            // Xử lý tải ảnh đại diện lên
            String avatarPath = user.getAvatar();
            Part filePart = request.getPart("avatar");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = filePart.getSubmittedFileName();
                String fileExt = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                if (!fileExt.equals(".jpg") && !fileExt.equals(".png")) {
                    request.setAttribute("error", "Chỉ cho phép file JPG và PNG!");
                    doGet(request, response);
                    return;
                }

                String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();

                avatarPath = UPLOAD_DIR + File.separator + user.getId() + fileExt;
                filePart.write(uploadPath + File.separator + user.getId() + fileExt);
            }

            user.setEmail(email);
            user.setBirthYear(birthYear);
            user.setProvinceId(provinceId);
            user.setAvatar(avatarPath);
            userDAO.updateUser(user); // Phương thức này có thể ném SQLException

            session.setAttribute("user", user);
            response.sendRedirect("profile.jsp");
        } catch (SQLException e) {
            throw new ServletException("Lỗi khi cập nhật thông tin người dùng", e);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Năm sinh phải là một số hợp lệ!");
            doGet(request, response);
        }
    }
}