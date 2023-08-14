package thrive.order.model;

import java.math.BigDecimal;

public class OrderLineItem {
    private Long id;

    private Long productId;

    private String productName;

    private BigDecimal unitPrice;

    private Integer quantity;

    public OrderLineItem() {

    }
}
