package cn.porkchop.ebuy.search.dao.impl;

import cn.porkchop.ebuy.pojo.SearchItem;
import cn.porkchop.ebuy.pojo.SearchResult;
import cn.porkchop.ebuy.search.dao.SearchItemDao;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchItemDaoImpl implements SearchItemDao {

    private SolrServer solrServer;

    public void setSolrServer(SolrServer solrServer) {
        this.solrServer = solrServer;
    }

    @Override
    public SearchResult search(SolrQuery query) throws SolrServerException {
        QueryResponse queryResponse = solrServer.query(query);
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        List<SearchItem> searchItemList = new ArrayList<>();
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        for(SolrDocument solrDocument:solrDocumentList){
            //封装到searchitem中
            SearchItem searchItem = new SearchItem();
            searchItem.setCategoryName((String) solrDocument.get("item_category_name"));
            searchItem.setId(Long.valueOf((String) solrDocument.get("id")));
            searchItem.setImage((String) solrDocument.get("item_image"));
            searchItem.setPrice((Long) solrDocument.get("item_price"));
            searchItem.setSellPoint((String) solrDocument.get("item_sell_point"));
            //取得高亮结果
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            if(list!=null&&list.size()>0){
                searchItem.setTitle(list.get(0));
            }else{
                searchItem.setTitle((String) solrDocument.get("item_title"));
            }
            searchItemList.add(searchItem);
        }
        //封装到searchresult中
        SearchResult searchResult = new SearchResult();
        searchResult.setRecordCount(solrDocumentList.getNumFound());
        searchResult.setSearchItemList(searchItemList);
        return searchResult;
    }
}
