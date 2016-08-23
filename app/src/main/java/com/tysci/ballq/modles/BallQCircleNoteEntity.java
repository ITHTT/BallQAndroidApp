package com.tysci.ballq.modles;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Config;

import com.tysci.ballq.utils.ParseUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 * 表示帖子实体
 */
public class BallQCircleNoteEntity implements Parcelable
{
    private int id;
    private int sectionId;
    private String sectionName;
    private String sectionPortrait;
    private String title;
    private long createTime;
    private int clickCount;
    private int commentCount;
    private int viewCount;
    private int top;
    private int good;
    private int goodTop;
    private String shareUrl;
    private int isLike;
    private int isCollect;
    private int fid;
    private List<BallQNoteContentEntity> contents;
    private BallQUserEntity creater;
    private String summaryText;

    public int getId()
    {
        return id;
    }

    public int getSectionId()
    {
        return sectionId;
    }

    public String getSectionName()
    {
        return sectionName;
    }

    public String getSectionPortrait()
    {
        return sectionPortrait;
    }

    public String getTitle()
    {
        return title;
    }

    public long getCreateTime()
    {
        return createTime;
    }

    public int getClickCount()
    {
        return clickCount;
    }

    public int getCommentCount()
    {
        return commentCount;
    }

    public int getViewCount()
    {
        return viewCount;
    }

    public int getTop()
    {
        return top;
    }

    public int getGood()
    {
        return good;
    }

    public int getGoodTop()
    {
        return goodTop;
    }

    public String getShareUrl()
    {
        return shareUrl;
    }

    public int getIsLike()
    {
        return isLike;
    }

    public int getIsCollect()
    {
        return isCollect;
    }

    public int getFid()
    {
        return fid;
    }

    public List<BallQNoteContentEntity> getContents()
    {
        return contents;
    }

    public BallQUserEntity getCreater()
    {
        return creater;
    }

    public String getSummaryText()
    {
        return summaryText;
    }

    public static Creator<BallQCircleNoteEntity> getCREATOR()
    {
        return CREATOR;
    }

    public <T> void setId(T id)
    {
        this.id = ParseUtil.makeParse(id, 0);
    }

    public <T> void setSectionId(T sectionId)
    {
        this.sectionId = ParseUtil.makeParse(sectionId, 0);
    }

    public void setSectionName(String sectionName)
    {
        this.sectionName = sectionName;
    }

    public void setSectionPortrait(String sectionPortrait)
    {
        this.sectionPortrait = sectionPortrait;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
    }

    public <T> void setClickCount(T clickCount)
    {
        this.clickCount = ParseUtil.makeParse(clickCount, 0);
    }

    public <T> void setCommentCount(T commentCount)
    {
        this.commentCount = ParseUtil.makeParse(commentCount, 0);
    }

    public <T> void setViewCount(T viewCount)
    {
        this.viewCount = ParseUtil.makeParse(viewCount, 0);
    }

    public <T> void setTop(T top)
    {
        this.top = ParseUtil.makeParse(top, 0);
    }

    public <T> void setGood(T good)
    {
        this.good = ParseUtil.makeParse(good, 0);
    }

    public <T> void setGoodTop(T goodTop)
    {
        this.goodTop = ParseUtil.makeParse(goodTop, 0);
    }

    public void setShareUrl(String shareUrl)
    {
        this.shareUrl = shareUrl;
    }

    public <T> void setIsLike(T isLike)
    {
        this.isLike = ParseUtil.makeParse(isLike, 0);
    }

    public <T> void setIsCollect(T isCollect)
    {
        this.isCollect = ParseUtil.makeParse(isCollect, 0);
    }

    public <T> void setFid(T fid)
    {
        this.fid = ParseUtil.makeParse(fid, 0);
    }

    public void setContents(List<BallQNoteContentEntity> contents)
    {
        this.contents = contents;
    }

    public void setCreater(BallQUserEntity creater)
    {
        this.creater = creater;
    }

    public void setSummaryText(String summaryText)
    {
        this.summaryText = summaryText;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.id);
        dest.writeInt(this.sectionId);
        dest.writeString(this.sectionName);
        dest.writeString(this.sectionPortrait);
        dest.writeString(this.title);
        dest.writeLong(this.createTime);
        dest.writeInt(this.clickCount);
        dest.writeInt(this.commentCount);
        dest.writeInt(this.viewCount);
        dest.writeInt(this.top);
        dest.writeInt(this.good);
        dest.writeInt(this.goodTop);
        dest.writeString(this.shareUrl);
        dest.writeInt(this.isLike);
        dest.writeInt(this.isCollect);
        dest.writeInt(this.fid);
        dest.writeTypedList(contents);
        dest.writeParcelable(this.creater, 0);
        dest.writeString(this.summaryText);
    }

    public BallQCircleNoteEntity()
    {
    }

    private BallQCircleNoteEntity(Parcel in)
    {
        this.id = in.readInt();
        this.sectionId = in.readInt();
        this.sectionName = in.readString();
        this.sectionPortrait = in.readString();
        this.title = in.readString();
        this.createTime = in.readLong();
        this.clickCount = in.readInt();
        this.commentCount = in.readInt();
        this.viewCount = in.readInt();
        this.top = in.readInt();
        this.good = in.readInt();
        this.goodTop = in.readInt();
        this.shareUrl = in.readString();
        this.isLike = in.readInt();
        this.isCollect = in.readInt();
        this.fid = in.readInt();
        if (contents != null && !contents.isEmpty())
        {
            in.readTypedList(contents, BallQNoteContentEntity.CREATOR);
        }
        this.creater = in.readParcelable(Config.class.getClassLoader());
        this.summaryText = in.readString();
    }

    public static final Creator<BallQCircleNoteEntity> CREATOR = new Creator<BallQCircleNoteEntity>()
    {
        public BallQCircleNoteEntity createFromParcel(Parcel source)
        {
            return new BallQCircleNoteEntity(source);
        }

        public BallQCircleNoteEntity[] newArray(int size)
        {
            return new BallQCircleNoteEntity[size];
        }
    };
}
