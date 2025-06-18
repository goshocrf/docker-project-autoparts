package spring.project.autopart_shop.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import spring.project.autopart_shop.entity.Part;
import spring.project.autopart_shop.entity.PartCategory;
import spring.project.autopart_shop.repository.CarRepository;
import spring.project.autopart_shop.repository.PartRepository;
import spring.project.autopart_shop.request.PartRequest;

import java.util.List;
import java.util.Optional;

@Service
public class PartService {

    @Autowired
    private PartRepository partRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private UtilService utilService;

    public ResponseEntity<?> getPartById(Long id) {
        Optional<Part> optionalPart = partRepository.findById(id);
        if (optionalPart.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Part not found");

        return ResponseEntity.ok(optionalPart.get());
    }

    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

    public List<Part> getPartsByCategory(String category) {
        return partRepository.findAllByCategory(PartCategory.valueOf(category.toUpperCase()));
    }

    public List<Part> getPartsForCar(Long carId) {
        List<Part> parts = partRepository.findAll();
        parts.removeIf(part -> !part.getSupportedCars().contains(carRepository.findById(carId).get()));
        return parts;
    }

    public ResponseEntity<?> addPart(String accessToken, String refreshToken, HttpServletResponse response,
                                     PartRequest partRequest) {
        accessToken = utilService.validateAndRefreshToken(accessToken, refreshToken, response);
        if (accessToken == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in");

        Part part = new Part();
        part.setName(partRequest.getName());
        part.setCode(partRequest.getCode());
        part.setCategory(PartCategory.valueOf(partRequest.getCategory().toUpperCase()));
        part.setBuyPrice(partRequest.getBuyPrice());
        part.setSellPrice(partRequest.getSellPrice());
        part.setSupportedCars(partRequest.getSupportedCars().stream()
                .map(carId -> carRepository.findById(carId).get())
                .toList());

        partRepository.save(part);
        return ResponseEntity.ok("Part added successfully");
    }

    public ResponseEntity<?> deletePart(String accessToken, String refreshToken, HttpServletResponse response, Long id) {
        accessToken = utilService.validateAndRefreshToken(accessToken, refreshToken, response);
        if (accessToken == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in");

        Optional<Part> optionalPart = partRepository.findById(id);
        if (optionalPart.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manufacturer not found");

        partRepository.delete(optionalPart.get());
        return ResponseEntity.ok("Part deleted successfully");
    }
}
