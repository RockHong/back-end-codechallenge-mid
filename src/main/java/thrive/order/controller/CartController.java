package thrive.order.controller;

import org.springframework.web.bind.annotation.*;
import thrive.order.model.Cart;
import thrive.order.model.CartItem;
import thrive.order.service.CartService;

@RestController
public class CartController {
    private CartService cartService;

    CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart/{userId}")
    Cart getCart(@PathVariable Long userId) {
        return cartService.getCart(userId);
    }

    @PostMapping("/cart/{userId}/items")
    Cart addItem(@PathVariable Long userId, @RequestBody CartItem item) {
        return cartService.addItem(userId, item);
    }

    @PutMapping("/cart/{userId}/items/{cartItemId}")
    Cart updateItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestBody CartItem item)
    {
        return cartService.updateItem(userId, cartItemId, item);
    }

    @DeleteMapping("/cart/{userId}/items")
    Cart clearCart(@PathVariable Long userId) {
        return cartService.clearItems(userId);
    }
}
