package thrive.order.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class Order {
    public Order() {

    }

    private Long id;
    private String status;
    private BigDecimal total;

    private List<OrderLineItem> lineItems;
}
