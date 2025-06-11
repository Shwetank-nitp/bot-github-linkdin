package org.expensly.authService.service;

import lombok.extern.slf4j.Slf4j;
import org.expensly.authService.model.UserInfoMessageKafka;
import org.expensly.authService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {
    @Autowired
    private KafkaTemplate<String, UserInfoMessageKafka> kafkaTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${kafka.topic.name}")
    private String TOPIC_NAME;

    @Autowired
    UserRepository userRepository;

    @Async
    public void send(UserInfoMessageKafka data) {
        try {
            kafkaTemplate.send(TOPIC_NAME, data);
            log.info("EVENT IS PUBLISHED TO KAFKA FORM MESSAGING SERVICE");
        } catch (Exception ex) {
            log.warn("CANNOT PUBLISH THE EVENT TO SERVICES --> USERNAME: {} ", data.getUserName());
            log.error("Error :", ex);
        }
    }
}
