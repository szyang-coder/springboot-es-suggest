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

    public ProductBuilder addName(String name) {
        product.setName(name);
        return this;
    }

    public ProductBuilder addPartner(String partner) {
        product.setPartner(partner);
        return this;
    }

    public ProductBuilder addCreateTime(Date createTime) {
        product.setCreateTime(createTime);
        return this;
    }

    public ProductBuilder addSub(String sub) {
        product.setSub(sub);
        return this;
    }

    public Product builder() {
        return product;
    }
}
