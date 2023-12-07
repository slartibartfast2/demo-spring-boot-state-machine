package ea.slartibartfast.statemachine.api;

import ea.slartibartfast.statemachine.api.request.CreatePaymentRequest;
import ea.slartibartfast.statemachine.model.dto.PaymentDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
public interface PaymentApi {

    @GetMapping(value = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    PaymentDto retrievePayment(@RequestParam String paymentId);

    @PostMapping(value = "/payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    PaymentDto createPayment(@RequestBody CreatePaymentRequest createPaymentRequest);
}
