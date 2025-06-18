package spring.project.autopart_shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.project.autopart_shop.entity.Part;
import spring.project.autopart_shop.request.PartRequest;
import spring.project.autopart_shop.service.PartService;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
public class PartController {

    @Autowired
    private PartService partService;

    @GetMapping()
    public List<Part> getAllParts() {
        return partService.getAllParts();
    }

    @GetMapping("/{partId}")
    public ResponseEntity<?> getPartById(@PathVariable Long partId) {
        return partService.getPartById(partId);
    }

    @GetMapping("/category/{category}")
    public List<Part> getPartsByCategory(@PathVariable String category) {
        return partService.getPartsByCategory(category);
    }

    @GetMapping("/car/{carId}")
    public List<Part> getPartsForCar(@PathVariable Long carId) {
        return partService.getPartsForCar(carId);
    }

    @PostMapping()
    public ResponseEntity<?> addPart(@CookieValue("access_token") String accessToken,
                                     @CookieValue("refresh_token") String refreshToken, HttpServletResponse response,
                                     @RequestBody PartRequest partRequest) {
        return partService.addPart(accessToken, refreshToken, response, partRequest);
    }

    @DeleteMapping("/{partId}")
    public ResponseEntity<?> deletePart(@CookieValue("access_token") String accessToken,
                                        @CookieValue("refresh_token") String refreshToken, HttpServletResponse response,
                                        @PathVariable Long partId) {
        return partService.deletePart(accessToken, refreshToken, response, partId);
    }
}
