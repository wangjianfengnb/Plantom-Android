package com.phantom.sample.api;

import java.util.List;

/**
 * 群组信息
 *
 * @author Jianfeng Wang
 * @since 2019/12/5 15:32
 */
public class GroupResponse {

    /**
     * 群组ID
     */
    private Long groupId;
    /**
     * 群组名称
     */
    private String groupName;
    /**
     * 群组头像
     */
    private String groupAvatar;

    /**
     * 成员
     */
    private List<String> members;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupAvatar() {
        return groupAvatar;
    }

    public void setGroupAvatar(String groupAvatar) {
        this.groupAvatar = groupAvatar;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
