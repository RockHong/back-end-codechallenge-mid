package thrive.order.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Long> {
}
