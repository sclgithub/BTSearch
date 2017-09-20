package com.techscl.btsearch;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by songchunlin on 16/7/6.
 */
public class BaseModel implements Parcelable {
    //系统消息用到的字段
    private String title;
    private long sendTime;
    private int type; //0代表检查、1代表检验
    private int readStatus;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public BaseModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeLong(this.sendTime);
        dest.writeInt(this.type);
        dest.writeInt(this.readStatus);
    }

    protected BaseModel(Parcel in) {
        this.title = in.readString();
        this.sendTime = in.readLong();
        this.type = in.readInt();
        this.readStatus = in.readInt();
    }

    public static final Creator<BaseModel> CREATOR = new Creator<BaseModel>() {
        @Override
        public BaseModel createFromParcel(Parcel source) {
            return new BaseModel(source);
        }

        @Override
        public BaseModel[] newArray(int size) {
            return new BaseModel[size];
        }
    };
}
