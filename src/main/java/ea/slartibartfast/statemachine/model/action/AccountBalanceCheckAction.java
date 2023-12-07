package ea.slartibartfast.statemachine.model.action;

import ea.slartibartfast.statemachine.model.entity.Payment;
import ea.slartibartfast.statemachine.model.entity.PaymentEvent;
import ea.slartibartfast.statemachine.model.entity.PaymentState;
import ea.slartibartfast.statemachine.repository.PaymentRepository;
import ea.slartibartfast.statemachine.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class AccountBalanceCheckAction implements Action<PaymentState, PaymentEvent> {

    private final PaymentRepository paymentRepository;

    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> context) {
        log.info("PreAuth was called!!!");
        var paymentId = UUID.fromString(String.class.cast(context.getMessageHeaders().getOrDefault(PaymentService.PAYMENT_ID_HEADER, "11111111-1111-1111-1111-111111111111")));
        Payment payment = paymentRepository.getByExternalId(paymentId);

        if (payment.getAmount().compareTo(BigDecimal.valueOf(100L)) < 0) {
            log.info("Approved");
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_APPROVED)
                                                              .setHeader(PaymentService.PAYMENT_ID_HEADER, context.getMessageHeader(PaymentService.PAYMENT_ID_HEADER))
                                                              .build());

        } else {
            log.warn("Declined! No Credit!!!!!!");
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_DECLINED)
                                                              .setHeader(PaymentService.PAYMENT_ID_HEADER, context.getMessageHeader(PaymentService.PAYMENT_ID_HEADER))
                                                              .build());
        }
    }
}