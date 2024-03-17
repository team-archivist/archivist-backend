package com.beside.archivist.service.group;

import com.beside.archivist.entity.group.GroupImg;
import com.beside.archivist.entity.link.LinkImg;
import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.images.ImageNotFoundException;
import com.beside.archivist.repository.group.GroupImgRepository;
import com.beside.archivist.service.util.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service @Transactional
@RequiredArgsConstructor
public class GroupImgServiceImpl implements GroupImgService {

    @Value("${groupImgLocation}")
    private String groupImgLocation;
    private final GroupImgRepository groupImgRepository;
    private final FileService fileService;

    /**
     * 이미지 설정 안했을 때 초기 그룹 이미지는 링크 디폴트 이미지로 저장
     * @param groupImg
     * @return
     */
    @Override
    public GroupImg saveGroupImg(GroupImg groupImg) {
        GroupImg savedGroupImg = groupImgRepository.save(groupImg);
        savedGroupImg.getGroup().saveGroupImg(savedGroupImg);
        return savedGroupImg;
    }
    @Override
    public void changeToLinkImg(GroupImg groupImg, LinkImg linkImg) {
        groupImg.updateGroupImg(linkImg.getImgName(), linkImg.getOriImgName(), linkImg.getImgUrl());
    }

    @Override
    public GroupImg insertGroupImg(GroupImg groupImg, MultipartFile groupImgFile) {
        String oriImgName = groupImgFile.getOriginalFilename();
        String imgName = fileService.uploadFile(groupImgLocation, groupImgFile);
        String imgUrl = "/images/groups/"+imgName;

        groupImg.updateGroupImg(imgName,oriImgName,imgUrl);
        GroupImg savedGroupImg = groupImgRepository.save(groupImg);
        savedGroupImg.getGroup().saveGroupImg(savedGroupImg);

        return savedGroupImg;
    }

    @Override
    public void changeGroupImg(Long groupImgId, MultipartFile groupImgFile) {
        if(groupImgFile != null){
            GroupImg savedGroupImg = groupImgRepository.findById(groupImgId)
                    .orElseThrow(() -> new ImageNotFoundException(ExceptionCode.IMAGE_NOT_FOUND));
            if(!(StringUtils.isEmpty(savedGroupImg.getImgName()) || StringUtils.isBlank(savedGroupImg.getImgName()))){
                fileService.deleteFile(groupImgLocation, savedGroupImg.getImgName());
            }

            String oriImgName = groupImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(groupImgLocation, groupImgFile);
            String imgUrl = "/images/groups/"+imgName;
            savedGroupImg.updateGroupImg(imgName,oriImgName,imgUrl);
        }
    }
}
