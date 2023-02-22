package cn.itcast.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;

//@Configuration
@Slf4j
public class TTLConfig {
    /**
     * 声明死信交换机、队列、与消费者
     */
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue("dl.queue"),
        exchange = @Exchange(name = "dl.direct", type = ExchangeTypes.DIRECT),
        key = "dl"
    ))
    public void consumeMsg(String msg){
        log.info("运维人员收到 死信: " + msg);
    }

    @Bean
    public DirectExchange ttlExchange(){
        return new DirectExchange("ttl.direct");
    }

    /**
     * 队列超时 消息转给死信
     * @return
     */
    @Bean
    public org.springframework.amqp.core.Queue ttlQueue(){
        return QueueBuilder.durable("ttl.queue")
            // 存入到这个队列的消息最大时长 单位为毫秒
            .ttl(10000)
            .deadLetterExchange("dl.direct")
            .deadLetterRoutingKey("dl")
            .build();
    }

    @Bean
    public Binding ttlBinding(DirectExchange ttlExchange, org.springframework.amqp.core.Queue ttlQueue){
        return BindingBuilder.bind(ttlQueue).to(ttlExchange).with("ttl");
    }
}
