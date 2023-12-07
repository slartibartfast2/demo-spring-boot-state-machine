package ea.slartibartfast.statemachine.rest;

import ea.slartibartfast.statemachine.api.PaymentApi;
import ea.slartibartfast.statemachine.api.request.CreatePaymentRequest;
import ea.slartibartfast.statemachine.model.dto.PaymentDto;
import ea.slartibartfast.statemachine.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PaymentController implements PaymentApi {

    private final PaymentService paymentService;

    @Override
    public PaymentDto retrievePayment(String paymentId) {
        return paymentService.getPayment(paymentId);
    }

    @Override
    public PaymentDto createPayment(CreatePaymentRequest createPaymentRequest) {
        return paymentService.createPayment(createPaymentRequest);
    }
}
