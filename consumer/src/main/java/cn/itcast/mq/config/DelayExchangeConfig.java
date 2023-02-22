package cn.itcast.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @version 1.0
 * @description 说明
 * @package cn.itcast.mq.config
 */
//@Configuration
@Slf4j
public class DelayExchangeConfig {

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue("delay.queue"),
        exchange = @Exchange(name = "delay.direct", delayed = "true"),
        key = "delay"
    ))
    public void consumeMsg(String msg){
        log.info("消费者收到消息："  + msg);
    }

}
