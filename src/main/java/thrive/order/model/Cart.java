package thrive.order.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Cart {
    private List<CartItem> items;

    public Cart() {

    }
}
