package ea.slartibartfast.statemachine.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity(name = "cards")
@Table(name = "cards")
@SQLRestriction("deleted_at is null")
public class Card extends AuditBaseEntity {

    @Column(name = "card_token", unique = true)
    private String cardToken;

    @Column(name = "card_limit")
    private BigDecimal cardLimit;

    @ManyToOne
    @JoinColumn(name = "account_external_id", referencedColumnName = "external_id")
    @JsonManagedReference
    private Account account;
}
