package spring.project.autopart_shop.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.project.autopart_shop.component.JwtUtil;
import spring.project.autopart_shop.entity.User;
import spring.project.autopart_shop.repository.UserRepository;
import java.util.Optional;

@Service
public class UtilService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    public void resetCookies(HttpServletResponse response) {
        Cookie accessCookie = new Cookie("access_token", null);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);

        Cookie refreshCookie = new Cookie("refresh_token", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    public String validateAndRefreshToken(String accessToken, String refreshToken, HttpServletResponse response) {
        if (accessToken == null || !jwtUtil.validateToken(accessToken)) {
            if (refreshToken == null || !jwtUtil.validateToken(refreshToken))
                return null;

            accessToken = refreshAccessToken(refreshToken, response);
        }
        return accessToken;
    }

    private String refreshAccessToken(String refreshToken, HttpServletResponse response) {
        String email = jwtUtil.extractEmail(refreshToken);
        Optional<User> optionalUser = userRepository.getUserByEmail(email);

        if (optionalUser.isEmpty())
            return null;

        User user = optionalUser.get();
        String newAccessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name());

        Cookie newAccessCookie = new Cookie("access_token", newAccessToken);
        newAccessCookie.setHttpOnly(true);
        newAccessCookie.setSecure(true);
        newAccessCookie.setPath("/");
        newAccessCookie.setMaxAge(15 * 60); // 15 minutes (method retrieves parameter as seconds)

        response.addCookie(newAccessCookie);

        return newAccessToken;
    }

//    public boolean saveImage(MultipartFile image) {
//        if (image == null || image.isEmpty()) {
//            System.out.println("No image to save.");
//            return false;
//        }
//
//        try {
//            Path uploadPath = Paths.get(uploadDir);
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            String filename = Paths.get(image.getOriginalFilename()).getFileName().toString();
//            Path filePath = uploadPath.resolve(filename);
//
//            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//            return true;
//
//        } catch (IOException e) {
//            return false;
//        }
//    }
}
