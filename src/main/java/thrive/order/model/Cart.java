package thrive.order.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class Cart {
    private List<CartItem> items;
    private BigDecimal totalPrice;

    public Cart() {

    }
}
