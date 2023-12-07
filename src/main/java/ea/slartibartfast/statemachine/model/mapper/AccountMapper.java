package ea.slartibartfast.statemachine.model.mapper;

import ea.slartibartfast.statemachine.model.dto.AccountDto;
import ea.slartibartfast.statemachine.model.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(source = "latestPayment.paymentState", target = "latestPayment.state")
    @Mapping(target = "cards", ignore = true)
    Account toEntity(AccountDto accountDto);

    @Mapping(source = "latestPayment.state", target = "latestPayment.paymentState")
    AccountDto fromEntity(Account account);

    @Mapping(target = "latestPayment", ignore = true)
    AccountDto fromEntityIgnorePayment(Account account);
}

