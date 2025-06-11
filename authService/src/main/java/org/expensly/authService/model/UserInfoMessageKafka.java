package org.expensly.authService.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.expensly.authService.dto.request.UserDto;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class UserInfoMessageKafka extends UserDto {
    @JsonProperty(value = "user_id")
    private Long userId;

    public static UserInfoMessageKafka castInto(UserDto userDto, Long id) {
        UserInfoMessageKafka userInfoMessageKafka = new UserInfoMessageKafka(id);
        userInfoMessageKafka.setFullName(userDto.getFullName());
        userInfoMessageKafka.setFirstName(userDto.getFirstName());
        userInfoMessageKafka.setPhoneNumber(userDto.getPhoneNumber());
        userInfoMessageKafka.setGender(userDto.getGender());
        userInfoMessageKafka.setCountry(userDto.getCountry());
        return userInfoMessageKafka;
    }
}
