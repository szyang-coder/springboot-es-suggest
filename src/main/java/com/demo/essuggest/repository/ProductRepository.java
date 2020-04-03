package com.demo.essuggest.repository;

import com.demo.essuggest.document.Product;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @author yangjx
 */
@Component
public interface ProductRepository extends ElasticsearchRepository<Product,String> {
}
