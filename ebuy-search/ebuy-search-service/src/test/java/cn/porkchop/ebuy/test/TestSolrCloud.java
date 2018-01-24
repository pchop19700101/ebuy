package cn.porkchop.ebuy.test;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class TestSolrCloud {
    /**
     * 添加文档
     * @date 2018/1/16 21:57
     * @author porkchop
     */
    @Test
    public void testAddDocument() throws IOException, SolrServerException {
        //构造方法的参数是zookeeper的地址列表
        CloudSolrServer solrServer = new CloudSolrServer("192.168.193.128:2181,192.168.193.128:2182,192.168.193.128:2183");
        solrServer.setDefaultCollection("collection2");
        //设置文档域
        SolrInputDocument document = new SolrInputDocument();
        document.addField("item_title", "测试商品");
        document.addField("item_price", "100");
        document.addField("id", "001");
        solrServer.add(document);
        //提交
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
        CloudSolrServer solrServer = new CloudSolrServer("192.168.193.128:2181,192.168.193.128:2182,192.168.193.128:2183");
        solrServer.setDefaultCollection("collection2");
        solrServer.deleteByQuery("id:001");
        solrServer.commit();
    }
}


