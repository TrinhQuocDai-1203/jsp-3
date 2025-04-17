package org.example.DAO;

import org.example.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    User findByUsernameAndPassword(String username, String password) throws SQLException;
    void save(User user) throws SQLException;
    User findById(Long id) throws SQLException;
    List<User> findAll() throws SQLException;
    User findByUsername(String username) throws SQLException;
    void updateUser(User user) throws SQLException; // Thêm throws SQLException
    List<User> searchByUsername(String query) throws SQLException;
    boolean isEmailValid(String email);
    boolean isEmailExists(String email, Long excludeUserId) throws SQLException;
}