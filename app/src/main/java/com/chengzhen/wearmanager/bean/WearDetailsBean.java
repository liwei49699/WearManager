package com.chengzhen.wearmanager.bean;

public class WearDetailsBean extends BaseResponse<WearDetailsBean.DataBean> {


    /**
     * msg : 获取成功
     * code : 0
     * data : {"name":"设备4","device_id":"444444444","user_id":3,"device_no":"0444444444","deviceName":"测试4","deviceType":"类型4","mobilePhone":"18514725836","lonType":null,"longitude":null,"lon":null,"latType":null,"latitude":null,"lat":null,"lastActive":null,"powerLevel":null,"posAlert":null,"bs":null,"fgcs":null,"iot_node_status":null,"gy":null,"dy":null,"xl":null,"alertflag":0,"scene_id":1,"imgUrl":null,"address":null,"offset":null,"limit":null}
     */

//    private String msg;
//    private int code;
//    private DataBean data;
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
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }

    public static class DataBean {
        /**
         * name : 设备4
         * device_id : 444444444
         * user_id : 3
         * device_no : 0444444444
         * deviceName : 测试4
         * deviceType : 类型4
         * mobilePhone : 18514725836
         * lonType : null
         * longitude : null
         * lon : null
         * latType : null
         * latitude : null
         * lat : null
         * lastActive : null
         * powerLevel : null
         * posAlert : null
         * bs : null
         * fgcs : null
         * iot_node_status : null
         * gy : null
         * dy : null
         * xl : null
         * alertflag : 0
         * scene_id : 1
         * imgUrl : null
         * address : null
         * offset : null
         * limit : null
         */

        private String name;
        private String device_id;
        private int user_id;
        private String device_no;
        private String deviceName;
        private String deviceType;
        private String mobilePhone;
        private Object lonType;
        private Object longitude;
        private Object lon;
        private Object latType;
        private Object latitude;
        private Object lat;
        private long lastActive;
        private String powerLevel;
        private Object posAlert;
        private Object bs;
        private Object fgcs;
        private Object iot_node_status;
        private Object gy;
        private Object dy;
        private Object xl;
        private int alertflag;
        private int scene_id;
        private String imgUrl;
        private String address;
        private Object offset;
        private Object limit;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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

        public Object getLonType() {
            return lonType;
        }

        public void setLonType(Object lonType) {
            this.lonType = lonType;
        }

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(Object longitude) {
            this.longitude = longitude;
        }

        public Object getLon() {
            return lon;
        }

        public void setLon(Object lon) {
            this.lon = lon;
        }

        public Object getLatType() {
            return latType;
        }

        public void setLatType(Object latType) {
            this.latType = latType;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(Object latitude) {
            this.latitude = latitude;
        }

        public Object getLat() {
            return lat;
        }

        public void setLat(Object lat) {
            this.lat = lat;
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

        public Object getPosAlert() {
            return posAlert;
        }

        public void setPosAlert(Object posAlert) {
            this.posAlert = posAlert;
        }

        public Object getBs() {
            return bs;
        }

        public void setBs(Object bs) {
            this.bs = bs;
        }

        public Object getFgcs() {
            return fgcs;
        }

        public void setFgcs(Object fgcs) {
            this.fgcs = fgcs;
        }

        public Object getIot_node_status() {
            return iot_node_status;
        }

        public void setIot_node_status(Object iot_node_status) {
            this.iot_node_status = iot_node_status;
        }

        public Object getGy() {
            return gy;
        }

        public void setGy(Object gy) {
            this.gy = gy;
        }

        public Object getDy() {
            return dy;
        }

        public void setDy(Object dy) {
            this.dy = dy;
        }

        public Object getXl() {
            return xl;
        }

        public void setXl(Object xl) {
            this.xl = xl;
        }

        public int getAlertflag() {
            return alertflag;
        }

        public void setAlertflag(int alertflag) {
            this.alertflag = alertflag;
        }

        public int getScene_id() {
            return scene_id;
        }

        public void setScene_id(int scene_id) {
            this.scene_id = scene_id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getOffset() {
            return offset;
        }

        public void setOffset(Object offset) {
            this.offset = offset;
        }

        public Object getLimit() {
            return limit;
        }

        public void setLimit(Object limit) {
            this.limit = limit;
        }
    }
}
