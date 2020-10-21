package com.bjfu.news.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OperateType {

    CONTRIBUTOR_SUBMIT("投稿人提交"),
    APPROVE_SUBMIT("审稿人审稿"),
    CONTRIBUTOR_WITH_DRAW("投稿人撤回"),
    CONTRIBUTOR_EDIT("投稿人编辑"),
    APPROVE_WITH_DRAW("审稿人撤销"),
    EDITOR_SUBMIT("编辑人审批");

    @Getter
    private final String desc;
}
