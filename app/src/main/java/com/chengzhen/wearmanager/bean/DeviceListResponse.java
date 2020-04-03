package com.chengzhen.wearmanager.bean;

import java.util.List;

public class DeviceListResponse extends BaseResponse<List<DeviceListResponse.DataBean>>{

    /**
     * msg : 获取成功
     * code : 0
     * data : [{"device_id":"1703328863","user_id":3,"device_no":"3G","deviceName":"张三","deviceType":"1","mobilePhone":"13888888888","lonType":"E","longitude":"118.8870100","latType":"N","latitude":"32.093495","lastActive":1579405054000,"powerLevel":"79","posAlert":1,"bs":"0","fgcs":"0","status":"1","gy":"128","dy":"82","xl":"0"}]
     */
//
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

    public static class DataBean {
        /**
         * 		"name": "1",
         * device_id : 1703328863
         * user_id : 3
         * device_no : 3G
         * deviceName : 张三
         * deviceType : 1
         * mobilePhone : 13888888888
         * lonType : E
         * longitude : 118.8870100
         * latType : N
         * latitude : 32.093495
         * lastActive : 1579405054000
         * powerLevel : 79
         * posAlert : 1
         * bs : 0
         * fgcs : 0
         * "iot_node_status": 17,
         * status : 1
         * gy : 128
         * dy : 82
         * xl : 0
         * alarmFlag
         * "scene_id": 1
         */

        //"name": "1",
        //		"device_id": "121",
        //		"user_id": 3,
        //		"device_no": "1231",
        //		"deviceName": "313",
        //		"deviceType": "1",
        //		"mobilePhone": "138888888",
        //		"lonType": null,
        //		"longitude": null,
        //		"latType": null,
        //		"latitude": null,
        //		"lastActive": null,
        //		"powerLevel": null,
        //		"posAlert": null,
        //		"bs": null,
        //		"fgcs": null,
        //		"iot_node_status": 17,
        //		"gy": null,
        //		"dy": null,
        //		"xl": null,
        //		"alertflag": 1,
        //		"scene_id": 1
//                "imgUrl":null,
//                        "offset":null,
//                        "limit":null

        private String name;
        private String device_id;
        private int user_id;
        private String device_no;
        private String deviceName;
        private String deviceType;
        private String mobilePhone;
        private String lonType;
        private String longitude;
        private String latType;
        private String latitude;
        private long lastActive;
        private String powerLevel;
        private int posAlert;
        private String bs;
        private String fgcs;
        private int iot_node_status;
        private String status;
        private String gy;
        private String dy;
        private String xl;
        private int alertflag;
        private int scene_id;
        private String imgUrl;
        private String offset;
        private String limit;

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getDevice_no() {
            return device_no;
        }

        public void setDevice_no(String device_no) {
            this.device_no = device_no;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public String getLonType() {
            return lonType;
        }

        public void setLonType(String lonType) {
            this.lonType = lonType;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatType() {
            return latType;
        }

        public void setLatType(String latType) {
            this.latType = latType;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public long getLastActive() {
            return lastActive;
        }

        public void setLastActive(long lastActive) {
            this.lastActive = lastActive;
        }

        public String getPowerLevel() {
            return powerLevel;
        }

        public void setPowerLevel(String powerLevel) {
            this.powerLevel = powerLevel;
        }

        public int getPosAlert() {
            return posAlert;
        }

        public void setPosAlert(int posAlert) {
            this.posAlert = posAlert;
        }

        public String getBs() {
            return bs;
        }

        public void setBs(String bs) {
            this.bs = bs;
        }

        public String getFgcs() {
            return fgcs;
        }

        public void setFgcs(String fgcs) {
            this.fgcs = fgcs;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGy() {
            return gy;
        }

        public void setGy(String gy) {
            this.gy = gy;
        }

        public String getDy() {
            return dy;
        }

        public void setDy(String dy) {
            this.dy = dy;
        }

        public String getXl() {
            return xl;
        }

        public void setXl(String xl) {
            this.xl = xl;
        }

        public int getAlarmFlag() {
            return alertflag;
        }

        public void setAlarmFlag(int alarmFlag) {
            this.alertflag = alarmFlag;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getScene_id() {
            return scene_id;
        }

        public void setScene_id(int scene_id) {
            this.scene_id = scene_id;
        }

        public int getIot_node_status() {
            return iot_node_status;
        }

        public void setIot_node_status(int iot_node_status) {
            this.iot_node_status = iot_node_status;
        }
    }
}
