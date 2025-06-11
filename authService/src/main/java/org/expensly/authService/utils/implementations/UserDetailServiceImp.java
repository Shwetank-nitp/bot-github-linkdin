package org.expensly.authService.utils.implementations;

import org.expensly.authService.entity.UserEntity;
import org.expensly.authService.model.CustomUserDetails;
import org.expensly.authService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImp implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserName(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("user not found")
                );

        CustomUserDetails userDetails = new CustomUserDetails();

        userDetails.setId(userEntity.getId());
        userDetails.setUserName(userEntity.getUserName());
        userDetails.setPassword(userEntity.getPassword());
        userDetails.setRoleEntities(userEntity.getRoleEntities());
        userDetails.setEmail(userEntity.getEmail());

        userDetails.setAuthorities(userDetails.getRoleEntities());

        return userDetails;
    }
}
