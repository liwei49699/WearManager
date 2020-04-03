package com.chengzhen.wearmanager.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PeopleListResponse extends BaseResponse<List<PeopleListResponse.DataBean>>  {


    /**
     * msg : 获取成功
     * code : 0
     * data : [{"id":31,"scene_id":1,"deviceName":"test12","mobilePhone":"18751996113","imgUrl":"","aid":1,"atime":1584947258000,"mid":-1,"mtime":null,"deviceInfoList":null,"offset":null,"limit":null},{"id":32,"scene_id":1,"deviceName":"嘎嘎","mobilePhone":"187","imgUrl":null,"aid":3,"atime":1585018613000,"mid":-1,"mtime":1585019872000,"deviceInfoList":null,"offset":null,"limit":null}]
     */

//    private String msg;
//    private int code;
//    private List<DataBean> data;
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }

    public static class DataBean implements Parcelable {
        /**
         * id : 31
         * scene_id : 1
         * deviceName : test12
         * mobilePhone : 18751996113
         * imgUrl :
         * aid : 1
         * atime : 1584947258000
         * mid : -1
         * mtime : null
         * deviceInfoList : null
         * offset : null
         * limit : null
         */

        private int id;
        private int scene_id;
        private String deviceName;
        private String mobilePhone;
        private String imgUrl;
        private int aid;
        private long atime;
        private int mid;
        private String mtime;
        private String deviceInfoList;
        private String offset;
        private String limit;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getScene_id() {
            return scene_id;
        }

        public void setScene_id(int scene_id) {
            this.scene_id = scene_id;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public long getAtime() {
            return atime;
        }

        public void setAtime(long atime) {
            this.atime = atime;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public String getMtime() {
            return mtime;
        }

        public void setMtime(String mtime) {
            this.mtime = mtime;
        }

        public String getDeviceInfoList() {
            return deviceInfoList;
        }

        public void setDeviceInfoList(String deviceInfoList) {
            this.deviceInfoList = deviceInfoList;
        }

        public String getOffset() {
            return offset;
        }

        public void setOffset(String offset) {
            this.offset = offset;
        }

        public String getLimit() {
            return limit;
        }

        public void setLimit(String limit) {
            this.limit = limit;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", scene_id=" + scene_id +
                    ", deviceName='" + deviceName + '\'' +
                    ", mobilePhone='" + mobilePhone + '\'' +
                    ", imgUrl='" + imgUrl + '\'' +
                    ", aid=" + aid +
                    ", atime=" + atime +
                    ", mid=" + mid +
                    ", mtime=" + mtime +
                    ", deviceInfoList=" + deviceInfoList +
                    ", offset=" + offset +
                    ", limit=" + limit +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.scene_id);
            dest.writeString(this.deviceName);
            dest.writeString(this.mobilePhone);
            dest.writeString(this.imgUrl);
            dest.writeInt(this.aid);
            dest.writeLong(this.atime);
            dest.writeInt(this.mid);
            dest.writeString(this.mtime);
            dest.writeString(this.deviceInfoList);
            dest.writeString(this.offset);
            dest.writeString(this.limit);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.id = in.readInt();
            this.scene_id = in.readInt();
            this.deviceName = in.readString();
            this.mobilePhone = in.readString();
            this.imgUrl = in.readString();
            this.aid = in.readInt();
            this.atime = in.readLong();
            this.mid = in.readInt();
            this.mtime = in.readString();
            this.deviceInfoList = in.readString();
            this.offset = in.readString();
            this.limit = in.readString();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
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
}
