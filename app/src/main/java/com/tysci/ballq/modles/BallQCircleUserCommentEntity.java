package com.tysci.ballq.modles;

import com.tysci.ballq.utils.ParseUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/5/3.
 * 圈子中用户评论的实体
 */
public class BallQCircleUserCommentEntity
{
    private int topicId;
    private BallQUserEntity creator;
    private BallQUserEntity replied;
    private long createTime;
    private int indexCount;
    private List<BallQNoteContentEntity> content;

    public List<BallQNoteContentEntity> getContent()
    {
        return content;
    }

    public void setContent(List<BallQNoteContentEntity> content)
    {
        this.content = content;
    }

    public long getCreateTime()
    {
        return createTime;
    }

    public <T> void setCreateTime(T createTime)
    {
        this.createTime = ParseUtil.makeParse(createTime, 0L);
    }

    public BallQUserEntity getCreator()
    {
        return creator;
    }

    public void setCreator(BallQUserEntity creator)
    {
        this.creator = creator;
    }

    public int getIndexCount()
    {
        return indexCount;
    }

    public <T> void setIndexCount(T indexCount)
    {
        this.indexCount = ParseUtil.makeParse(indexCount, 0);
    }

    public BallQUserEntity getReplied()
    {
        return replied;
    }

    public void setReplied(BallQUserEntity replied)
    {
        this.replied = replied;
    }

    public int getTopicId()
    {
        return topicId;
    }

    public <T> void setTopicId(T topicId)
    {
        this.topicId = ParseUtil.makeParse(topicId, 0);
    }
}
