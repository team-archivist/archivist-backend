package com.beside.archivist.service.group;

import com.beside.archivist.entity.group.GroupImg;
import com.beside.archivist.repository.group.GroupImgRepository;
import com.beside.archivist.service.group.GroupImgService;
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

    @Override
    public GroupImg initializeDefaultImg() {
        return groupImgRepository.save(GroupImg.builder()
                .oriImgName("groupDefaultImg.png")
                .imgName("groupDefaultImg")
                .imgUrl("/image/groupDefaultImg.png")
                .build());
    }

    public GroupImg insertGroupImg(MultipartFile groupImgFile) {
        if(groupImgFile != null){

            String oriImgName = groupImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(groupImgLocation, groupImgFile);
            String imgUrl = "/images/groups/"+imgName;
            return groupImgRepository.save(GroupImg.builder()
                    .oriImgName(oriImgName)
                    .imgName(imgName)
                    .imgUrl(imgUrl)
                    .build());
        }else {
            return null;
        }
    }

    @Override
    public void updateGroupImg(Long groupImgId, MultipartFile groupImgFile) {
        if(groupImgFile != null){
            GroupImg savedGroupImg = groupImgRepository.findById(groupImgId)
                    .orElseThrow(RuntimeException::new); // TO DO : 예외 처리
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
