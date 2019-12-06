package com.phantom.sample.api;

/**
 * 加入群组
 *
 * @author Jianfeng Wang
 * @since 2019/12/5 15:44
 */
public class JoinGroupRequest {
    /**
     * 用户ID
     */
    private String userAccount;
    /**
     * 群组ID
     */
    private Long groupId;

    /**
     * 关系ID
     */
    private Long relationshipId;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(Long relationshipId) {
        this.relationshipId = relationshipId;
    }
}
