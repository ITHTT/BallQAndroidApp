package com.tysci.ballq.modles;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class BallQUserEntity implements Parcelable {
    private String className;
    private String portrait;
    private String firstName;
    private int userId;
    private int isAuthor;
    private int isV;
    private List<BallQUserAchievementEntity>achievements;

    public List<BallQUserAchievementEntity> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<BallQUserAchievementEntity> achievements) {
        this.achievements = achievements;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getIsAuthor() {
        return isAuthor;
    }

    public void setIsAuthor(int isAuthor) {
        this.isAuthor = isAuthor;
    }

    public int getIsV() {
        return isV;
    }

    public void setIsV(int isV) {
        this.isV = isV;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.className);
        dest.writeString(this.portrait);
        dest.writeString(this.firstName);
        dest.writeInt(this.userId);
        dest.writeInt(this.isAuthor);
        dest.writeInt(this.isV);
        dest.writeTypedList(achievements);
    }

    public BallQUserEntity() {
    }

    private BallQUserEntity(Parcel in) {
        this.className = in.readString();
        this.portrait = in.readString();
        this.firstName = in.readString();
        this.userId = in.readInt();
        this.isAuthor = in.readInt();
        this.isV = in.readInt();
        in.readTypedList(achievements, BallQUserAchievementEntity.CREATOR);
    }

    public static final Creator<BallQUserEntity> CREATOR = new Creator<BallQUserEntity>() {
        public BallQUserEntity createFromParcel(Parcel source) {
            return new BallQUserEntity(source);
        }

        public BallQUserEntity[] newArray(int size) {
            return new BallQUserEntity[size];
        }
    };
}
