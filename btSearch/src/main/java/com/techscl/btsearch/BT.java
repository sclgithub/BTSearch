package com.techscl.btsearch;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者 songchunlin.
 * 创建时间 2017/9/20 00:01.
 * 邮箱 songchunlin1314@gmail.com
 * 备注:
 * 修改者 songchunlin
 * 修改时间 2017/9/20 00:01.
 * 邮箱
 * 备注:
 */

public class BT implements Parcelable {

    /**
     * code : 0
     * msg : success
     * data : [{"id":1,"name":"scl","content":"content","imgUrl":"default","download":"11","uriId":"sss","uriContent":"sss","updateTime":1505813158408},{"id":2,"name":"scl","content":"content","imgUrl":"default","download":"11","uriId":"sss","uriContent":"sss","updateTime":1505813258927},{"id":3,"name":"scl","content":"content","imgUrl":"default","download":"11","uriId":"sss","uriContent":"sss","updateTime":1505813158408},{"id":4,"name":"scl","content":"content","imgUrl":"default","download":"11","uriId":"sss","uriContent":"sss","updateTime":1505813158408},{"id":5,"name":"scl","content":"content","imgUrl":"default","download":"11","uriId":"sss","uriContent":"sss","updateTime":1505813158408}]
     */

    private String code;
    private String msg;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * id : 1
         * name : scl
         * content : content
         * imgUrl : default
         * download : 11
         * uriId : sss
         * uriContent : sss
         * updateTime : 1505813158408
         */

        private int id;
        private String name;
        private String content;
        private String imgUrl;
        private String download;
        private String uriId;
        private String uriContent;
        private long updateTime;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }

        public String getUriId() {
            return uriId;
        }

        public void setUriId(String uriId) {
            this.uriId = uriId;
        }

        public String getUriContent() {
            return uriContent;
        }

        public void setUriContent(String uriContent) {
            this.uriContent = uriContent;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeString(this.content);
            dest.writeString(this.imgUrl);
            dest.writeString(this.download);
            dest.writeString(this.uriId);
            dest.writeString(this.uriContent);
            dest.writeLong(this.updateTime);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
            this.content = in.readString();
            this.imgUrl = in.readString();
            this.download = in.readString();
            this.uriId = in.readString();
            this.uriContent = in.readString();
            this.updateTime = in.readLong();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.msg);
        dest.writeList(this.data);
    }

    public BT() {
    }

    protected BT(Parcel in) {
        this.code = in.readString();
        this.msg = in.readString();
        this.data = new ArrayList<DataBean>();
        in.readList(this.data, DataBean.class.getClassLoader());
    }

    public static final Creator<BT> CREATOR = new Creator<BT>() {
        @Override
        public BT createFromParcel(Parcel source) {
            return new BT(source);
        }

        @Override
        public BT[] newArray(int size) {
            return new BT[size];
        }
    };
}
