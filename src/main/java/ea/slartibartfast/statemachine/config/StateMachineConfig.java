package ea.slartibartfast.statemachine.config;

import ea.slartibartfast.statemachine.model.action.AccountBalanceCheckAction;
import ea.slartibartfast.statemachine.model.entity.PaymentEvent;
import ea.slartibartfast.statemachine.model.entity.PaymentState;
import ea.slartibartfast.statemachine.repository.PaymentRepository;
import ea.slartibartfast.statemachine.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Slf4j
@RequiredArgsConstructor
@EnableStateMachineFactory(contextEvents = false)
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {

    private final PaymentRepository paymentRepository;

    @Override
    public void configure(StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
        states.withStates()
              .initial(PaymentState.NEW)
              .states(EnumSet.allOf(PaymentState.class))
              .end(PaymentState.AUTH)
              .end(PaymentState.PRE_AUTH_ERROR)
              .end(PaymentState.AUTH_ERROR);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config) throws Exception {
        StateMachineListenerAdapter<PaymentState, PaymentEvent> adapter = new StateMachineListenerAdapter<>(){
            @Override
            public void stateChanged(State<PaymentState, PaymentEvent> from, State<PaymentState, PaymentEvent> to) {
                log.info(String.format("State Changed from : %s, to: %s", from, to));
            }
        };

        config.withConfiguration().listener(adapter);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
        transitions.withExternal().source(PaymentState.NEW).target(PaymentState.NEW).event(PaymentEvent.PRE_AUTHORIZE)
                   .action(new AccountBalanceCheckAction(paymentRepository)).guard(paymentIdGuard())
                   .and()
                   .withExternal().source(PaymentState.NEW).target(PaymentState.PRE_AUTH).event(PaymentEvent.PRE_AUTH_APPROVED)
                   .and()
                   .withExternal().source(PaymentState.NEW).target(PaymentState.PRE_AUTH_ERROR).event(PaymentEvent.PRE_AUTH_DECLINED);
    }

    public Guard<PaymentState, PaymentEvent> paymentIdGuard(){
        return context -> context.getMessageHeader(PaymentService.PAYMENT_ID_HEADER) != null;
    }
}
