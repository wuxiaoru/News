package com.bjfu.news.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum EditStatus {
    AGREE("接受处理"),
    REJECTION("拒绝处理");

    @Getter
    private final String desc;

    public static final Map<String, String> NAME_MAPPING;

    static {
        Map<String, String> codeMap = new LinkedHashMap<>();
        for (EditStatus editStatus : EditStatus.values()) {
            codeMap.put(editStatus.name(), editStatus.desc);
        }
        NAME_MAPPING = Collections.unmodifiableMap(codeMap);
    }
}
