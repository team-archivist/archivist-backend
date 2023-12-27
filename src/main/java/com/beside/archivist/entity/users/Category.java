package com.beside.archivist.entity.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum Category {
    LIFESTYLE("라이프스타일"), SELF_DEVELOPMENT("자기개발"),
    CAREER("커리어"), HOBBY("취미"), EXERCISE("운동"),
    MUSIC("음악"), ENTERTAINMENT("엔터테인먼트"),
    FASHION("패션"), BEAUTY("뷰티"), TRAVEL("여행"),
    RESTAURANTS("맛집"), KNOWLEDGE("지식"),
    IT_TECHNOLOGY("IT/기술"), BUSINESS_ECONOMICS("비즈니스/경제"),
    CULTURE("문화생활"), READING("독서"),
    MOVIES("영화"), COOKING("요리");
    ;

    private final String value;

    Category(String value) {
        this.value = value;
    }

    @JsonCreator // 역 직렬화
    public static Category toCategory(String value) {
        for (Category category : Category.values()) {
            if (category.getValue().equals(value)) {
                return category;
            }
        }
        return null;
    }

    @JsonValue // 직렬화
    public String getValue() {
        return value;
    }
}
