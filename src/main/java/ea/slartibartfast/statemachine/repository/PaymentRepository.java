package ea.slartibartfast.statemachine.repository;

import ea.slartibartfast.statemachine.model.entity.Payment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends BaseNaturalIdRepository<Payment, UUID> {

    Optional<Payment> findPaymentByExternalId(UUID externalId);

    List<Payment> findAllByAccountExternalId(UUID accountExternalId);
}
