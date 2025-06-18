package spring.project.autopart_shop.request;

import java.time.Year;

public class CarRequest {
    private String brand;
    private String model;
    private Year year;
    private Long manufacturerId;

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public Year getYear() {
        return year;
    }

    public Long getManufacturerId() {
        return manufacturerId;
    }
}
