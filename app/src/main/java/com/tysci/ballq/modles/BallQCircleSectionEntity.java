package com.tysci.ballq.modles;

import android.os.Parcel;
import android.os.Parcelable;

import com.tysci.ballq.utils.ParseUtil;

/**
 * Created by Administrator on 2016/4/30.
 */
public class BallQCircleSectionEntity implements Parcelable {
    private int id;
    private int resId;
    private String name;
    private int type;
    private String description;
    private int displayIndex;
    private int postTotal;
    private int userTotal;
    private String portrait;
    private String portraitSub1;

    public int getId()
    {
        return id;
    }

    public int getResId()
    {
        return resId;
    }

    public String getName()
    {
        return name;
    }

    public int getType()
    {
        return type;
    }

    public String getDescription()
    {
        return description;
    }

    public int getDisplayIndex()
    {
        return displayIndex;
    }

    public int getPostTotal()
    {
        return postTotal;
    }

    public int getUserTotal()
    {
        return userTotal;
    }

    public String getPortrait()
    {
        return portrait;
    }

    public String getPortraitSub1()
    {
        return portraitSub1;
    }

    public static Creator<BallQCircleSectionEntity> getCREATOR()
    {
        return CREATOR;
    }

    public <T>void setId(T id)
    {
        this.id = ParseUtil.makeParse(id,0);
    }

    public <T>void setResId(T resId)
    {
        this.resId = ParseUtil.makeParse(resId,0);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public <T>void setType(T type)
    {
        this.type = ParseUtil.makeParse(type,0);
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public <T>void setDisplayIndex(T displayIndex)
    {
        this.displayIndex = ParseUtil.makeParse(displayIndex,0);
    }

    public <T>void setPostTotal(T postTotal)
    {
        this.postTotal = ParseUtil.makeParse(postTotal,0);
    }

    public <T>void setUserTotal(T userTotal)
    {
        this.userTotal = ParseUtil.makeParse(userTotal,0);
    }

    public void setPortrait(String portrait)
    {
        this.portrait = portrait;
    }

    public void setPortraitSub1(String portraitSub1)
    {
        this.portraitSub1 = portraitSub1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.resId);
        dest.writeString(this.name);
        dest.writeInt(this.type);
        dest.writeString(this.description);
        dest.writeInt(this.displayIndex);
        dest.writeInt(this.postTotal);
        dest.writeInt(this.userTotal);
        dest.writeString(this.portrait);
        dest.writeString(this.portraitSub1);
    }

    public BallQCircleSectionEntity() {
    }

    protected BallQCircleSectionEntity(Parcel in) {
        this.id = in.readInt();
        this.resId = in.readInt();
        this.name = in.readString();
        this.type = in.readInt();
        this.description = in.readString();
        this.displayIndex = in.readInt();
        this.postTotal = in.readInt();
        this.userTotal = in.readInt();
        this.portrait = in.readString();
        this.portraitSub1=in.readString();
    }

    public static final Creator<BallQCircleSectionEntity> CREATOR = new Creator<BallQCircleSectionEntity>() {
        @Override
        public BallQCircleSectionEntity createFromParcel(Parcel source) {
            return new BallQCircleSectionEntity(source);
        }

        @Override
        public BallQCircleSectionEntity[] newArray(int size) {
            return new BallQCircleSectionEntity[size];
        }
    };
}
