package ea.slartibartfast.statemachine.service;

import ea.slartibartfast.statemachine.api.mapper.CreatePaymentRequestToDtoMapper;
import ea.slartibartfast.statemachine.api.request.CreatePaymentRequest;
import ea.slartibartfast.statemachine.model.dto.PaymentDto;
import ea.slartibartfast.statemachine.model.entity.Account;
import ea.slartibartfast.statemachine.model.entity.Payment;
import ea.slartibartfast.statemachine.model.entity.PaymentEvent;
import ea.slartibartfast.statemachine.model.entity.PaymentState;
import ea.slartibartfast.statemachine.model.exception.InsufficientBalanceException;
import ea.slartibartfast.statemachine.model.mapper.PaymentMapper;
import ea.slartibartfast.statemachine.repository.CardRepository;
import ea.slartibartfast.statemachine.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CardRepository cardRepository;
    private final PaymentMapper paymentMapper;
    private final CreatePaymentRequestToDtoMapper createPaymentRequestToDtoMapper;
    private final StateMachineFactory<PaymentState, PaymentEvent> stateMachineFactory;
    private final PaymentStateChangeInterceptor paymentStateChangeInterceptor;

    public static final String PAYMENT_ID_HEADER = "payment_id";


    public PaymentDto getPayment(String externalId) {
        return paymentRepository.findPaymentByExternalId(UUID.fromString(externalId))
                                .map(paymentMapper::fromEntity)
                                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public PaymentDto createPayment(CreatePaymentRequest request) {
        var card = cardRepository.findCardByCardToken(request.getCardToken()).orElseThrow(EntityNotFoundException::new);
        var paymentDto = createPaymentRequestToDtoMapper.toDto(request);
        paymentDto.setAccountExternalId(card.getAccount().getExternalId().toString());
        var payment = paymentRepository.save(paymentMapper.toEntity(paymentDto));
        log.info("new payment created on DB");
        return paymentMapper.fromEntity(payment);
    }

    @Transactional
    public StateMachine<PaymentState, PaymentEvent> preAuth(UUID paymentId) {
        StateMachine<PaymentState, PaymentEvent> stateMachine = build(paymentId);
        sendEvent(paymentId, stateMachine, PaymentEvent.PRE_AUTHORIZE);

        return stateMachine;
    }

    public StateMachine<PaymentState, PaymentEvent> authorizePayment(UUID paymentId) {
        StateMachine<PaymentState, PaymentEvent> stateMachine = build(paymentId);
        sendEvent(paymentId, stateMachine, PaymentEvent.AUTH_APPROVED);

        return stateMachine;
    }

    public StateMachine<PaymentState, PaymentEvent> declineAuth(UUID paymentId) {
        StateMachine<PaymentState, PaymentEvent> stateMachine = build(paymentId);
        sendEvent(paymentId, stateMachine, PaymentEvent.AUTH_DECLINED);

        return stateMachine;
    }

    private Account updateAccountBalance(Payment payment, Account account) {
        if (account.getBalance().compareTo(payment.getAmount()) < 0) throw new InsufficientBalanceException();
        account.setBalance(account.getBalance().subtract(payment.getAmount()));
        return account;
    }

    private void sendEvent(UUID paymentId, StateMachine<PaymentState, PaymentEvent> stateMachine, PaymentEvent event){
        Message msg = MessageBuilder.withPayload(event)
                                    .setHeader(PAYMENT_ID_HEADER, paymentId.toString())
                                    .build();

        stateMachine.sendEvent(msg);
    }

    private StateMachine<PaymentState, PaymentEvent> build(UUID paymentId){
        Payment payment = paymentRepository.getByExternalId(paymentId);
        StateMachine<PaymentState, PaymentEvent> stateMachine = stateMachineFactory.getStateMachine(payment.getExternalId().toString());

        stateMachine.stop();

        stateMachine.getStateMachineAccessor()
          .doWithAllRegions(sma -> {
              sma.addStateMachineInterceptor(paymentStateChangeInterceptor);
              sma.resetStateMachine(new DefaultStateMachineContext<>(payment.getState(), null, null, null));
          });

        stateMachine.start();
        return stateMachine;
    }
}
