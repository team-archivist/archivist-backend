package com.beside.archivist.mapper;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.entity.group.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {

    @Mapping(target = "groupId", source = "id")
    @Mapping(target = "imgUrl", source = "groupImg.imgUrl")
    GroupDto toDto(Group group);
    Group toEntity(GroupDto groupDto);
}
