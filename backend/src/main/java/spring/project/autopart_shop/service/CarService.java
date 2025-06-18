package spring.project.autopart_shop.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import spring.project.autopart_shop.entity.Car;
import spring.project.autopart_shop.repository.CarRepository;
import spring.project.autopart_shop.repository.ManufacturerRepository;
import spring.project.autopart_shop.request.CarRequest;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private UtilService utilService;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public ResponseEntity<?> getCarById(Long id) {
        Optional<Car> foundCar = carRepository.findById(id);
        if (foundCar.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");

        return ResponseEntity.ok(foundCar.get());
    }

    public List<Car> getCarsByManufacturer(Long manufacturerId) {
        return carRepository.findAllByManufacturerId(manufacturerId);
    }

    public ResponseEntity<?> addCar(String accessToken, String refreshToken, HttpServletResponse response,
                                    CarRequest carRequest) {
        accessToken = utilService.validateAndRefreshToken(accessToken, refreshToken, response);
        if (accessToken == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in");

        Car car = new Car();
        car.setBrand(carRequest.getBrand());
        car.setModel(carRequest.getModel());
        car.setYear(carRequest.getYear());
        car.setManufacturer(manufacturerRepository.findById(carRequest.getManufacturerId()).get());

        carRepository.save(car);
        return ResponseEntity.ok("Car added successfully");
    }

    public ResponseEntity<?> deleteCar(String accessToken, String refreshToken, HttpServletResponse response,
                                       Long id) {
        accessToken = utilService.validateAndRefreshToken(accessToken, refreshToken, response);
        if (accessToken == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in");

        Optional<Car> optionalCar = carRepository.findById(id);
        if (optionalCar.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");

        carRepository.delete(optionalCar.get());
        return ResponseEntity.ok("Car deleted successfully");
    }
}
