package com.demo.essuggest.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        String sql = "SELECT id,term,keyword,clickUrl,imageUrl,impressionUrl,labelRequired,suggest,createtime FROM Product";
        return (List<Product>) jdbcTemplate.query(sql, new RowMapper<Product>(){

            @Override
            public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            	Product product = new Product();
            	product.setId(rs.getString("id"));
            	product.setTerm(rs.getString("term"));
            	product.setKeyword(rs.getString("keyword"));
            	product.setClickUrl(rs.getString("clickUrl"));
            	product.setImageUrl(rs.getString("imageUrl"));
            	product.setImpressionUrl(rs.getString("impressionUrl"));
            	product.setLabelRequired(rs.getString("labelRequired"));
            	product.setSuggest(rs.getString("suggest"));
            	product.setCreateTime(rs.getDate("createtime"));
                return product;
            }
        });
    }
    
    public void save(List<Product> products){
        String sql = "INSERT INTO product(term,keyword,suggest,clickUrl,imageUrl,impressionUrl,labelRequired) VALUES(?,?,?,?,?,?,?)";
        List<Object[]> list = new ArrayList<Object[]>();
        for(Product product : products){
        	list.add(new String[]{product.getTerm(),product.getKeyword(),product.getSuggest()
        			,product.getClickUrl()
        			,product.getImageUrl(),product.getImpressionUrl(),product.getLabelRequired()});
        }
        jdbcTemplate.batchUpdate(sql,list);
    }
}
