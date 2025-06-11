package org.expensly.authService.repository;

import org.expensly.authService.entity.TokenEntity;
import org.expensly.authService.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByUserEntity(UserEntity userEntity);
}
