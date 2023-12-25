package com.beside.archivist.dto.users;

import com.beside.archivist.entity.users.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @ToString
public class UserDto {

    @Pattern(regexp = "^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+$", message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @Size(min = 2, message = "닉네임은 2자 이상 입력해주세요") @Size(max = 20, message = "닉네임은 20자 이하로 입력해주세요")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$",message = "닉네임은 띄어쓰기 없이 한글, 영문, 숫자만 가능해요" )
    private String nickname;

    private List<Category> categories;
}
