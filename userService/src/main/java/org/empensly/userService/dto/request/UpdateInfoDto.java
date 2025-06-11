package org.empensly.userService.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.empensly.userService.utils.enums.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateInfoDto {
    @JsonProperty(value = "country")
    private String country;
    @JsonProperty(value = "phone_number")
    private Long phoneNumber;
    @JsonProperty(value = "gender")
    private Gender gender;
    @JsonProperty(value = "new_full_name")
    private String newFullName;
    @JsonProperty(value = "new_first_name")
    private String newFirstName;
}
