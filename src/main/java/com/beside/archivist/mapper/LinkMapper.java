package com.beside.archivist.mapper;

import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.entity.link.Link;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LinkMapper {
    @Mapping(target = "linkId", source = "id")
    @Mapping(target = "userId", source = "users.id")
    @Mapping(target = "imgUrl", source = "linkImg.imgUrl")
    LinkDto toDto(Link link);
    Link toEntity(LinkDto linkDto);
}
