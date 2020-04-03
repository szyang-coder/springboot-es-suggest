package com.demo.essuggest.service;

import com.demo.essuggest.document.Product;
import com.demo.essuggest.document.SuggestDoc;

import java.util.List;
import java.util.Map;

/**
 * @author yangjx
 */
public interface EsSearchService<T> {
    /**
     * 搜 索
     * @param keyword
     * @param clazz
     * @return
     */
    List<T> query(String keyword, Class<T> clazz);

    /**
     * 搜索高亮显示
     * @param keyword       关键字
     * @param indexName     索引库
     * @param fieldNames    搜索的字段
     * @return
     */
    List<Map<String,Object>> queryHit(String keyword, String indexName, String ... fieldNames);

    /**
     * 删除索引库
     * @param indexName
     * @return
     */
    void deleteIndex(String indexName);
    /**
     * 保存
     */
    void save(Product product);
    void save(List<Product> products);

    /**
     * 删除
     * @param id
     */
    void delete(String id);

    /**
     * 清空索引
     */
    void deleteAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Product getById(String id);

    /**
     * 查询全部
     * @return
     */
    List<Product> getAll();
    
    /**
     * 搜索建议
     * @return
     */
    List<String> suggest(String partner, String sub, String qt);
    
    /**
     * 添加搜索建议
     * @return
     */
    void addSuggest(List<SuggestDoc> suggests);
}
