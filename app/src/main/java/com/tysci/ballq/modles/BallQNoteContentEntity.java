package com.tysci.ballq.modles;

import android.os.Parcel;
import android.os.Parcelable;

import com.tysci.ballq.utils.ParseUtil;

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

    public int getType()
    {
        return type;
    }

    public String getContent()
    {
        return content;
    }

    public String getOriginal()
    {
        return original;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getLength()
    {
        return length;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public <T>void setType(T type)
    {
        this.type = ParseUtil.makeParse(type,0);
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setOriginal(String original)
    {
        this.original = original;
    }

    public <T>void setWidth(T width)
    {
        this.width = ParseUtil.makeParse(width,0);
    }

    public <T>void setHeight(T height)
    {
        this.height = ParseUtil.makeParse(height,0);
    }

    public <T>void setLength(T length)
    {
        this.length = ParseUtil.makeParse(length,0);
    }

    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
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
