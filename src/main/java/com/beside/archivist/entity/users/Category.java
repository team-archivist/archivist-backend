package com.beside.archivist.entity.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum Category {
    HOBBY("취미"), DEVELOPMENT("자기계발"), DRAWING("드로잉"), MUSIC("음악");

    @Getter
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
