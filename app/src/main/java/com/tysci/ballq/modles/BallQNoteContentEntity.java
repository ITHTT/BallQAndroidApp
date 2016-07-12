package com.tysci.ballq.modles;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/27.
 * 帖子内容实体
 */
public class BallQNoteContentEntity implements Parcelable {
    private int type;
    private String content;
    private String original;
    private int width;
    private int height;
    private int length;
    private String mimeType;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.content);
        dest.writeString(this.original);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.length);
        dest.writeString(this.mimeType);
    }

    public BallQNoteContentEntity() {
    }

    private BallQNoteContentEntity(Parcel in) {
        this.type = in.readInt();
        this.content = in.readString();
        this.original = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.length = in.readInt();
        this.mimeType = in.readString();
    }

    public static final Creator<BallQNoteContentEntity> CREATOR = new Creator<BallQNoteContentEntity>() {
        public BallQNoteContentEntity createFromParcel(Parcel source) {
            return new BallQNoteContentEntity(source);
        }

        public BallQNoteContentEntity[] newArray(int size) {
            return new BallQNoteContentEntity[size];
        }
    };
}
