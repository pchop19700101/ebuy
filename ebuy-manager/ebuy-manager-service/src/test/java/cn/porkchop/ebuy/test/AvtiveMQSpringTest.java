package cn.porkchop.ebuy.test;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;


/**
 * 测试用JmsTemplate发送接收消息
 */
public class AvtiveMQSpringTest {
    @Test
    public void testSpringActiveMQ() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        JmsTemplate jmsTemplate= classPathXmlApplicationContext.getBean(JmsTemplate.class);
        Destination destination = (Destination) classPathXmlApplicationContext.getBean("queueDestination");
        jmsTemplate.send(destination, new MessageCreator() {


            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("spring activemq queue message");
            }
        });
    }

}
