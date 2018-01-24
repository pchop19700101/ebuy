package cn.porkchop.ebuy.search.message;

import cn.porkchop.ebuy.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class DocumentAddListener implements MessageListener{
    @Autowired
    private SearchItemService searchItemService;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            //等待一段时间,让发送消息的service层提交事务,防止从数据库中查询不到当前id的商品
            Thread.sleep(1000);
            System.out.println(textMessage.getText());
            searchItemService.addDocument(Long.parseLong(textMessage.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
