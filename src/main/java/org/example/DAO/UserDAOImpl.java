package org.example.DAO;

import org.example.model.User;
import org.example.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserDAOImpl implements UserDAO {
    private final DatabaseConnection databaseConnection = new DatabaseConnection();
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)$"
    );

    @Override
    public User findByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRowToUser(resultSet);
            }
        }
        return null;
    }

    @Override
    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, role, created_at, email, birth_year, province_id, avatar) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole());
            statement.setTimestamp(4, Timestamp.valueOf(user.getCreatedAt()));
            statement.setString(5, user.getEmail());
            statement.setInt(6, user.getBirthYear());
            if (user.getProvinceId() == 0) {
                statement.setNull(7, Types.INTEGER);
            } else {
                statement.setInt(7, user.getProvinceId());
            }
            statement.setString(8, user.getAvatar());
            statement.executeUpdate();
        }
    }

    @Override
    public User findById(Long id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRowToUser(resultSet);
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(mapRowToUser(resultSet));
            }
        }
        return users;
    }

    @Override
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRowToUser(resultSet);
            }
        }
        return null;
    }

    @Override
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, email = ?, birth_year = ?, province_id = ?, avatar = ? WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole());
            statement.setString(4, user.getEmail());
            statement.setInt(5, user.getBirthYear());
            if (user.getProvinceId() == 0) {
                statement.setNull(6, Types.INTEGER);
            } else {
                statement.setInt(6, user.getProvinceId());
            }
            statement.setString(7, user.getAvatar());
            statement.setLong(8, user.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public List<User> searchByUsername(String query) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE username LIKE ?";
        System.out.println("Executing searchByUsername with query: " + query);
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + query + "%");
            System.out.println("SQL query: " + sql + ", param: %" + query + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(mapRowToUser(resultSet));
                System.out.println("Found user: " + mapRowToUser(resultSet).getUsername());
            }
            System.out.println("Found " + users.size() + " users for query: " + query);
        }
        return users;
    }

    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(resultSet.getString("role"));
        user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        user.setEmail(resultSet.getString("email"));
        user.setBirthYear(resultSet.getInt("birth_year"));
        user.setProvinceId(resultSet.getInt("province_id"));
        user.setAvatar(resultSet.getString("avatar"));
        return user;
    }

    @Override
    public boolean isEmailValid(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public boolean isEmailExists(String email, Long excludeUserId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND id != ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            if (excludeUserId == null) {
                statement.setNull(2, Types.BIGINT);
            } else {
                statement.setLong(2, excludeUserId);
            }
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }
}