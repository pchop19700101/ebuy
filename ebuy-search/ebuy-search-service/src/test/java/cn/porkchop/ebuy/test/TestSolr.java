package cn.porkchop.ebuy.test;

import cn.porkchop.ebuy.mapper.SearchItemMapper;
import cn.porkchop.ebuy.pojo.SearchItem;
import cn.porkchop.ebuy.search.dao.SearchItemDao;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestSolr {
    @Test
    public void testGetSolrServer(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        SearchItemDao searchItemDao= classPathXmlApplicationContext.getBean(SearchItemDao.class);
        System.out.println(searchItemDao);

    }
    /**
     * 打印从数据库中获取的所有的商品
     * @date 2018/1/13 11:28
     * @author porkchop
     */
    @Test
    public void testGetAllItems(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        SearchItemMapper bean = classPathXmlApplicationContext.getBean(SearchItemMapper.class);
        List<SearchItem> itemlist = bean.getAllItem();
        for (SearchItem item:itemlist){
            System.out.println(item);
        }
    }
    /**
     * 添加或者修改文档
     *
     * @date 2018/1/11 19:47
     * @author porkchop
     */
    @Test
    public void addOrUpdateDocument() throws IOException, SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer("http://119.28.142.56/solr/collection1");
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "doc01");
        document.addField("item_title", "测试商品01");
        document.addField("item_price", "1000");
        solrServer.add(document);
        solrServer.commit();
    }

    /**
     * 按照查询的删除
     *
     * @date 2018/1/11 19:55
     * @author porkchop
     */
    @Test
    public void deleteDocumentByQuery() throws IOException, SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer("http://119.28.142.56/solr/collection1");
        solrServer.deleteByQuery("id:doc01");
        solrServer.commit();
    }

    /**
     * 按照id删除
     *
     * @date 2018/1/11 20:00
     * @author porkchop
     */
    @Test
    public void deleteDocumentById() throws IOException, SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer("http://119.28.142.56/solr/collection1");
        solrServer.deleteById("doc01");
        solrServer.commit();
    }

    /**
     * 简单查询
     *
     * @date 2018/1/11 20:14
     * @author porkchop
     */
    @Test
    public void querySimpleDocument() throws SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer("http://119.28.142.56/solr/collection1");
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        //query.set("q","*:*");
        //获得查询结果
        QueryResponse queryResponse = solrServer.query(solrQuery);
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        System.out.println("查询结果的总记录数" + solrDocumentList.getNumFound());
        for (SolrDocument solrDocument : solrDocumentList) {
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
            System.out.println(solrDocument.get("item_price"));
        }
    }

    /**
     * 带有高亮的复杂查询
     *
     * @date 2018/1/11 20:39
     * @author porkchop
     */
    @Test
    public void queryComplexDocument() throws SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer("http://119.28.142.56/solr/collection1");
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("测试");
        solrQuery.set("df", "item_keywords");
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        //设置前缀后缀
        solrQuery.setHighlightSimplePre("<em>");
        solrQuery.setHighlightSimplePost("<em>");
        //获得查询结果
        QueryResponse queryResponse = solrServer.query(solrQuery);
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        System.out.println("查询结果的总记录数" + solrDocumentList.getNumFound());
        //获得高亮结果
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        for (SolrDocument solrDocument : solrDocumentList) {
            System.out.println(solrDocument.get("id"));
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String itemTitle = "";
            //判断这个id的数据有没有高亮信息
            if (list != null && list.size() > 0) {
                //获得第一条高亮
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) solrDocument.get("item_title");
            }
            System.out.println(itemTitle);
            System.out.println(solrDocument.get("item_price"));

        }
    }
}
