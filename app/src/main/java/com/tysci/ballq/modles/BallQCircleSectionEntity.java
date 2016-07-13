package com.tysci.ballq.modles;

import android.os.Parcel;
import android.os.Parcelable;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisplayIndex() {
        return displayIndex;
    }

    public void setDisplayIndex(int displayIndex) {
        this.displayIndex = displayIndex;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getPostTotal() {
        return postTotal;
    }

    public void setPostTotal(int postTotal) {
        this.postTotal = postTotal;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserTotal() {
        return userTotal;
    }

    public void setUserTotal(int userTotal) {
        this.userTotal = userTotal;
    }

    public String getPortraitSub1() {
        return portraitSub1;
    }

    public void setPortraitSub1(String portraitSub1) {
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
