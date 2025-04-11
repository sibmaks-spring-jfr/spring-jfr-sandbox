package io.github.sibmaks.springjfr.sandbox.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Component
public class KafkaConsumer {

    @KafkaListener(id = "jfr-sandbox", topics = "demo")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println(record);
    }

}
