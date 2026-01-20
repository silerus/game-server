package com.example.demo.user.infrastructure.persistence;

import com.example.demo.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "id", source = "id")         // если имена совпадают → можно не писать
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password_hash", source = "password")
    UserJpaEntity toJpaEntity(User domain);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    User toDomain(UserJpaEntity entity);
}
