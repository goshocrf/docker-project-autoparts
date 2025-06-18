package spring.project.autopart_shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.project.autopart_shop.entity.Manufacturer;
import spring.project.autopart_shop.request.ManufacturerRequest;
import spring.project.autopart_shop.service.ManufacturerService;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;

    @GetMapping()
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerService.getAllManufacturers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getManufacturerById(@PathVariable Long id) {
        return manufacturerService.getManufacturerById(id);
    }

    @PostMapping()
    public ResponseEntity<?> addManufacturer(@CookieValue("access_token") String accessToken,
                                             @CookieValue("refresh_token") String refreshToken, HttpServletResponse response,
                                             @RequestBody ManufacturerRequest manufacturerRequest) {
        return manufacturerService.addManufacturer(accessToken, refreshToken, response, manufacturerRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteManufacturer(@CookieValue("access_token") String accessToken,
                                                @CookieValue("refresh_token") String refreshToken, HttpServletResponse response,
                                                @PathVariable Long id) {
        return manufacturerService.deleteManufacturer(accessToken, refreshToken, response, id);
    }
}
