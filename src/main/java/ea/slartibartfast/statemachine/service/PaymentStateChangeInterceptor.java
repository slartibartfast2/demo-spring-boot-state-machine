package ea.slartibartfast.statemachine.service;

import ea.slartibartfast.statemachine.model.entity.Payment;
import ea.slartibartfast.statemachine.model.entity.PaymentEvent;
import ea.slartibartfast.statemachine.model.entity.PaymentState;
import ea.slartibartfast.statemachine.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {

    private final PaymentRepository paymentRepository;

    @Override
    public void preStateChange(State<PaymentState, PaymentEvent> state, Message<PaymentEvent> message,
                               Transition<PaymentState, PaymentEvent> transition, StateMachine<PaymentState, PaymentEvent> stateMachine,
                               StateMachine<PaymentState, PaymentEvent> rootStateMachine) {

        Optional.ofNullable(message)
                .flatMap(msg -> Optional.ofNullable(UUID.fromString(String.class.cast(msg.getHeaders().getOrDefault(PaymentService.PAYMENT_ID_HEADER, "11111111-1111-1111-1111-111111111111")))))
                .ifPresent(paymentId -> {
                    Payment payment = paymentRepository.getByExternalId(paymentId);
                    payment.setState(state.getId());
                    paymentRepository.save(payment);
                });
    }

}