package cn.itcast.mq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;

/**
 * @version 1.0
 * @description 说明
 * @package cn.itcast.mq.config
 */
//@Configuration
@Slf4j
public class DeadLetterConfig {
    @Bean
    public DirectExchange dlExchange(){
        return new DirectExchange("dl.direct");
    }

    @Bean
    public Queue dlQueue(){
        return new Queue("dl.queue");
    }

    @Bean
    public Binding dlBinding(DirectExchange dlExchange,Queue dlQueue){
        return BindingBuilder.bind(dlQueue).to(dlExchange).with("dl");
    }

    @Bean
    public Queue simpleQueue(){
        return QueueBuilder.durable("simple.queue")
            // 指定死信交换机
            .deadLetterExchange("dl.direct")
            // 绑定的key
            .deadLetterRoutingKey("dl")
            .build();
    }

    /**
     * 普通消费者，它出错了，导致重试耗尽，reject或nack且non requeue。成了信息
     * @param msg
     */
    @RabbitListener(queues = "simple.queue")
    public void consumeMsg(String msg){
        log.info("消费者：{}", msg);
        int i = 1/0;
    }


}
