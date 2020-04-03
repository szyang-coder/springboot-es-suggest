package com.demo.essuggest.repository;

import com.demo.essuggest.document.SuggestDoc;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @author yangjx
 */
@Component
public interface SuggestDocRepository extends ElasticsearchRepository<SuggestDoc,String> {
}
