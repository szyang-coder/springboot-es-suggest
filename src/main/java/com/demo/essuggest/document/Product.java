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
@Mapping(mappingPath = "productIndex-suggest.json")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    private String id;
    //@Field(analyzer = "standard",searchAnalyzer = "standard")
    
    private String name;
    //@Field(analyzer = "standard",searchAnalyzer = "standard")
    
    private String partner;

	private String sub;

    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}
}
