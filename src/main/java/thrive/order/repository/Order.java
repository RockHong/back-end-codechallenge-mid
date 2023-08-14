package thrive.order.repository;

import jakarta.persistence.*;
import thrive.account.Account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SALES_ORDER")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="account_id", nullable=false)
    private Account account;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name="payment_method_id")
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy="order", cascade = CascadeType.ALL)
    private List<OrderLineItem> lineItems = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderLineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<OrderLineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
