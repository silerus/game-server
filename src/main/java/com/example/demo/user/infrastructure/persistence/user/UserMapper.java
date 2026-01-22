package com.example.demo.user.infrastructure.persistence.user;

import com.example.demo.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password_hash", source = "password")
    @Mapping(target = "role", source = "role")
    UserJpaEntity toJpaEntity(User domain);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password_hash")
    @Mapping(target = "role", source = "role")
    User toDomain(UserJpaEntity entity);
}
