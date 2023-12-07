package ea.slartibartfast.statemachine.repository;

import ea.slartibartfast.statemachine.model.entity.Card;

import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends BaseNaturalIdRepository<Card, UUID> {

    Optional<Card> findCardByCardToken(String cardToken);
}
