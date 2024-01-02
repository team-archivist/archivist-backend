package com.beside.archivist.mapper;

import com.beside.archivist.dto.group.LinkGroupDto;
import com.beside.archivist.entity.group.LinkGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LinkGroupMapper {

    @Mapping(target = "linkGroupId", source = "id")
    @Mapping(target = "linkId", source = "link.id")
    @Mapping(target = "groupId", source = "group.id")
    LinkGroupDto toDto(LinkGroup linkGroup);
    LinkGroup toEntity(LinkGroupDto linkGroupDto);
}
