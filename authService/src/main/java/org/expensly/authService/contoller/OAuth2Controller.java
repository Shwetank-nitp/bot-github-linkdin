package org.expensly.authService.contoller;

import org.expensly.authService.dto.response.TokenDto;
import org.expensly.authService.service.OAuthService;
import org.expensly.authService.utils.response.Response;
import org.expensly.authService.utils.response.ResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("oauth")
public class OAuth2Controller {
    @Autowired
    private OAuthService oAuthService;

    @PostMapping("octocat/callback")
    public ResponseEntity<Response<TokenDto>> signInWithGithub(@RequestParam("code") String code) {
        var res = oAuthService.oauth2GitHubAuth(code);
        return new ResponseEntity<>(
                ResponseFactory.successResponse(
                        "Oauth completed successfully",
                        res
                ),
                HttpStatus.OK
        );
    }
}
