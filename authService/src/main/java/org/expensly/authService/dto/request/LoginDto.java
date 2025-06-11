package org.expensly.authService.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDto {
    @NotNull
    @JsonProperty(value = "user_name")
    private String userName;
    @NotNull
    @JsonProperty(value = "password")
    private String password;
}
