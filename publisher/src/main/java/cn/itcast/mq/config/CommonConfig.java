package cn.itcast.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CommonConfig implements ApplicationContextAware {
    /**
     * Spring容器初始化后，回调这个方法
     * ApplicationContextAware： 使用场景：做框架整合
     * @param ioc
     */
    @Override
    public void setApplicationContext(ApplicationContext ioc) throws BeansException {
        // 消息到交打换机，是否到达队列，做一个回调处理
        RabbitTemplate rabbitTemplate = ioc.getBean("rabbitTemplate", RabbitTemplate.class);
        // 给设置return callback
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             *
             * @param message 消息对象
             * @param replyCode 服务端MQ响应的状态码
             * @param replyText 响应内容、失败原因
             * @param exchange 交换机的名称
             * @param routingKey 发送消息时指定的routingKey
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("消息发送失败，应答码{}，原因{}，交换机{}，路由键{},消息{}",
                    replyCode, replyText, exchange, routingKey, message.toString());
            }
        });
    }

}
