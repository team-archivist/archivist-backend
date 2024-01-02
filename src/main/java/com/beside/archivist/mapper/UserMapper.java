package com.beside.archivist.mapper;

import com.beside.archivist.dto.users.UserInfoDto;
import com.beside.archivist.entity.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "userId", source = "id")
    @Mapping(target = "imgUrl", source = "userImg.imgUrl")
    UserInfoDto toDto(User user);
    User toEntity(UserInfoDto userInfoDto);
}
