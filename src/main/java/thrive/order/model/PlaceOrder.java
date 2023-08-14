package thrive.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class PlaceOrder {
    private PaymentMethod paymentMethod;

    // Others like shipping address and so on

    public PlaceOrder() {

    }
}
