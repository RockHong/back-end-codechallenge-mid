package thrive.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import thrive.account.Account;
import thrive.order.exception.ConflictException;
import thrive.order.exception.NotEnoughStockException;
import thrive.order.exception.ResourceNotFoundException;
import thrive.order.model.Order;
import thrive.order.model.PlaceOrder;
import thrive.order.repository.*;
import thrive.order.model.CartItem;
import thrive.order.model.Cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// TODO txn-annotation Transactional
@Slf4j
@Service
public class CartServiceImpl implements CartService{
    private CartRepository cartRepository;
    private OrderRepository orderRepository;

    private InventoryService inventoryService;

    private PaymentMethodRepository paymentMethodRepository;

    public CartServiceImpl(
            CartRepository cartRepository,
            InventoryService inventoryService,
            OrderRepository orderRepository,
            PaymentMethodRepository paymentMethodRepository)
    {
        this.cartRepository = cartRepository;
        this.inventoryService = inventoryService;
        this.orderRepository = orderRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public Cart getCart(Long userId) {
        return convert(getOrCreateCartEntity(userId));
    }

    @Override
    public Cart addItem(Long userId, CartItem item) {
        var cartEntity = getOrCreateCartEntity(userId);

        thrive.order.repository.CartItem itemEntity = null;
        for (var e: cartEntity.getItems()) {
            if (e.getProduct().getId().equals(item.getProductId())) {
                log.debug("product {} already exists in a cart item", item.getProductId());
                itemEntity = e;
            }
        }
        if (itemEntity == null) {
            itemEntity = new thrive.order.repository.CartItem();
            itemEntity.setCart(cartEntity);
            cartEntity.getItems().add(itemEntity);
        }

        var product = new Product();
        product.setId(item.getProductId());
        itemEntity.setProduct(product);
        itemEntity.setQuantity(item.getQuantity());

        checkInventory(item.getProductId(), item.getQuantity());

        return convert(cartRepository.save(cartEntity));
    }

    @Override
    public Cart updateItem(Long userId, Long cartItemId, CartItem item) {
        var cartEntity = getOrCreateCartEntity(userId);
        thrive.order.repository.CartItem itemEntity = null;
        for (var i: cartEntity.getItems()) {
            if (i.getId().equals(cartItemId)) {
                itemEntity = i;
            }
        }
        if (itemEntity == null) {
            throw new ResourceNotFoundException("Cart item not found");
        }
        itemEntity.setQuantity(item.getQuantity()); // now only update quantity is supported

        checkInventory(item.getProductId(), item.getQuantity());
        return convert(cartRepository.save(cartEntity));
    }

    @Override
    public Cart clearItems(Long userId) {
        var cartEntity = getOrCreateCartEntity(userId);

        cartRepository.deleteCartItems(cartEntity.getId());

//        cartEntity.setItems(Collections.emptyList());
        return convert(getOrCreateCartEntity(userId));
    }

    @Override
    public Order placeOrder(Long userId, PlaceOrder placeOrder) {
        log.debug("request placeOrder={}", placeOrder);
        var cartEntity = getOrCreateCartEntity(userId);
        if (cartEntity.getItems().isEmpty()) {
            log.error("No item in cart");
            throw new ConflictException("No item in cart");
        }

        var cart = convert(cartEntity);

        var orderEntity = new thrive.order.repository.Order();
        for (var cartItem: cart.getItems()) {
            var lineEntity = new thrive.order.repository.OrderLineItem();
            lineEntity.setOrder(orderEntity);
            var product = new Product();
            product.setId(cartItem.getProductId());
            lineEntity.setProduct(product);
            lineEntity.setQuantity(cartItem.getQuantity());
            lineEntity.setPrice(cartItem.getUnitPrice());
            orderEntity.getLineItems().add(lineEntity);

            inventoryService.reduceStockQuantity(cartItem.getProductId(), cartItem.getQuantity());
        }

        orderEntity.setAccount(cartEntity.getAccount());
        orderEntity.setStatus(OrderStatus.PLACED);
        orderEntity.setTotal(cart.getTotalPrice());
        orderEntity.setPaymentMethod(getPaymentMethod(placeOrder, cartEntity.getAccount()));

        this.clearItems(userId);

        return convert(orderRepository.save(orderEntity));
    }

    private thrive.order.repository.PaymentMethod getPaymentMethod(PlaceOrder placeOrder, Account account) {
        if (placeOrder.getPaymentMethod().getId() != null) {
            var paymentMethod = paymentMethodRepository.findById(placeOrder.getPaymentMethod().getId())
                    .orElseThrow(() -> {
                        return new ResourceNotFoundException("Payment method not found");
                    });
            if (!paymentMethod.getAccount().getId().equals(account.getId())) {
                throw new ConflictException("Payment method from another user!");
            }
        }

        var paymentMethod = new thrive.order.repository.PaymentMethod();
        paymentMethod.setAccount(account);
        paymentMethod.setCardNumber(placeOrder.getPaymentMethod().getCardNumber());
        paymentMethod.setCvcNumber(placeOrder.getPaymentMethod().getCvcNumber());
        paymentMethod.setExpiryDate(placeOrder.getPaymentMethod().getExpiryDate());
        paymentMethod.setType(PaymentMethodType.valueOf(placeOrder.getPaymentMethod().getType().name()));

        return paymentMethod;
    }

    private thrive.order.repository.Cart getOrCreateCartEntity(Long userId) {
        return cartRepository.findFirstByAccountId(userId).orElseGet(() -> {
            log.info("creating a cart for user {}", userId);
            var cart = new thrive.order.repository.Cart();
            var account = new Account();
            account.setId(userId);
            cart.setAccount(account);
            return cartRepository.save(cart);
        });
    }

    private Cart convert(thrive.order.repository.Cart entity) {
        var cart = new Cart();
        List<CartItem> items = new ArrayList<>();
        for (var i: entity.getItems()) {
            var item = new CartItem();
            item.setId(i.getId());
            item.setProductId(i.getProduct().getId());
            item.setProductName(i.getProduct().getName());
            item.setUnitPrice(i.getProduct().getPrice());
            item.setQuantity(i.getQuantity());
            items.add(item);
        }
        cart.setItems(items);

        calculatePrices(cart);
        return cart;
    }

    private Order convert(thrive.order.repository.Order entity) {
        var order = new Order();
        order.setId(entity.getId());
        order.setTotal(entity.getTotal());
        order.setStatus(entity.getStatus().name());
        return order;
    }

    private void calculatePrices(Cart cart) {
        BigDecimal total = BigDecimal.ZERO;
        for (var item: cart.getItems()) {
            item.setTotalPrice(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));
            total = total.add(item.getTotalPrice());
        }

        cart.setTotalPrice(total);
    }

    private void checkInventory(Long productId, int quantity) {
        var inStock = inventoryService.getStockQuantity(productId);
        if (quantity > inStock) {
            log.error("not enough product in stock. productId={}, requested={}, inStock={}",
                    productId, quantity, inStock);
            throw new NotEnoughStockException("Not enough stock of product " + productId);
        }
    }
}
