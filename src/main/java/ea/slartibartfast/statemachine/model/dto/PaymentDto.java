package ea.slartibartfast.statemachine.model.dto;

import ea.slartibartfast.statemachine.model.entity.PaymentState;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentDto {

    private UUID externalId;
    private BigDecimal amount;
    private PaymentState paymentState;
    private String accountExternalId;
}
