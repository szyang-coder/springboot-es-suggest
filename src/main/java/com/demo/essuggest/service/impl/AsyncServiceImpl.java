package com.demo.essuggest.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.essuggest.document.Product;
import com.demo.essuggest.document.ProductBuilder;
import com.demo.essuggest.document.SuggestDoc;
import com.demo.essuggest.document.SuggestDocBuilder;
import com.demo.essuggest.service.AsyncService;
import com.demo.essuggest.service.EsSearchService;
import com.demo.essuggest.utils.MysqlUtil;
import com.demo.essuggest.utils.RedisUtil;

@Service
public class AsyncServiceImpl implements AsyncService {

	private static String url = "https://in.eportalmobile.com/proxy/ampfeed/suggestions?"
			+ "partner=umecps&sub1=test&results-os=8&results-ps=10&ip=67.247.118.15&qt=";
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("rawtypes")
	@Autowired
    private EsSearchService esSearchService;
	
    @Autowired
    private RedisUtil redis;
	
    @Autowired
    private MysqlUtil mysql;
    
    @SuppressWarnings("unchecked")
	@Override
    public void executeAsync(String keyword) {
    	log.info("start executeAsync: " + keyword);
        try{
        	String data = fetchData(keyword);
        	if(data!=null){
        		JSONObject job = JSON.parseObject(data);
        		StringBuffer sb = new StringBuffer();
        		List<SuggestDoc> suggests = new ArrayList<SuggestDoc>();
        		int i = 1;
        		if(job.containsKey("organic_suggestions")){
        			JSONArray ja = job.getJSONArray("organic_suggestions");
        			if(ja.size()>0){
        				for(Object obj : ja){
        					JSONObject sugg = (JSONObject)obj;
        					if(sb.length()>0)sb.append("@_@");
        					String term = sugg.getString("term");
        					sb.append(term);
        					suggests.add(SuggestDocBuilder.create()
        							.addId(System.currentTimeMillis() + Integer.toString(i++))
        							.addSuggest(term).builder());
        				}
        			}
				}

        		if(job.containsKey("paid_suggestions")){
        			JSONArray ja = job.getJSONArray("paid_suggestions");
        			if(ja.size()>0){
        				List<Product> products = new ArrayList<Product>();
        				for(Object obj : ja){
        					JSONObject sugg = (JSONObject)obj;
        					String id = sugg.getString("id");
        					String term = sugg.getString("term");
        					String clickUrl = sugg.getString("click_url");
        					String imageUrl = sugg.getString("image_url");
        					String impressionUrl = sugg.getString("impression_url");
        					String labelRequired = sugg.getString("label_required");

        					Product p = ProductBuilder.create().addId(id).addTerm(term)
        							.addClickUrl(clickUrl).addImageUrl(imageUrl)
        							.addImpressionUrl(impressionUrl).addKeyword(keyword)
        							.addLabelRequired(labelRequired).addSuggest(sb.toString())
        							.addCreateTime(new Date()).builder();
        					products.add(p);
        				}
    					
        				//放入队列中，尽快响应客户端情况
        				redis.rightPush("fetchdata:" + keyword, products);
        				
        				//写入索引文件，后续查询可从索引中直接取到
        				esSearchService.addSuggest(suggests);
        				esSearchService.save(products);

        				//持久化到Mysql数据库中
        				mysql.save(products);
        			}
        		}
        	}
        }catch(Exception e){
            e.printStackTrace();
        }
        log.info("end executeAsync");
    }
	
    public String fetchData(String keyword) {
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;

        int itry = 1;//尝试次数
        for (int i = 0; i < itry; ++i) {
            try {
                URL uri = new URL(url + keyword);
                conn = (HttpsURLConnection) uri.openConnection(Proxy.NO_PROXY);
                
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) "
                		+ "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.108 Safari/537.36");

                int code = conn.getResponseCode();
                if (code == 200) {
                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        baos.write(buf, 0, len);
                    }
                    baos.flush();

                    return baos.toString("utf-8");
                } else
                    System.out.println(code);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                }
                try {
                    if (baos != null)
                        baos.close();
                } catch (IOException e) {
                }
                conn.disconnect();
            }
        }
        return null;
    }
}
