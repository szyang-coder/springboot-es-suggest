package com.demo.essuggest.document;

/**
 * 搜索建议实体
 * @author yangjx
 * @version 0.1
 * @date 2020/04/02 15:22
 */
public class SuggestDocBuilder {
    private static SuggestDoc suggest;

    // create start
    public static SuggestDocBuilder create(){
    	suggest = new SuggestDoc();
        return new SuggestDocBuilder();
    }
    public SuggestDocBuilder addId(String id) {
    	suggest.setId(id);
        return this;
    }
  
    public SuggestDocBuilder addSuggest(String msg) {
    	suggest.setSuggest(msg);
        return this;
    }
    
    public SuggestDoc builder() {
        return suggest;
    }
}
