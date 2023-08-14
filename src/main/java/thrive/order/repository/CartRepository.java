package thrive.order.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {
    Optional<Cart> findFirstByAccountId(Long userId);
}
