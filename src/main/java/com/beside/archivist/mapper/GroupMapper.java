package com.beside.archivist.mapper;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.dto.group.GroupInfoDto;
import com.beside.archivist.entity.group.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {

    @Mapping(target = "groupId", source = "id")
    @Mapping(target = "imgUrl", source = "groupImg.imgUrl")
    GroupInfoDto toDto(Group group);
    Group toEntity(GroupInfoDto groupInfoDto);
    @Mapping(target = "groupId", source = "id")
    @Mapping(target = "imgUrl", source = "groupImg.imgUrl")
    List<GroupInfoDto> toDtoList(List<Group> groups);
}
