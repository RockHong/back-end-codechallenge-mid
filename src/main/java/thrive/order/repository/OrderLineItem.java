package thrive.order.repository;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="order_id", nullable=false)
    private Order order;

    @OneToOne(optional = false) // must set `optional` to false, otherwise i.getProduct().getName() is null
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
