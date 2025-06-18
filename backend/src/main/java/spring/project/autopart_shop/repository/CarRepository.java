package spring.project.autopart_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.project.autopart_shop.entity.Car;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByManufacturerId(Long manufacturerId);
}
