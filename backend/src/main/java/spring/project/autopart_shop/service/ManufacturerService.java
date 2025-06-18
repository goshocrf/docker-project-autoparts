package spring.project.autopart_shop.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import spring.project.autopart_shop.entity.Manufacturer;
import spring.project.autopart_shop.repository.ManufacturerRepository;
import spring.project.autopart_shop.request.ManufacturerRequest;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerService {

    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private UtilService utilService;

    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    public ResponseEntity<?> getManufacturerById(Long id) {
        Optional<Manufacturer> foundManufacturer = manufacturerRepository.findById(id);
        if (foundManufacturer.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manufacturer not found");

        return ResponseEntity.ok(foundManufacturer.get());
    }

    public ResponseEntity<?> addManufacturer(String accessToken, String refreshToken, HttpServletResponse response,
                                             ManufacturerRequest manufacturerRequest) {
        accessToken = utilService.validateAndRefreshToken(accessToken, refreshToken, response);
        if (accessToken == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in");

        if (manufacturerRepository.findByName(manufacturerRequest.getName()).isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Manufacturer with this name already exists");

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(manufacturerRequest.getName());
        manufacturer.setCountry(manufacturerRequest.getCountry());
        manufacturer.setAddress(manufacturerRequest.getAddress());
        manufacturer.setPhone(manufacturerRequest.getPhone());
        manufacturer.setFax(manufacturerRequest.getFax());

        manufacturerRepository.save(manufacturer);
        return ResponseEntity.ok("Manufacturer added successfully");
    }

    public ResponseEntity<?> deleteManufacturer(String accessToken, String refreshToken, HttpServletResponse response,
                                                Long id) {
        accessToken = utilService.validateAndRefreshToken(accessToken, refreshToken, response);
        if (accessToken == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in");

        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findById(id);
        if (optionalManufacturer.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manufacturer not found");

        manufacturerRepository.delete(optionalManufacturer.get());
        return ResponseEntity.ok("Manufacturer deleted successfully");
    }
}
