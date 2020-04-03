package com.demo.essuggest.document;

import java.util.Date;

/**
 * 产品实体
 * @author yangjx
 * @version 0.1
 * @date 2020/04/02 15:22
 */
public class ProductBuilder {
    private static Product product;

    // create start
    public static ProductBuilder create(){
        product = new Product();
        return new ProductBuilder();
    }
    public ProductBuilder addId(String id) {
        product.setId(id);
        return this;
    }

    public ProductBuilder addKeyword(String keyword) {
        product.setKeyword(keyword);
        return this;
    }
  
    public ProductBuilder addSuggest(String suggest) {
        product.setSuggest(suggest);
        return this;
    }
    public ProductBuilder addTerm(String term) {
        product.setTerm(term);
        return this;
    }

    public ProductBuilder addClickUrl(String clickUrl) {
        product.setClickUrl(clickUrl);
        return this;
    }

    public ProductBuilder addCreateTime(Date createTime) {
        product.setCreateTime(createTime);
        return this;
    }

    public ProductBuilder addImageUrl(String imageUrl) {
        product.setImageUrl(imageUrl);
        return this;
    }
    
    public ProductBuilder addImpressionUrl(String impressionUrl) {
        product.setImpressionUrl(impressionUrl);
        return this;
    }
    
    public ProductBuilder addLabelRequired(String labelRequired) {
        product.setLabelRequired(labelRequired);
        return this;
    }

    public Product builder() {
        return product;
    }
}
