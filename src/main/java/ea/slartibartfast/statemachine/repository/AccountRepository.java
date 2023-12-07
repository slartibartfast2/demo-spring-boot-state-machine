package ea.slartibartfast.statemachine.repository;

import ea.slartibartfast.statemachine.model.entity.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends BaseNaturalIdRepository<Account, UUID> {

    Optional<Account> findAccountByExternalId(UUID externalId);

    @EntityGraph(value = "Account.latestPayment")
    Optional<Account> findAccountWithLatestPaymentByExternalId(UUID externalId);

    @EntityGraph(value = "Account.cards")
    List<Account> findAccountWithCardsByExternalId(UUID externalId);

    @Query("select a from accounts a inner join payments p on p.accountExternalId = a.externalId and p.externalId = :paymentExternalId")
    Optional<Account> findAccountByPayment(UUID paymentExternalId);
}
