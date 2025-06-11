package org.expensly.authService.model;

import org.expensly.authService.entity.RoleEntity;
import org.expensly.authService.entity.UserEntity;
import org.expensly.authService.utils.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class CustomUserDetails extends UserEntity implements UserDetails {
    Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

    public void setAuthorities(Set<RoleEntity> roles) {
        authorities = roles.stream()
                .map(RoleEntity::getRole)
                .map(Role::name)
                .map(str -> (GrantedAuthority) () -> "ROLE_" + str)
                .toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return super.getUserName();
    }
}
