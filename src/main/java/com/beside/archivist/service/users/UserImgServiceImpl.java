package com.beside.archivist.service.users;

import com.beside.archivist.entity.users.User;
import com.beside.archivist.entity.users.UserImg;
import com.beside.archivist.repository.users.UserImgRepository;
import com.beside.archivist.service.util.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service @Transactional
@RequiredArgsConstructor
public class UserImgServiceImpl implements UserImgService{

    @Value("${userImgLocation}")
    private String userImgLocation;
    private final UserImgRepository userImgRepository;
    private final FileService fileService;

    /**
     * 초기 무조건 디폴트 이미지 저장
     * @return UserImg
     */
    @Override
    public UserImg initializeDefaultImg() {
        return userImgRepository.save(UserImg.builder()
                .oriImgName("userDefaultImg.png")
                .imgName("userDefaultImg")
                .imgUrl("/image/userDefaultImg.png")
                .build());
    }

    /**
     * 디폴트 이미지 저장 이후, 모두 update
     * */
    @Override
    public void changeLinkImg(Long userImgId, MultipartFile userImgFile) {
        if(userImgFile != null){
            UserImg savedUserImg = userImgRepository.findById(userImgId)
                    .orElseThrow(RuntimeException::new); // TO DO : 예외 처리
            if(!(StringUtils.isEmpty(savedUserImg.getImgName()) || StringUtils.isBlank(savedUserImg.getImgName()))){
                fileService.deleteFile(userImgLocation, savedUserImg.getImgName());
            }

            String oriImgName = userImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(userImgLocation, userImgFile);
            String imgUrl = "/images/users/"+imgName;
            savedUserImg.updateUserImg(imgName,oriImgName,imgUrl);
        }
    }
}
