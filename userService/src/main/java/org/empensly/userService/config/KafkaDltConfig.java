package org.empensly.userService.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Header;
import org.empensly.userService.enitie.UserEntity;
import org.empensly.userService.utils.interfaces.DltStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.ExponentialBackOff;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class KafkaDltConfig implements DltStrategy<ExponentialBackOff> {
    private static final Long INIT_INTERVAL = 100L;
    private static final Double MULTIPLIER = 2.0;
    private static final Integer MAX_ATTEMPTS = 4;

    @Autowired
    private KafkaTemplate<String, UserEntity> kafkaTemplate;


    @Override
    public DeadLetterPublishingRecoverer recoverer() {
        return new DeadLetterPublishingRecoverer(kafkaTemplate);
    }

    @Override
    public ExponentialBackOff defineBackoff() {
        ExponentialBackOff backOff = new ExponentialBackOff(INIT_INTERVAL, MULTIPLIER);
        backOff.setMaxAttempts(MAX_ATTEMPTS);
        return backOff;
    }

    @Bean
    @Override
    public DefaultErrorHandler errorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(
                recoverer(),
                defineBackoff()
        );

        handler.addNotRetryableExceptions(
                NullPointerException.class,
                DataIntegrityViolationException.class,
                DeserializationException.class
        );

        handler.setRetryListeners((record, ex, attempts) -> {
            log.error(
                    "Failed to process the message from Kafka | Topic: {} | Partition: {} | Offset: {} | Attempts: {}",
                    record.topic(), record.partition(), record.offset(), attempts
            );
        });

        return handler;
    }
}
