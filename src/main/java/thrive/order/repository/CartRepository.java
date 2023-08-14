package thrive.order.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {
    Optional<Cart> findFirstByAccountId(Long userId);

    @Modifying
    @Transactional
    @Query("delete from CartItem c where c.cart.id = :cartId")
    void deleteCartItems(Long cartId);
}
