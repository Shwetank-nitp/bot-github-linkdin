package org.empensly.userService.controller;

import org.empensly.userService.dto.request.UpdateInfoDto;
import org.empensly.userService.enitie.UserEntity;
import org.empensly.userService.service.UserService;
import org.empensly.userService.utils.response.Response;
import org.empensly.userService.utils.response.ResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserServiceController {
    @Autowired
    private UserService userService;

    @GetMapping("info")
    public ResponseEntity<Response<UserEntity>> getUserInfo(@RequestHeader("x-user-id") Long userId) {
        UserEntity user = userService.getUser(userId);
        return ResponseEntity.ok(ResponseFactory.successResponse(
                "user data found",
                user
        ));
    }

    @PutMapping("update")
    public ResponseEntity<Response<UserEntity>> updateUserInfo(
            @RequestBody UpdateInfoDto data,
            @RequestHeader("x-user-id") Long userId
    ) {
        UserEntity updateInfo = userService.updateInfo(data, userId);
        return ResponseEntity.ok(ResponseFactory.successResponse(
                "user info update",
                updateInfo
        ));
    }
}
