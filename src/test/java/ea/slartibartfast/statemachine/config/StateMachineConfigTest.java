package ea.slartibartfast.statemachine.config;

import ea.slartibartfast.statemachine.model.entity.PaymentEvent;
import ea.slartibartfast.statemachine.model.entity.PaymentState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    StateMachineFactory<PaymentState, PaymentEvent> factory;

    @Test
    void testNewStateMachine() {
        StateMachine<PaymentState, PaymentEvent> stateMachine = factory.getStateMachine(UUID.randomUUID());

        stateMachine.start();
        assertThat(stateMachine.getState().getId()).isEqualTo(PaymentState.NEW);
        stateMachine.sendEvent(PaymentEvent.PRE_AUTHORIZE);
        assertThat(stateMachine.getState().getId()).isEqualTo(PaymentState.NEW);
        stateMachine.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);
        assertThat(stateMachine.getState().getId()).isEqualTo(PaymentState.PRE_AUTH);
        stateMachine.sendEvent(PaymentEvent.PRE_AUTH_DECLINED);
        assertThat(stateMachine.getState().getId()).isEqualTo(PaymentState.PRE_AUTH);
    }
}