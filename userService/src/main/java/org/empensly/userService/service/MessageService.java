package org.empensly.userService.service;

import lombok.extern.slf4j.Slf4j;
import org.empensly.userService.enitie.UserEntity;
import org.empensly.userService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {
    @Autowired
    private UserRepository userRepository;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "auth_user_consumer",
    containerFactory = "concurrentKafkaListenerContainerFactory")
    public void userRegisterListener(UserEntity data) {
        try {
            userRepository.save(data);
        } catch (DataIntegrityViolationException ex) {
            log.error("cannot create new user with userId : {} because of data integrity exception {}", data.getUserId(), ex.getMessage(), ex);
        } catch (Exception ex) {
            log.warn("failed to save user exception :", ex);
        }
    }
}
