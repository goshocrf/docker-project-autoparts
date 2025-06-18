package spring.project.autopart_shop.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring.project.autopart_shop.component.JwtFilter;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(("/**")).permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/cars/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/cars/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/cars/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/parts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/parts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/parts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/manufacturers/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/manufacturers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/manufacturers/**").hasRole("ADMIN")
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
