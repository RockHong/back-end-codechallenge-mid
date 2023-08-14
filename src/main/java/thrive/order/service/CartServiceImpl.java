package thrive.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import thrive.account.Account;
import thrive.order.exception.ResourceNotFoundException;
import thrive.order.repository.Product;
import thrive.order.model.CartItem;
import thrive.order.repository.CartRepository;
import thrive.order.model.Cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO txn-annotation Transactional
@Slf4j
@Service
public class CartServiceImpl implements CartService{
    private CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
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
        return convert(cartRepository.save(cartEntity));
    }

    @Override
    public Cart clearItems(Long userId) {
        var cartEntity = getOrCreateCartEntity(userId);
        cartEntity.setItems(Collections.emptyList());
        return convert(cartRepository.save(cartEntity));
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

    private void calculatePrices(Cart cart) {
        BigDecimal total = BigDecimal.ZERO;
        for (var item: cart.getItems()) {
            item.setTotalPrice(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));
            total = total.add(item.getTotalPrice());
        }

        cart.setTotalPrice(total);
    }

}
