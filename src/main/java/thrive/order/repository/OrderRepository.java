package thrive.order.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Collection<Order> findByAccountId(Long userId);
}
