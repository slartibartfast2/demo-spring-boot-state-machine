package ea.slartibartfast.statemachine.model.mapper;

import ea.slartibartfast.statemachine.model.dto.PaymentDto;
import ea.slartibartfast.statemachine.model.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(source = "paymentState", target = "state")
    Payment toEntity(PaymentDto paymentDto);

    @Mapping(source = "state", target = "paymentState")
    PaymentDto fromEntity(Payment payment);
}
