package org.empensly.userService.enitie;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.empensly.userService.utils.enums.Gender;


@Entity
@Data
@Table(name = "user_details")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @Column(name = "user_id")
    @JsonProperty(value = "user_id")
    public Long userId;

    @Column(name = "first_name", nullable = false)
    @JsonProperty(value = "first_name")
    public String firstName;

    @Column(name = "full_name", nullable = false)
    @JsonProperty(value = "full_name")
    public String fullName;

    @Column(name = "country")
    @JsonProperty(value = "country")
    public String country;

    @Column(name = "phone_number")
    @JsonProperty(value = "phone_number")
    public Long phoneNumber;

    @Column(name = "gender")
    @JsonProperty(value = "gender")
    public Gender gender;
}
