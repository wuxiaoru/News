package com.bjfu.news.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum ApproveStatus {
    AGREE("通过"),
    REJECTION("不通过");

    @Getter
    private final String desc;

    public static final Map<String, String> NAME_MAPPING;

    static {
        Map<String, String> codeMap = new LinkedHashMap<>();
        for (ApproveStatus approveStatus : ApproveStatus.values()) {
            codeMap.put(approveStatus.name(), approveStatus.desc);
        }
        NAME_MAPPING = Collections.unmodifiableMap(codeMap);
    }
}
