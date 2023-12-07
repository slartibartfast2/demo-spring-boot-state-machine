package ea.slartibartfast.statemachine.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity(name = "payments")
@Table(name = "payments")
@SQLRestriction("deleted_at is null")
public class Payment extends AuditBaseEntity {

    @Column(updatable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private PaymentState state;

    @Column(name = "account_external_id")
    private UUID accountExternalId;
}
