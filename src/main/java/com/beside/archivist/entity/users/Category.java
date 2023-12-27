package com.beside.archivist.entity.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum Category {
    FOREIGN_LANGUAGES("외국어"), SELF_IMPROVEMENT("자기계발"),
    CULTURAL_ACTIVITIES("문화생활"), PHOTOGRAPHY("사진"), EXERCISE("운동"),
    FINANCIAL_MANAGEMENT("재태크"), HOBBIES("취미"), TRAVEL("여행"),
    DRAWING("드로잉"), CAREER_DEVELOPMENT("커리어")
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
