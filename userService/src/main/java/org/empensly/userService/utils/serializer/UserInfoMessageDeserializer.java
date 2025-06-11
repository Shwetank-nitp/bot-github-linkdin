package org.empensly.userService.utils.serializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.empensly.userService.enitie.UserEntity;
import org.springframework.kafka.support.serializer.DeserializationException;

import java.io.IOException;

@Slf4j
public class UserInfoMessageDeserializer implements Deserializer<UserEntity> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public UserEntity deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(data, UserEntity.class);
        } catch (IOException e) {
            log.error("Cannot deserialize the object of UserInfoMessageDeserializer");
            throw new DeserializationException("Error deserializing UserEntity", data, false, e);
        }
    }
}
