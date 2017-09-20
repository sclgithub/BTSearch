package com.techscl.btsearch;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by songchunlin on 16/7/6.
 */
public class ResponseInfo extends BaseModel implements Parcelable {
    private int code;
    private String msg;
    //private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

   /* public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }*/

    public ResponseInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
    }

    protected ResponseInfo(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
    }

    public static final Creator<ResponseInfo> CREATOR = new Creator<ResponseInfo>() {
        @Override
        public ResponseInfo createFromParcel(Parcel source) {
            return new ResponseInfo(source);
        }

        @Override
        public ResponseInfo[] newArray(int size) {
            return new ResponseInfo[size];
        }
    };
}
