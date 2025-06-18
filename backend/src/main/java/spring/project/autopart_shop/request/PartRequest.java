package spring.project.autopart_shop.request;

import java.math.BigDecimal;
import java.util.List;

public class PartRequest {
    private Long id;
    private String name;
    private String code;
    private String category;
    private BigDecimal buyPrice;
    private BigDecimal sellPrice;
    private List<Long> supportedCars;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public List<Long> getSupportedCars() {
        return supportedCars;
    }
}
