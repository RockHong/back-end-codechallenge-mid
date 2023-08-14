package thrive.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter @Setter @ToString
public class PaymentMethod {
    private Long id;

    private String cardNumber;

    private LocalDate expiryDate;

    private String cvcNumber;

    private PaymentMethodType type;

    public PaymentMethod() {

    }
}
