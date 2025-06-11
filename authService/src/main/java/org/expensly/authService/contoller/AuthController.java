package org.expensly.authService.contoller;

import jakarta.annotation.PostConstruct;
import org.expensly.authService.dto.request.LoginDto;
import org.expensly.authService.dto.request.UserDto;
import org.expensly.authService.dto.response.SignupDto;
import org.expensly.authService.dto.response.TokenDto;
import org.expensly.authService.model.CustomUserDetails;
import org.expensly.authService.model.UserHeaders;
import org.expensly.authService.service.AuthService;
import org.expensly.authService.utils.response.Response;
import org.expensly.authService.utils.response.ResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("login")
    public ResponseEntity<Response<TokenDto>> login(@RequestBody LoginDto data) {
        TokenDto dto = authService.login(data.getUserName(), data.getPassword());
        Response<TokenDto> response = ResponseFactory.successResponse("login successful", dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("signup")
    public ResponseEntity<Response<SignupDto>> signup(@RequestBody UserDto data) {
        SignupDto dto = authService.signup(data);
        Response<SignupDto> response = ResponseFactory.successResponse("signup successful", dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("token")
    public ResponseEntity<Response<TokenDto>> token(@RequestBody TokenDto tokenDto) {
        TokenDto dto = authService.generateNewToken(tokenDto.getRefreshToken());
        Response<TokenDto> response = ResponseFactory.successResponse("new tokens generated successful", dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("isAuthenticated")
    public ResponseEntity<?> isAuthenticated() {
        UserHeaders userHeaders = authService.getHeader();
        return ResponseEntity.ok()
                .header("x-user-id",userHeaders.getUserId())
                .header("x-user-roles", userHeaders.getRoles())
                .header("x-user-username", userHeaders.getUserName())
                .build();
    }
}
