package ea.slartibartfast.statemachine.repository;

import ea.slartibartfast.statemachine.model.entity.AuditBaseEntity;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Transactional(readOnly = true)
public class BaseNaturalIdRepositoryImpl<T extends AuditBaseEntity, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseNaturalIdRepository<T, ID> {

    private final EntityManager entityManager;

    public BaseNaturalIdRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public T getByExternalId(ID externalId) {
        return entityManager.unwrap(Session.class)
                            .bySimpleNaturalId(this.getDomainClass())
                            .load(externalId);
    }
}