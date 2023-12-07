package ea.slartibartfast.statemachine.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity(name = "accounts")
@Table(name = "accounts")
@SQLRestriction("deleted_at is null")
@NamedEntityGraph(name = "Account.latestPayment",
        attributeNodes = @NamedAttributeNode("latestPayment")
)
@NamedEntityGraph(name = "Account.cards",
        attributeNodes = @NamedAttributeNode("cards")
)
public class Account extends AuditBaseEntity {

    private String name;

    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinFormula("(" +
            "SELECT trx.id " +
            "FROM payments trx " +
            "WHERE trx.account_external_id = external_id " +
            "ORDER BY trx.created_at DESC " +
            "LIMIT 1" +
            ")")
    @JsonBackReference
    private Payment latestPayment;

    @OneToMany(mappedBy = "account")
    @JsonBackReference
    private Set<Card> cards = new HashSet<>();
}
