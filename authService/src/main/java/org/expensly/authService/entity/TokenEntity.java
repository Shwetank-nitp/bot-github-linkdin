package org.expensly.authService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenEntity {
    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "token must be not null")
    @Column(name = "refresh_token", unique = true)
    private String refreshToken;

    @OneToOne
    @NotNull(message = "user must be there.")
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;
}
