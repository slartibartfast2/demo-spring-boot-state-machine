package ea.slartibartfast.statemachine.api.request;

import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreatePaymentRequest {

    private String externalId;
    private BigDecimal amount;
    private String cardToken;
}
