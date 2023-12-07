package ea.slartibartfast.statemachine.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDto {

    private String externalId;
    private String name;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private PaymentDto latestPayment;
}
