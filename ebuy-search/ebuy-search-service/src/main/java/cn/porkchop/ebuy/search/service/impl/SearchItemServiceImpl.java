package cn.porkchop.ebuy.search.service.impl;

import cn.porkchop.ebuy.mapper.SearchItemMapper;
import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.SearchItem;
import cn.porkchop.ebuy.pojo.SearchResult;
import cn.porkchop.ebuy.search.dao.SearchItemDao;
import cn.porkchop.ebuy.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearchItemServiceImpl implements SearchItemService {
    @Autowired
    private SolrServer solrServer;
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SearchItemDao searchItemDao;

    @Value("${DEFAULT_FIELD}")
    private String DEFAULT_FILED;

    @Override
    public E3Result importSearchItems() {
        try {
            List<SearchItem> itemList = searchItemMapper.getAllItem();
            for (SearchItem searchItem : itemList) {
                SolrInputDocument document = new SolrInputDocument();
                document.addField("id", searchItem.getId());
                document.addField("item_title", searchItem.getTitle());
                document.addField("item_sell_point", searchItem.getSellPoint());
                document.addField("item_price", searchItem.getPrice());
                document.addField("item_image", searchItem.getImage());
                document.addField("item_category_name", searchItem.getCategoryName());
                solrServer.add(document);
            }
            solrServer.commit();
            return E3Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(500, "商品导入失败");
        }


    }

    @Override
    public SearchResult search(String keyWord, int page, int rows) throws SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setQuery(keyWord);
        query.set("df", DEFAULT_FILED);
        //防止页数小于1
        page = page > 0 ? page : 1;
        //设置查询个数
        query.setStart((page - 1) * rows).setRows(rows);
        //设置高亮
        query.setHighlight(true).addHighlightField("item_title").setHighlightSimplePre("<em style=\"color:red\">").setHighlightSimplePost("</em>");
        SearchResult searchResult = searchItemDao.search(query);
        searchResult.setTotalPage((int) (searchResult.getRecordCount() % rows == 0 ? searchResult.getRecordCount() / rows : searchResult.getRecordCount() + 1));
        return searchResult;
    }

    @Override
    public E3Result addDocument(long id) {
        try {
            SearchItem searchItem = searchItemMapper.getItemById(id);
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSellPoint());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategoryName());
            solrServer.add(document);
            solrServer.commit();
            return E3Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(500,"商品添加到索引库失败");
        }
    }

}
