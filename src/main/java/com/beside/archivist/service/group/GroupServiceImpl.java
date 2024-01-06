package com.beside.archivist.service.group;

import com.beside.archivist.config.AuditConfig;
import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.group.GroupImg;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.mapper.GroupMapper;
import com.beside.archivist.repository.group.GroupRepository;
import com.beside.archivist.repository.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    private final GroupImgService groupImgServiceImpl;
    private final GroupMapper groupMapperImpl;

    private final AuditConfig auditConfig;

    private final UserRepository userRepository;


    @Override
    public GroupDto saveGroup(GroupDto groupDto, MultipartFile groupImgFile)  {
        Optional<String> authentication = auditConfig.auditorProvider().getCurrentAuditor();
        String email = authentication.get();
        User user = userRepository.findByEmail(email).orElseThrow();

        GroupImg groupImg;
        if(groupImgFile == null){
            groupImg = groupImgServiceImpl.initializeDefaultImg();
        }else{
            groupImg = groupImgServiceImpl.insertGroupImg(groupImgFile);
        }

        Group group = Group.builder()
                .groupName(groupDto.getGroupName())
                .groupDesc(groupDto.getGroupDesc())
                .isGroupPublic(groupDto.getIsGroupPublic())
                .categories(groupDto.getCategories())
                .groupImg(groupImg)
                .linkCount(0L)
                .build();
        groupRepository.save(group);

        return groupMapperImpl.toDto(group);
    }

    @Override
    public GroupDto updateGroup(Long groupId, GroupDto groupDto, MultipartFile groupImgFile) {
        Group group = groupRepository.findById(groupId).orElseThrow(RuntimeException::new);

        if(groupImgFile != null){
            if(group.getGroupImg() == null){
                groupImgServiceImpl.insertGroupImg(groupImgFile);
            }else{
                groupImgServiceImpl.updateGroupImg(group.getGroupImg().getId(), groupImgFile);
            }
        }

        group.update(GroupDto.builder()
                .groupName(groupDto.getGroupName())
                .groupDesc(groupDto.getGroupDesc())
                .isGroupPublic(groupDto.getIsGroupPublic())
                .categories(groupDto.getCategories())
                .build());
        return groupMapperImpl.toDto(group);
    }

    @Override
    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }

    public GroupDto findGroupById(Long id){
        // 특정 북마크 ID에 해당하는 북마크 조회
        Group group = groupRepository.findById(id).orElseThrow();

        return groupMapperImpl.toDto(group);
    }

//    public List<GroupDto> getGroupsByUserId(Long userId){
//        // 특정 사용자 ID에 해당하는 북마크 목록 조회
//        List<Group> groupList = groupRepository.findByUsers_Id(userId);
//
//        return groupList.stream()
//                .map(groupMapperImpl::toDto)
//                .collect(Collectors.toList());
//    }


    public  List<LinkDto> getLinksByGroupId(Long groupId){
        Optional<Group> groupList = groupRepository.findByIdWithLinks(groupId);

        return groupList.orElseThrow().getLinks().stream()
                .map(m-> new LinkDto(m.getLink().getId(),
                        m.getLink().getLinkUrl(),
                        m.getLink().getLinkName(),
                        m.getLink().getLinkDesc(),
                        m.getLink().getLinkImg().getImgUrl(),
                        m.getLink().getUsers().getId())
                ).toList();
    }
}
