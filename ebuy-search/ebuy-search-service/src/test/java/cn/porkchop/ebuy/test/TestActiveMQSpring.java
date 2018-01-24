package cn.porkchop.ebuy.test;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 测试activemq接收消息
 */
public class TestActiveMQSpring {
    @Test
    public void testQueueConsumer() throws IOException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
//        System.in.read();
    }
}
