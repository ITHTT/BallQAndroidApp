package com.tysci.ballq.modles;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/27.
 */
public class BallQUserAchievementEntity implements Parcelable {
    public int id;
    public int type;
    public String name;
    public String logo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
        dest.writeString(this.name);
        dest.writeString(this.logo);
    }

    public BallQUserAchievementEntity() {
    }

    private BallQUserAchievementEntity(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.name = in.readString();
        this.logo = in.readString();
    }

    public static final Creator<BallQUserAchievementEntity> CREATOR = new Creator<BallQUserAchievementEntity>() {
        public BallQUserAchievementEntity createFromParcel(Parcel source) {
            return new BallQUserAchievementEntity(source);
        }

        public BallQUserAchievementEntity[] newArray(int size) {
            return new BallQUserAchievementEntity[size];
        }
    };
}
