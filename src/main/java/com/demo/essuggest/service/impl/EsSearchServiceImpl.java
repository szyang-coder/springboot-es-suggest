package com.demo.essuggest.service.impl;

import com.alibaba.fastjson.JSON;
import com.demo.essuggest.document.Product;
import com.demo.essuggest.repository.ProductRepository;
import com.demo.essuggest.service.EsSearchService;
import com.demo.essuggest.utils.RedisUtil;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;

/**
 * elasticsearch 搜索引擎 service实现
 * @author yangjx
 */
@Service
public class EsSearchServiceImpl<T> implements EsSearchService<T> {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private ProductRepository productRepository;

    @Autowired
    private Client client;
    
    @Autowired
    private RedisUtil redis;
    
    @Override
    public List<T> query(String keyword, Class<T> clazz) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(new QueryStringQueryBuilder(keyword))
                .withSort(SortBuilders.scoreSort().order(SortOrder.DESC))
                .build();

        return elasticsearchTemplate.queryForList(searchQuery,clazz);
    }

    /**
     * 高亮显示
     */
    @Override
    public  List<Map<String,Object>> queryHit(String keyword,String indexName,String ... fieldNames) {
        // 构造查询条件,使用标准分词器.
        QueryBuilder matchQuery = createQueryBuilder(keyword,fieldNames);

        // 设置高亮,使用默认的highlighter高亮器
        HighlightBuilder highlightBuilder = createHighlightBuilder(fieldNames);

        // 设置查询字段
        SearchResponse response = elasticsearchTemplate.getClient().prepareSearch(indexName)
                .setQuery(matchQuery)
                .highlighter(highlightBuilder)
                .setSize(10000) // 设置一次返回的文档数量，最大值：10000
                .get();

        // 返回搜索结果
        SearchHits hits = response.getHits();

        return getHitList(hits);
    }

    /**
     * 构造查询条件
     */
    private QueryBuilder createQueryBuilder(String keyword, String... fieldNames){
        // 构造查询条件,使用标准分词器.
        return QueryBuilders.multiMatchQuery(keyword,fieldNames)   // matchQuery(),单字段搜索
                //.analyzer("standard")
                .operator(Operator.OR);
    }
    /**
     * 构造高亮器
     */
    private HighlightBuilder createHighlightBuilder(String... fieldNames){
        // 设置高亮,使用默认的highlighter高亮器
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                // .field("productName")
                .preTags("<span style='color:red'>")
                .postTags("</span>");

        // 设置高亮字段
        for (String fieldName: fieldNames) highlightBuilder.field(fieldName);
        return highlightBuilder;
    }

    /**
     * 处理高亮结果
     */
    private List<Map<String,Object>> getHitList(SearchHits hits){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map;
        for(SearchHit searchHit : hits){
            map = new HashMap<>();
            // 处理源数据
            map.put("source",searchHit.getSourceAsMap());
            // 处理高亮数据
            Map<String,Object> hitMap = new HashMap<>();
            searchHit.getHighlightFields().forEach((k,v) -> {
                String hight = "";
                for(Text text : v.getFragments()) hight += text.string();
                hitMap.put(v.getName(),hight);
            });
            map.put("highlight",hitMap);
            list.add(map);
        }
        return list;
    }

    @Override
    public void deleteIndex(String indexName) {
        elasticsearchTemplate.deleteIndex(indexName);
    }
    
    @Override
    public void save(List<Product> products) {
        elasticsearchTemplate.putMapping(Product.class);
        if(products.size() > 0){
            /*Arrays.asList(products).parallelStream()
                    .map(productRepository::save)
                    .forEach(product -> log.info("【保存数据】：{}", JSON.toJSONString(product)));*/
            log.info("【保存索引】：{}",JSON.toJSONString(productRepository.saveAll(products)));
        }
    }

    @Override
    public void delete(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        productRepository.deleteAll();
    }

    @Override
    public Product getById(String id) {
    	Optional<Product> op = productRepository.findById(id);
        return op.get();
    }

    @Override
    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        productRepository.findAll().forEach(list::add);
        return list;
    }

    /**
     * 根据前缀关联搜索所有匹配前缀的词语
     *
     * @param keyword
     * @return
     */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<String> suggest(String partner, String sub, String qt){
    	Object cache = redis.get(qt);
    	if(cache!=null){
			return (List<String>)cache;
    	}
        //field的名字,前缀(输入的text),以及大小size
        CompletionSuggestionBuilder suggestionBuilderDistrict = SuggestBuilders.completionSuggestion("name.suggest")
                .prefix(qt).size(100);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("my-suggest", suggestionBuilderDistrict);//添加suggest my-suggest自定义的随便取的
        
        //构造查询条件，完全匹配partner、sub字段
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("partner", partner))
                .must(QueryBuilders.termQuery("sub", sub));
        
        //设置查询builder的index,type,以及建议
        SearchRequestBuilder requestBuilder = client.prepareSearch("merchandise").setTypes("product")
        	.setQuery(queryBuilder)
        	.suggest(suggestBuilder);
        System.out.println(requestBuilder.toString());

        SearchResponse response = requestBuilder.get();
        Suggest suggest = response.getSuggest();//suggest实体

        Set<String> suggestSet = new HashSet<>();//set
        int maxSuggest = 0;
        boolean match = false;
        if (suggest!=null){
			Suggest.Suggestion result = suggest.getSuggestion("my-suggest");//获取suggest,name任意string
            for (Object term : result.getEntries()) {
                if (term instanceof CompletionSuggestion.Entry){
                    CompletionSuggestion.Entry item = (CompletionSuggestion.Entry) term;
                    if (!item.getOptions().isEmpty()){
                        //若item的option不为空,循环遍历
                        for (CompletionSuggestion.Entry.Option option : item.getOptions()) {
                            String tip = option.getText().toString();
                            if (tip.equals(qt)){
                            	match = true;
                            }
                            else{
                            	 if(!suggestSet.contains(tip)){
                                     suggestSet.add(tip);
                                     ++maxSuggest;
                            	 }
                            }
                        }
                    }
                }
                if (maxSuggest>=10){
                    break;
                }
            }
        }
        List<String> suggests = Arrays.asList(suggestSet.toArray(new String[]{}));
        
        //其实没必要，只是为了展现redis缓存功能
        if(match && maxSuggest>0){
        	//搜索建议按关键词缓存1小时
        	redis.set(qt, suggests, 3600);
        }
        return suggests;
    }
}
