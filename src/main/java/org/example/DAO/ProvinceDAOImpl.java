package org.example.DAO;

import org.example.model.Province;
import org.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProvinceDAOImpl implements ProvinceDAO {
    private final DatabaseConnection databaseConnection = new DatabaseConnection();

    @Override
    public List<Province> getAllProvinces() throws SQLException {
        List<Province> provinces = new ArrayList<>();
        String sql = "SELECT * FROM provinces";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Province province = new Province();
                province.setId(resultSet.getInt("idProvince"));
                province.setName(resultSet.getString("nameProvince"));
                province.setNote(resultSet.getString("note"));
                provinces.add(province);
            }
        }
        return provinces;
    }
}