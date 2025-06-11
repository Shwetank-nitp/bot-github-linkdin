package org.expensly.authService.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.expensly.authService.entity.RoleEntity;
import org.expensly.authService.repository.RoleRepository;
import org.expensly.authService.utils.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
public class AppCacheService {
    private final Map<String, RoleEntity> cache = new ConcurrentHashMap<>();

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void initCache() {
        if (roleRepository.count() == 0) {
            createInitRoles();
        }
        fillCache();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void refreshCache() {
        fillCache();
    }

    private void fillCache() {
        cache.clear();
        roleRepository.findAll().forEach(roleEntity -> {
            cache.put(roleEntity.getRole().name(), roleEntity);
        });
        log.info("Role cache refreshed with {} entries", cache.size());
    }

    private void createInitRoles() {
        roleRepository.save(RoleEntity.builder().role(Role.USER).build());
        roleRepository.save(RoleEntity.builder().role(Role.ADMIN).build());
    }

    public RoleEntity getRole(String role) {
        return cache.get(role);
    }
}
