package ea.slartibartfast.statemachine.service;

import ea.slartibartfast.statemachine.api.request.CreatePaymentRequest;
import ea.slartibartfast.statemachine.model.dto.PaymentDto;
import ea.slartibartfast.statemachine.model.entity.Payment;
import ea.slartibartfast.statemachine.model.entity.PaymentEvent;
import ea.slartibartfast.statemachine.model.entity.PaymentState;
import ea.slartibartfast.statemachine.repository.AccountRepository;
import ea.slartibartfast.statemachine.repository.CardRepository;
import ea.slartibartfast.statemachine.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void should_authorize_when_amount_lower_than_100() {
        var createPaymentRequest = CreatePaymentRequest.builder()
                                                   .amount(new BigDecimal("15.75"))
                                                   .externalId(UUID.randomUUID().toString())
                                                   .cardToken("test-card")
                                                   .build();

        PaymentDto savedPayment = paymentService.createPayment(createPaymentRequest);
        StateMachine<PaymentState, PaymentEvent> stateMachine = paymentService.preAuth(savedPayment.getExternalId());
        Payment preAuthPayment = paymentRepository.getByExternalId(savedPayment.getExternalId());
        assertThat(stateMachine.getState().getId()).isEqualTo(PaymentState.PRE_AUTH);
        assertThat(preAuthPayment.getState()).isEqualTo(PaymentState.PRE_AUTH);
        assertThat(preAuthPayment.getVersion()).isEqualTo(1L);
    }

    @Test
    void should_not_authorize_when_amount_higher_than_100() {
        var createPaymentRequest = CreatePaymentRequest.builder()
                                                       .amount(new BigDecimal("100.75"))
                                                       .externalId(UUID.randomUUID().toString())
                                                       .cardToken("test-card")
                                                       .build();

        PaymentDto savedPayment = paymentService.createPayment(createPaymentRequest);
        StateMachine<PaymentState, PaymentEvent> stateMachine = paymentService.preAuth(savedPayment.getExternalId());
        Payment preAuthPayment = paymentRepository.getByExternalId(savedPayment.getExternalId());
        assertThat(stateMachine.getState().getId()).isEqualTo(PaymentState.PRE_AUTH_ERROR);
        assertThat(preAuthPayment.getState()).isEqualTo(PaymentState.PRE_AUTH_ERROR);
        assertThat(preAuthPayment.getVersion()).isEqualTo(1L);
    }
}