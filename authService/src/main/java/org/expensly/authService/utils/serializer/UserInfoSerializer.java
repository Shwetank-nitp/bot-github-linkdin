package org.expensly.authService.utils.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.expensly.authService.model.UserInfoMessageKafka;

@Slf4j
public class UserInfoSerializer implements Serializer<UserInfoMessageKafka> {
    @Override
    public byte[] serialize(String s, UserInfoMessageKafka user) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsBytes(user);
        } catch (Exception e) {
            log.error("Exception at serialization in UserInfoSerializer :", e);
            throw new RuntimeException(e);
        }
    }
}
