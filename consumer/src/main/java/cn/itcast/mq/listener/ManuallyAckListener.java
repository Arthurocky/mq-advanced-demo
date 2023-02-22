package cn.itcast.mq.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;

//@Component
@Slf4j
public class ManuallyAckListener {

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String msg, Channel channel, Message message) throws IOException
    {
        try {
            System.out.println("消费者接收到simple.queue的消息：【" + msg + "】");
            //int a = 1/0;
            //ack DeliveryTag作用 代表第几个消息。multiple：是否是多个，批量。一个个确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("消费消息失败了!", e);
            //nack
            // requeue: 是否重入队列。 防止 消费者挂了 消息丢失，所以要重入到队列中
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}
