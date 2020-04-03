package com.demo.essuggest.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品实体
 * @author yangjx
 */
@Document(indexName = "merchandise", type = "product")
@Mapping(mappingPath = "productIndex-search.json")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    private String id;
    
	private String keyword;
	
	private String suggest;

	private String term;
    //@Field(analyzer = "standard",searchAnalyzer = "standard")
    
    private String clickUrl;

	private String imageUrl;
	
	private String impressionUrl;
	
	private String labelRequired;

    private Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

    public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getClickUrl() {
		return clickUrl;
	}

	public void setClickUrl(String clickUrl) {
		this.clickUrl = clickUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImpressionUrl() {
		return impressionUrl;
	}

	public void setImpressionUrl(String impressionUrl) {
		this.impressionUrl = impressionUrl;
	}

	public String getLabelRequired() {
		return labelRequired;
	}

	public void setLabelRequired(String labelRequired) {
		this.labelRequired = labelRequired;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
