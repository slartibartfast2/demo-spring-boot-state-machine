package ea.slartibartfast.statemachine.api.mapper;

import ea.slartibartfast.statemachine.api.request.CreatePaymentRequest;
import ea.slartibartfast.statemachine.model.dto.PaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreatePaymentRequestToDtoMapper {
    @Mapping(target = "paymentState", constant = "NEW")
    @Mapping(target = "accountExternalId", ignore = true)
    PaymentDto toDto(CreatePaymentRequest createPaymentRequest);
}
