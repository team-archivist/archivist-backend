package com.beside.archivist.mapper;

import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.dto.link.LinkInfoDto;
import com.beside.archivist.entity.group.LinkGroup;
import com.beside.archivist.entity.link.Link;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LinkMapper {
    @Mapping(target = "linkId", source = "id")
    @Mapping(target = "userId", source = "users.id")
    @Mapping(target = "imgUrl", source = "linkImg.imgUrl")
    @Mapping(target = "groupList", source = "linkGroups", qualifiedByName = "mapLinkGroupsToGroupList")
    LinkInfoDto toDto(Link link);
    Link toEntity(LinkDto linkDto);

    @Named("mapLinkGroupsToGroupList")
    default List<Long> mapLinkGroupsToGroupList(List<LinkGroup> linkGroups) {
        return linkGroups.stream()
                .map(linkGroup -> linkGroup.getGroup().getId())
                .toList();
    }

}
