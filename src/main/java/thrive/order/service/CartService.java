package thrive.order.service;

import thrive.order.model.Cart;
import thrive.order.model.CartItem;
import thrive.order.model.Order;
import thrive.order.model.PlaceOrder;

public interface CartService {
    /**
     * Get the cart for the user. Create one, if the user doesn't have a cart.
     * @param userId
     * @return
     */
    Cart getCart(Long userId);

    /**
     * Create a cart item.
     * If the cart already has a cart item with the same product, update that cart item.
     * @param userId
     * @param item
     * @return
     */
    Cart addItem(Long userId, CartItem item);

    Cart updateItem(Long userId, Long cartItemId, CartItem item);

    Cart clearItems(Long userId);

    Order placeOrder(Long userId, PlaceOrder placeOrder);
}
