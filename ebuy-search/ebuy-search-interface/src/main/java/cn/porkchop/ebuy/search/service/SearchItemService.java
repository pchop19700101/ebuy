package cn.porkchop.ebuy.search.service;

import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.SearchItem;
import cn.porkchop.ebuy.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

import java.util.List;

public interface SearchItemService {
   /**
    * 导入所有的商品到solr中
    * @date 2018/1/11 22:49
    * @author porkchop
    */
   E3Result importSearchItems();
   /**
    * 根据条件到索引库中查询
    * @date 2018/1/13 12:11
    * @author porkchop
    */
   SearchResult search(String keyWord,int page,int rows) throws SolrServerException;

   /**
    * 从数据库中查出商品,并添加到文档域
    * @date 2018/1/21 21:18
    * @author porkchop
    */
   E3Result addDocument(long id);
}
