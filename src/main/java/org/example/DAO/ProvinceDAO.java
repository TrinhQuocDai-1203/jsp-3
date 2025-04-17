package org.example.DAO;

import org.example.model.Province;

import java.sql.SQLException;
import java.util.List;

public interface ProvinceDAO {
    List<Province> getAllProvinces() throws SQLException;
}