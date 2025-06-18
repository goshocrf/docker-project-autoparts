package spring.project.autopart_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.project.autopart_shop.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByEmail(String email);
}
