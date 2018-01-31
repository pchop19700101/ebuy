package cn.porkchop.ebuy.item.message;

import cn.porkchop.ebuy.pojo.Item;
import cn.porkchop.ebuy.pojo.TbItemDesc;
import cn.porkchop.ebuy.manager.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.util.HashMap;

/**
 * 监听商品增加修改的消息
 *
 * @author porkchop
 * @date 2018/1/24 17:21
 */
public class HtmlGenerateListenr implements MessageListener {
    @Value("${FREEMARKER_HTML_GENERATE_PATH}")
    private String FREEMARKER_HTML_GENERATE_PATH;
    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            long id = Long.parseLong(textMessage.getText());
            //等待商品添加修改的事务提交
            Thread.sleep(2000);
            Item item = new Item(itemService.getItemById(id));
            TbItemDesc tbItemDesc = itemService.getItemDescByItemId(id);
            HashMap<Object, Object> map = new HashMap<>();
            map.put("item", item);
            map.put("itemDesc", tbItemDesc);
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            FileWriter fileWriter = new FileWriter(FREEMARKER_HTML_GENERATE_PATH + id + ".html");
            template.process(map,fileWriter);
            fileWriter.close();;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
