package com.beside.archivist.service.users;

import com.beside.archivist.entity.users.User;
import com.beside.archivist.entity.users.UserImg;
import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.images.ImageNotFoundException;
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
     * User 디폴트 이미지 생성
     */
    @Override
    public UserImg initializeDefaultImg() {
        return UserImg.builder()
                .oriImgName("userDefaultImg.png")
                .imgName("userDefaultImg")
                .imgUrl("/image/userDefaultImg.png")
                .build();
    }

    /**
     * 초기 무조건 디폴트 이미지 저장
     */
    @Override
    public void saveUserImg(UserImg userImg){
        UserImg savedUserImg = userImgRepository.save(userImg);
        savedUserImg.getUsers().saveUserImg(savedUserImg);
    }

    /**
     * 디폴트 이미지 저장 이후, 모두 update
     * */
    @Override
    public void changeUserImg(Long userImgId, MultipartFile userImgFile) {
        UserImg savedUserImg = userImgRepository.findById(userImgId)
                .orElseThrow(() -> new ImageNotFoundException(ExceptionCode.IMAGE_NOT_FOUND));
        if(!(StringUtils.isEmpty(savedUserImg.getImgName()) || StringUtils.isBlank(savedUserImg.getImgName()))){
            fileService.deleteFile(userImgLocation, savedUserImg.getImgName());
        }

        String oriImgName = userImgFile.getOriginalFilename();
        String imgName = fileService.uploadFile(userImgLocation, userImgFile);
        String imgUrl = "/images/users/"+imgName;
        savedUserImg.updateUserImg(imgName,oriImgName,imgUrl);
    }
}
