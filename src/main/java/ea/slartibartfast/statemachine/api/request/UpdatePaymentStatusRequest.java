package ea.slartibartfast.statemachine.api.request;

import ea.slartibartfast.statemachine.model.entity.PaymentState;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdatePaymentStatusRequest {

    private String paymentId;
    private PaymentState newState;
}
