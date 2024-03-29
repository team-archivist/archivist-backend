package com.beside.archivist.dto.users;

import com.beside.archivist.entity.users.Category;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @ToString
public class UserDto {

    @Pattern(regexp = "^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+$", message = "올바른 이메일 형식이 아닙니다")
    @NotBlank(message = "이메일은 필수 값입니다.")
    private String email;

    @Size(min = 2, message = "닉네임은 2자 이상 입력해주세요") @Size(max = 20, message = "닉네임은 20자 이하로 입력해주세요")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$",message = "닉네임은 띄어쓰기 없이 한글, 영문, 숫자만 가능해요" )
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotEmpty(message = "카테고리는 필수 값입니다.")
    private List<Category> categories;

    @Builder
    public UserDto(String email, String nickname, List<Category> categories) {
        this.email = email;
        this.nickname = nickname;
        this.categories = categories;
    }
}
