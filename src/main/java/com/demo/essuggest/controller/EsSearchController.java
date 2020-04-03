package com.demo.essuggest.controller;

import com.demo.essuggest.document.Product;
import com.demo.essuggest.service.EsSearchService;

import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * elasticsearch 搜索
 * @author yangjx
 */
@RestController
public class EsSearchController {

    @SuppressWarnings("rawtypes")
	@Autowired
    private EsSearchService esSearchService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 新增 / 修改索引
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping("save")
    public String add(@RequestBody Product product) {
    	List<Product> products = new ArrayList<Product>();
    	products.add(product);
        esSearchService.save(products);
        return "success";
    }

    /**
     * 删除索引
     * @return
     */
    @RequestMapping("delete/{id}")
    public String delete(@PathVariable String id) {
        esSearchService.delete(id);
        return "success";
    }
    /**
     * 清空索引
     * @return
     */
    @RequestMapping("delete_all")
    public String deleteAll() {
        esSearchService.deleteAll();
        return "success";
    }

    /**
     * 根据ID获取
     * @return
     */
    @RequestMapping("get/{id}")
    public Product getById(@PathVariable String id){
        return esSearchService.getById(id);
    }

    /**
     * 根据获取全部
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping("get_all")
    public List<Product> getAll(){
        return esSearchService.getAll();
    }

    /**
     * 搜索
     * @param qt
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping("demo/query")
    public List<Product> query(@RequestParam String qt){
        return esSearchService.query(qt,Product.class);
    }

    /**
     * 搜索，命中关键字高亮
     * http://localhost:8080/query_hit?qt=iphone&indexName=merchandise&fields=name,partner,sub
     * @param qt   关键字
     * @param indexName 索引库名称
     * @param fields    搜索字段名称，多个以“，”分割
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping("query_hit")
    public List<Map<String,Object>> queryHit(@RequestParam String keyword, @RequestParam String indexName, @RequestParam String fields){
        String[] fieldNames = {};
        if(fields.contains(",")) fieldNames = fields.split(",");
        else fieldNames[0] = fields;
        return esSearchService.queryHit(keyword,indexName,fieldNames);
    }

    /**
     * 删除索引库
     * @param indexName
     * @return
     */
    @RequestMapping("delete_index/{indexName}")
    public String deleteIndex(@PathVariable String indexName){
        esSearchService.deleteIndex(indexName);
        return "success";
    }

    /**
     * 建议搜索(分词)
     * 先将keyword词语分词 然后用分词后的数据去匹配所有前缀为当前分词的数据
     *
     * @param qt
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("demo/suggestSeg")
    public List<String> getSuggestSearchSeg(@RequestParam String partner, @RequestParam String sub, @RequestParam String qt) {
		List<String> responseSuggests = new ArrayList();
        //分词
        List<String> list = getAnalyzes("merchandise" ,qt);
        list.forEach(keyword->{
            //查询建议词
            List<String> suggestKeyword = esSearchService.suggest(partner, sub, keyword);
            responseSuggests.addAll(suggestKeyword);
        });
        //去重
        List<String> distinctSearchTermList = responseSuggests.stream().distinct().collect(Collectors.toList());
        return distinctSearchTermList;
    }

    /**
     * 建议搜索
     * 根据前缀关联搜索所有匹配前缀的词语
     *
     * @param qt
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping("demo/suggest")
    public List<String> getSuggestSearch(@RequestParam String partner, @RequestParam String sub, @RequestParam String qt) {
        List<String> suggestKeyword = esSearchService.suggest(partner, sub, qt);
        return suggestKeyword;
    }

    /**
     * 将文本分词后的数据 默认使用es内置标准分词
     *
     * @param index 索引index
     * @param text 需要被分析的词语
     * @return
     */
    public List<String> getAnalyzes(String index,String text){
        //调用ES客户端分词器进行分词
        AnalyzeRequestBuilder ikRequest = new AnalyzeRequestBuilder(elasticsearchTemplate.getClient(),
                AnalyzeAction.INSTANCE,index,text).setAnalyzer("standard");
        List<AnalyzeResponse.AnalyzeToken> ikTokenList = ikRequest.execute().actionGet().getTokens();

        // 赋值
        List<String> searchTermList = new ArrayList<>();
        if(ikTokenList!=null){
	        ikTokenList.forEach(ikToken -> {
	            searchTermList.add(ikToken.getTerm());
	        });
        }

        return searchTermList;
    }
}
