package spring.project.autopart_shop.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.project.autopart_shop.repository.UserRepository;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import static io.jsonwebtoken.Jwts.builder;

@Component
public class JwtUtil {

    @Value("${secret.key}")
    private String SECRET_KEY;

    private static final long ACCESS_TOKEN_EXPIRATION = 900000 ; // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 1209600000; // 14 days

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateAccessToken(String email, String role) {
        return generateToken(email, role, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(String email, String role) {
        return generateToken(email, role, REFRESH_TOKEN_EXPIRATION);
    }

    public String generateToken(String email, String role, long expiration) {
        return builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}
