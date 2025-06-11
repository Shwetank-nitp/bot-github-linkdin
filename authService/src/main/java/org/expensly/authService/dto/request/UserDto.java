package org.expensly.authService.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.expensly.authService.utils.enums.Gender;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDto {
    @NotNull
    @JsonProperty(value = "user_name")
    private String userName;

    @NotNull
    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "email")
    private String email;

    @NotNull
    @JsonProperty(value = "full_name")
    private String fullName;

    @NotNull
    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "phone_number")
    private Long phoneNumber;

    @JsonProperty(value = "country")
    private String country;

    @JsonProperty(value = "gender")
    private Gender gender;
}
