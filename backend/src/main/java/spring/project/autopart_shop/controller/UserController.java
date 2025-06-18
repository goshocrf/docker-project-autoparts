package spring.project.autopart_shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.project.autopart_shop.request.LoginRequest;
import spring.project.autopart_shop.request.RegisterRequest;
import spring.project.autopart_shop.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request, HttpServletResponse response) {
        return userService.loginUser(request, response);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<?> checkAuth(@CookieValue(name = "access_token", required = false) String accessToken,
                                       @CookieValue(name = "refresh_token", required = false) String refreshToken,
                                       HttpServletResponse response) {
        return userService.checkAuth(accessToken, refreshToken, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        return userService.logoutUser(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@CookieValue(name = "access_token", required = false) String accessToken,
                                        @CookieValue(name = "refresh_token", required = false) String refreshToken,
                                        HttpServletResponse response) {
        return userService.deleteUser(accessToken, refreshToken, response);
    }
}
