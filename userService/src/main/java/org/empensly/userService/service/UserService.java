package org.empensly.userService.service;

import org.empensly.userService.dto.request.UpdateInfoDto;
import org.empensly.userService.enitie.UserEntity;
import org.empensly.userService.repository.UserRepository;
import org.empensly.userService.utils.errors.ExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                ExceptionFactory.notFoundException("user not found!")
        );
    }

    public UserEntity updateInfo(UpdateInfoDto data, Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                ExceptionFactory.notFoundException("user not found")
        );
        if (!(data.getCountry() == null)) {
            user.setCountry(data.getCountry());
        }
        if (!(data.getGender() == null)) {
            user.setGender(data.getGender());
        }
        if (!(data.getPhoneNumber() == null)) {
            user.setPhoneNumber(data.getPhoneNumber());
        }
        if (!(data.getNewFirstName() == null)) {
            user.setFirstName(data.getNewFirstName());
        }
        if (!(data.getNewFullName() == null)) {
            user.setFullName(data.getNewFullName());
        }
        return userRepository.save(user);
    }
}
