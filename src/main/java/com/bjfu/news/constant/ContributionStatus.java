package com.bjfu.news.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum ContributionStatus {

    DRAFT(1, "草稿"),
    APPROVAL_PENDING(2, "待审稿"),
    RE_APPROVAL_PENDING(2, "重投待审稿"),
    APPROVE(3, "审稿通过等待编辑部处理"),
    APPROVAL_REJECTION(4, "审稿不过待修改"),
    HIRE(5, "编辑部已录用"),
    REJECTION(6, "编辑部已拒稿");

    @Getter
    private final int code;
    @Getter
    private final String desc;

    public static final Map<String, String> CODE_MAPPING;

    public static final Map<String, String> APPROVE_MAPPING;

    public static final Map<String, String> EDITOR_MAPPING;

    static {
        Map<String, String> codeMap = new LinkedHashMap<>();
        Map<String, String> approveMap = new LinkedHashMap<>();
        Map<String, String> editorMap = new LinkedHashMap<>();
        for (ContributionStatus contributionStatus : ContributionStatus.values()) {
            codeMap.put(contributionStatus.name(), contributionStatus.desc);
            if (!contributionStatus.equals(DRAFT)) {
                approveMap.put(contributionStatus.name(), contributionStatus.desc);
            }
            if (contributionStatus.equals(APPROVE) || contributionStatus.equals(HIRE) || contributionStatus.equals(REJECTION)) {
                editorMap.put(contributionStatus.name(), contributionStatus.desc);
            }
        }
        CODE_MAPPING = Collections.unmodifiableMap(codeMap);
        APPROVE_MAPPING = Collections.unmodifiableMap(approveMap);
        EDITOR_MAPPING = Collections.unmodifiableMap(editorMap);
    }
}
