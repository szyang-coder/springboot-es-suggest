package com.demo.essuggest.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.io.Serializable;

/**
 * 产品实体
 * @author yangjx
 */
@Document(indexName = "suggest", type = "suggest")
@Mapping(mappingPath = "productIndex-suggest.json")
public class SuggestDoc implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    private String id;
	
	private String suggest;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
    public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
}
