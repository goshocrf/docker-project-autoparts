package spring.project.autopart_shop.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.project.autopart_shop.component.JwtUtil;
import spring.project.autopart_shop.entity.User;
import spring.project.autopart_shop.repository.UserRepository;
import spring.project.autopart_shop.request.LoginRequest;
import spring.project.autopart_shop.request.RegisterRequest;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UtilService utilService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseEntity<?> registerUser(RegisterRequest request) {
        if (userRepository.getUserByEmail(request.getEmail()).isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this email already exists");

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    public ResponseEntity<?> loginUser(LoginRequest request, HttpServletResponse response) {
        String email = request.getEmail();
        Optional<User> optionalUser = userRepository.getUserByEmail(email);
        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with this email does not exist");

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");

        String role = user.getRole().name();
        String accessToken = jwtUtil.generateAccessToken(email, role);
        String refreshToken = jwtUtil.generateRefreshToken(email, role);

        Cookie accessCookie = new Cookie("access_token", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(15 * 60); // 15 minutes

        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(14 * 24 * 60 * 60);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok("User logged in successfully");
    }

    public ResponseEntity<?> checkAuth(String accessToken, String refreshToken, HttpServletResponse response) {
        accessToken = utilService.validateAndRefreshToken(accessToken, refreshToken, response);
        if (accessToken == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in");

        String email = jwtUtil.extractEmail(accessToken);
        Optional<User> optionalUser = userRepository.getUserByEmail(email);
        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in or create an account.");

        return ResponseEntity.ok(optionalUser.get());
    }

    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        utilService.resetCookies(response);
        return ResponseEntity.ok("Logged out");
    }

    public ResponseEntity<?> deleteUser(String accessToken, String refreshToken, HttpServletResponse response) {
        accessToken = utilService.validateAndRefreshToken(accessToken, refreshToken, response);
        if (accessToken == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in");

        String email = jwtUtil.extractEmail(accessToken);
        Optional<User> optionalUser = userRepository.getUserByEmail(email);
        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in or create an account.");

        userRepository.delete(optionalUser.get());
        utilService.resetCookies(response);
        return ResponseEntity.ok("User deleted successfully");
    }
}
