package ru.coffee.loyalty.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.coffee.loyalty.service.PointTransactionSender;

/**
 * Когда Kafka отключена, подставляем заглушку для PointTransactionSender.
 */
@Configuration
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "false", matchIfMissing = true)
public class KafkaDisabledConfig {

    @Bean
    @ConditionalOnMissingBean(PointTransactionSender.class)
    public PointTransactionSender pointTransactionSenderStub() {
        return delta -> { /* no-op */ };
    }
}
