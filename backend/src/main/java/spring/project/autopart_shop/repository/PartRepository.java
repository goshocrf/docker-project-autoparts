package spring.project.autopart_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.project.autopart_shop.entity.Part;
import spring.project.autopart_shop.entity.PartCategory;

import java.util.List;

public interface PartRepository extends JpaRepository<Part, Long> {
    List<Part> findAllByCategory(PartCategory category);
}
