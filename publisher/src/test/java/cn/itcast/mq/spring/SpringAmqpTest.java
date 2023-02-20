package cn.itcast.mq.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessagePublishConfirm() throws InterruptedException
    {
        String routingKey = "simple";
        String message = "hello, spring amqp!";
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        correlationData.getFuture().addCallback(new SuccessCallback<CorrelationData.Confirm>() {
            @Override
            public void onSuccess(CorrelationData.Confirm confirm)
            {
                if (confirm.isAck()) {
                    // 返回ack
                    //log.info("消息发送成功!");
                    System.out.println("消息发送成功!");
                } else {
                    // 返回nack
                    log.error("消息发送失败! {}", confirm.getReason());
                    System.out.println("消息发送失败! " + confirm.getReason());
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable throwable)
            {
                // 异常。网络超时...
                log.error("消息发送异常了", throwable);
                // 发短信通知
                // 重试
            }
        });
        //设置交换机不存在
        rabbitTemplate.convertAndSend("camq.direct", routingKey, message, correlationData);

        try {
            Thread.sleep(10000);
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //设置队列不存在
    @Test
    public void testSendMessagePublishReturn() throws InterruptedException {
        String queueName = "abc";
        // 如果没有指定交换机，则使用默认交换机, amq.direct
        // amq.direct默认交换机的特点：队列名就是bindingKey
        rabbitTemplate.convertAndSend(queueName, "hello");
        Thread.sleep(10000);
    }
}