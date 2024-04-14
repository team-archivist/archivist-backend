package com.beside.archivist.service.group;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.group.GroupImg;
import com.beside.archivist.entity.link.LinkImg;
import com.beside.archivist.entity.usergroup.UserGroup;
import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.group.GroupNotFoundException;
import com.beside.archivist.mapper.GroupMapper;
import com.beside.archivist.repository.group.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupImgService groupImgServiceImpl;
    private final GroupMapper groupMapperImpl;

    /**
     * 그룹 생성 - 이미지 없을 때는 디폴트 이미지로 저장
     * @param groupDto
     * @param groupImgFile
     * @return GroupDto
     */
    @Override
    public GroupDto saveGroup(GroupDto groupDto, MultipartFile groupImgFile)  {

        Group savedGroup = groupRepository.save(
                Group.builder()
                        .groupName(groupDto.getGroupName())
                        .groupDesc(groupDto.getGroupDesc())
                        .isGroupPublic(groupDto.getIsGroupPublic()) // Y or N
                        .categories(groupDto.getCategories())
                        .linkCount(0L)
                        .build()
        );

        GroupImg groupImg = GroupImg.initializeDefaultLinkImg();
        groupImg.saveGroup(savedGroup);

        if(groupImgFile != null){ // 그룹 이미지를 넣지 않았을 때 링크 디폴트 이미지로 설정
            groupImgServiceImpl.insertGroupImg(groupImg, groupImgFile);
        }else {
            groupImgServiceImpl.saveGroupImg(groupImg);
        }

        return groupMapperImpl.toDto(savedGroup);
    }

    /**
     * 그룹 이미지가 없을 때 내부 링크 이미지 중 하나로 변경
     * @param groupId
     * @param linkImg
     */
    @Override
    public void changeToLinkImg(Long groupId, LinkImg linkImg) { // 해당 링크 이미지 삭제했을 떄 다른 이미지로 바꾸는 등의 작업 필요
        GroupImg groupImg = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(ExceptionCode.GROUP_NOT_FOUND))
                .getGroupImg();
        groupImgServiceImpl.changeToLinkImg(groupImg,linkImg); // groupImg -> linkImg 로 변경
    }

    /**
     * 그룹 정보 수정
     * @param groupId
     * @param groupDto
     * @param groupImgFile
     * @return GroupDto
     */
    @Override
    public GroupDto updateGroup(Long groupId, GroupDto groupDto, MultipartFile groupImgFile) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(ExceptionCode.GROUP_NOT_FOUND));

        if(groupImgFile != null){
            groupImgServiceImpl.changeGroupImg(group.getGroupImg().getId(), groupImgFile);
        }

        group.update(GroupDto.builder()
                .groupName(groupDto.getGroupName())
                .groupDesc(groupDto.getGroupDesc())
                .isGroupPublic(groupDto.getIsGroupPublic())
                .categories(groupDto.getCategories())
                .build());
        return groupMapperImpl.toDto(group);
    }

    /**
     * 그룹 단일 조회
     * @param groupId
     * @return Group
     */
    @Override
    public Group getGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(ExceptionCode.GROUP_NOT_FOUND));
    }

    /**
     * 그룹 삭제
     * @param groupId
     */
    @Override
    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }

    /**
     * 그룹 단일 조회
     * @param id
     * @return GroupDto
     */
    @Override
    public GroupDto findGroupById(Long id){
        Group group = groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException(ExceptionCode.GROUP_NOT_FOUND));
        return groupMapperImpl.toDto(group);
    }

    /**
     * 특정 그룹 내 모든 링크 조회
     * @param groupId
     * @return List<LinkDto>
     */
    @Override
    public List<LinkDto> getLinksByGroupId(Long groupId){
        Optional<Group> groupList = groupRepository.findByIdWithLinks(groupId);

        return groupList.orElseThrow(() -> new GroupNotFoundException(ExceptionCode.GROUP_NOT_FOUND))
                .getLinks().stream()
                .map(m-> new LinkDto(m.getLink().getId(),
                        m.getLink().getLinkUrl(),
                        m.getLink().getLinkName(),
                        m.getLink().getLinkDesc(),
                        m.getLink().getLinkImg().getImgUrl(),
                        m.getLink().getUsers().getId())
                ).toList();
    }
}
