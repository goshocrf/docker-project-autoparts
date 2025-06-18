package spring.project.autopart_shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.project.autopart_shop.entity.Car;
import spring.project.autopart_shop.request.CarRequest;
import spring.project.autopart_shop.service.CarService;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping()
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{carId}")
    public ResponseEntity<?> getCarById(@PathVariable Long carId) {
        return carService.getCarById(carId);
    }

    @GetMapping("/manufacturer/{manufacturerId}")
    public List<Car> getCarsByManufacturer(@PathVariable Long manufacturerId) {
        return carService.getCarsByManufacturer(manufacturerId);
    }

    @PostMapping()
    public ResponseEntity<?> addCar(@CookieValue("access_token") String accessToken,
                                    @CookieValue("refresh_token") String refreshToken, HttpServletResponse response,
                                    @RequestBody CarRequest carRequest) {
        return carService.addCar(accessToken, refreshToken, response, carRequest);
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<?> deleteCar(@CookieValue("access_token") String accessToken,
                                       @CookieValue("refresh_token") String refreshToken, HttpServletResponse response,
                                       @PathVariable Long carId) {
        return carService.deleteCar(accessToken, refreshToken, response, carId);
    }
}
