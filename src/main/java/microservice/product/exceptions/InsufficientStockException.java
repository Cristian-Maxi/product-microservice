package microservice.product.exceptions;

import java.util.List;

public class InsufficientStockException extends RuntimeException {

    private final List<String> unavailableProducts;

    public InsufficientStockException(String message, List<String> unavailableProducts) {
        super(message);
        this.unavailableProducts = unavailableProducts;
    }

    public List<String> getUnavailableProducts() {
        return unavailableProducts;
    }
}
