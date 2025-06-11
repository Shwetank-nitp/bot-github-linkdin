package org.empensly.userService.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.empensly.userService.enitie.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;
    @Value("${kafka.retry.backoff.ms}")
    private Integer retryBackoffMs;
    @Value("${kafka.retry.backoff.max}")
    private Integer retryBackoffMaxMs;

    private Map<String, Object> commonProducerConfigs() {
        Map<String, Object> configs = new HashMap<>();

        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, retryBackoffMs);
        configs.put(ProducerConfig.RETRY_BACKOFF_MAX_MS_CONFIG, retryBackoffMaxMs);

        // retry infinite initially
        configs.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, Integer.MAX_VALUE);
        configs.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, Integer.MAX_VALUE);
        configs.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, Integer.MAX_VALUE);

        return configs;
    }

    @Bean
    public ProducerFactory<String, UserEntity> dltFactory() {
            Map<String, Object> configs = commonProducerConfigs();
            configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

            return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<String, UserEntity> dltKafkaTemplate() {
        return new KafkaTemplate<>(dltFactory());
    }
}