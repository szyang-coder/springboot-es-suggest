package com.demo.essuggest.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.demo.essuggest.document.Product;

@Component
public class MysqlUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<Product> getList(){
        String sql = "SELECT id,name,partner,sub,createtime FROM Product";
        return (List<Product>) jdbcTemplate.query(sql, new RowMapper<Product>(){

            @Override
            public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            	Product product = new Product();
            	product.setId(rs.getString("id"));
            	product.setName(rs.getString("name"));
            	product.setPartner(rs.getString("partner"));
            	product.setSub(rs.getString("sub"));
                return product;
            }
        });
    }
}
