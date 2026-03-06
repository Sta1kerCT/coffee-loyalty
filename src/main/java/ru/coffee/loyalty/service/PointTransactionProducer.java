package ru.coffee.loyalty.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Отправка целочисленных значений (изменение баллов) в Kafka для аналитики в ClickHouse.
 * Формат сообщения: JSON {"value": N} для совместимости с ClickHouse Kafka engine.
 */
@Service
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class PointTransactionProducer implements PointTransactionSender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic:point-transactions}")
    private String topic;

    @Value("${app.kafka.dlq-topic:point-transactions-dlq}")
    private String dlqTopic;

    @Value("${app.kafka.enabled:true}")
    private boolean enabled;

    public void sendPointsDelta(Integer delta) {
        if (!enabled || delta == null || delta == 0) return;
        String key = "pts-" + System.currentTimeMillis();
        String payload;
        try {
            payload = objectMapper.writeValueAsString(Map.of("value", delta));
        } catch (JsonProcessingException e) {
            log.warn("JSON serialize failed", e);
            payload = "{\"value\":" + delta + "}";
        }
        String finalPayload = payload;
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, payload);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Kafka send failed for topic {}, sending to DLQ {}", topic, dlqTopic, ex);
                kafkaTemplate.send(dlqTopic, key, finalPayload);
            } else {
                log.debug("Sent points delta {} to {}", delta, topic);
            }
        });
    }
}
