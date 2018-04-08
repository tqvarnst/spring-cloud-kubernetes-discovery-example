package com.redhat.coolstore.service;

import java.util.List;

import com.redhat.coolstore.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    private RowMapper<Product> rowMapper = (rs, rowNum) -> new Product(
            rs.getString("ITEMID"),
            rs.getString("NAME"),
            rs.getString("DESCRIPTION"),
            rs.getDouble("PRICE"));


    public List<Product> readAll() {
        return jdbcTemplate.query("SELECT * FROM CATALOG", rowMapper);
    }

    public Product findById(String id) {
        return jdbcTemplate.queryForObject("SELECT * FROM CATALOG WHERE ITEMID = '" + id + "'", rowMapper);
    }

}
