package cn.porkchop.ebuy.search.dao;

import cn.porkchop.ebuy.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

public interface SearchItemDao {
    SearchResult search(SolrQuery query) throws SolrServerException;
}
