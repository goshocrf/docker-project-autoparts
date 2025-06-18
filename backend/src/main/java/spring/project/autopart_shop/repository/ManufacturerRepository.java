package spring.project.autopart_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.project.autopart_shop.entity.Manufacturer;

import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    Optional<Manufacturer> findByName(String name);
}
