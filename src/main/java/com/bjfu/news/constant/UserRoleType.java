package com.bjfu.news.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum UserRoleType {
    CONTRIBUTOR("投稿人"),
    APPROVER("审稿人"),
    EDITOR("编辑人"),
   ADMIN(" 管理员");

    @Getter
    private final String desc;

    public static final Map<String, String> NAME_MAPPING;

    static {
        Map<String, String> codeMap = new LinkedHashMap<>();
        for (UserRoleType userRoleType : UserRoleType.values()) {
                codeMap.put(userRoleType.name(), userRoleType.desc);
        }
        NAME_MAPPING = Collections.unmodifiableMap(codeMap);
    }
}
