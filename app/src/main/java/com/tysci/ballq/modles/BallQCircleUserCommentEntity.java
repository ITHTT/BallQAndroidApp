package com.tysci.ballq.modles;

import java.util.List;

/**
 * Created by Administrator on 2016/5/3.
 * 圈子中用户评论的实体
 */
public class BallQCircleUserCommentEntity {
    private int topicId;
    private BallQUserEntity creator;
    private BallQUserEntity replied;
    private long createTime;
    private int indexCount;
    private List<BallQNoteContentEntity>content;

    public List<BallQNoteContentEntity> getContent() {
        return content;
    }

    public void setContent(List<BallQNoteContentEntity> content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public BallQUserEntity getCreator() {
        return creator;
    }

    public void setCreator(BallQUserEntity creator) {
        this.creator = creator;
    }

    public int getIndexCount() {
        return indexCount;
    }

    public void setIndexCount(int indexCount) {
        this.indexCount = indexCount;
    }

    public BallQUserEntity getReplied() {
        return replied;
    }

    public void setReplied(BallQUserEntity replied) {
        this.replied = replied;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }
}
