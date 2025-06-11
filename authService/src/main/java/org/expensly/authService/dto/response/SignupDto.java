package org.expensly.authService.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.expensly.authService.utils.enums.Role;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SignupDto {
    @JsonProperty(value = "user_name")
    private String userName;
    @JsonProperty(value = "roles")
    private List<Role> roles;
}
