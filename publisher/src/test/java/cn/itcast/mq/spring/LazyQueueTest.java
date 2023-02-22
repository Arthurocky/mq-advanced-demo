package cn.itcast.mq.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LazyQueueTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMsg2LazyQueue() throws InterruptedException {
        Thread.sleep(10000);
        for (int i = 0; i < 10000000; i++) {
            rabbitTemplate.convertAndSend("lazy.queue","msg" + i);
        }
    }

    @Test
    public void testSendMsg2NormalQueue() throws InterruptedException {
        Thread.sleep(10000);
        for (int i = 0; i < 10000000; i++) {
            rabbitTemplate.convertAndSend("normal.queue","msg" + i);
        }
    }

}
