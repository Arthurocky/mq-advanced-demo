package cn.itcast.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@Slf4j
@SpringBootApplication
public class PublisherApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(PublisherApplication.class);
    }

    /**
     * 消息到交换机，但是没有到达队列则会调用ReturnCallback方法
     *
     * @param rabbitTemplate
     */
    @Resource
    public void setReturnCallback(RabbitTemplate rabbitTemplate)
    {
        rabbitTemplate.setReturnCallback(
                (message, replyCode, replyText, exchange, routingKey) ->
                        log.info("消息发送失败，应答码{}，原因{}，交换机{}，路由键{},消息{}"
                                , replyCode, replyText, exchange, routingKey, message.toString()));
    }

}
