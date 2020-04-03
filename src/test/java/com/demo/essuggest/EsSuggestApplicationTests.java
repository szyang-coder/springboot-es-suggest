package com.demo.essuggest;

import com.alibaba.fastjson.JSON;
import com.demo.essuggest.document.Product;
import com.demo.essuggest.service.EsSearchService;
import com.demo.essuggest.utils.MysqlUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsSuggestApplicationTests {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MysqlUtil mysql;
	@SuppressWarnings("rawtypes")
	@Autowired
    private EsSearchService esSearchService;

    @SuppressWarnings("unchecked")
	@Test
    public void save() {
        log.info("【创建索引前的数据条数】：{}",esSearchService.getAll().size());

        List<Product> products = mysql.getList();
        esSearchService.save(products);

        log.info("【创建索引后的数据条数】：{}",esSearchService.getAll().size());
    }

    @SuppressWarnings("unchecked")
	@Test
    public void getAll(){
        esSearchService.getAll().parallelStream()
                .map(JSON::toJSONString)
                .forEach(System.out::println);
    }

    @Test
    public void deleteAll() {
        esSearchService.deleteAll();
    }

    @Test
    public void getById() {
    	String json = JSON.toJSONString(esSearchService.getById("158582176595603"));
        log.info("【根据ID查询内容】：{}", json);
    }

    @SuppressWarnings("unchecked")
	@Test
    public void query() {
        log.info("【根据关键字搜索内容】：{}", JSON.toJSONString(esSearchService.query("iphone",Product.class)));
    }

    @SuppressWarnings("unchecked")
	@Test
    public void queryHit() {

        String keyword = "iphone";
        String indexName = "merchandise";

        List<Map<String,Object>> searchHits = esSearchService.queryHit(keyword,indexName,"name","partner","sub");
        log.info("【根据关键字搜索内容，命中部分高亮，返回内容】：{}", JSON.toJSONString(searchHits));
    }

    @SuppressWarnings("unchecked")
	@Test
    public void suggest() {

    	String partner = "other";//"umecps";
    	String sub = "test";
        String keyword = "iphone";

        List<String> searchHits = esSearchService.suggest(partner, sub, keyword);
        log.info("【搜索建议，根据关键字前缀匹配，返回内容】：{}", JSON.toJSONString(searchHits));
    }

    @Test
    public void deleteIndex() {
        log.info("【删除索引库】");
        esSearchService.deleteIndex("merchandise");
        esSearchService.deleteIndex("suggest");
    }
}