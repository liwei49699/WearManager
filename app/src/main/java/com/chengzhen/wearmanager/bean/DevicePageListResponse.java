package com.chengzhen.wearmanager.bean;

import java.io.Serializable;
import java.util.List;

public class DevicePageListResponse extends BaseResponse<DevicePageListResponse.DataBeanX>{


    /**
     * msg : 获取成功
     * code : 0
     * data : {"data":[{"name":"zhenjiang1-4","device_id":"1703328863","user_id":6,"device_no":"3G","deviceName":"李四","deviceType":"1","mobilePhone":"13444444444","lonType":"E","longitude":"118.89293750026594","lon":"113.890249","latType":"N","latitude":"32.091399538547854","lat":"22.586239","lastActive":1582005335000,"powerLevel":"50","posAlert":null,"bs":"0","fgcs":"0","iot_node_status":16,"gy":null,"dy":null,"xl":null,"alertflag":1,"scene_id":1,"imgUrl":null,"offset":null,"limit":null},{"name":"zhenjiang1-5","device_id":"1703328917","user_id":6,"device_no":"3G","deviceName":"王五","deviceType":"1","mobilePhone":"13555555555","lonType":"E","longitude":"118.8920729989301","lon":"118.8870433","latType":"N","latitude":"32.09118409821006","lat":"32.093407","lastActive":1579663955000,"powerLevel":"4","posAlert":null,"bs":"4","fgcs":"0","iot_node_status":16,"gy":null,"dy":null,"xl":null,"alertflag":1,"scene_id":1,"imgUrl":null,"offset":null,"limit":null},{"name":"zhenjiang1-1","device_id":"1703317362","user_id":6,"device_no":"3G","deviceName":"刘一","deviceType":"1","mobilePhone":"13111111111","lonType":"E","longitude":"118.89209969810098","lon":"118.8870700","latType":"N","latitude":"32.09135704586863","lat":"32.093580","lastActive":1579663910000,"powerLevel":"3","posAlert":null,"bs":"0","fgcs":"0","iot_node_status":16,"gy":null,"dy":null,"xl":null,"alertflag":1,"scene_id":1,"imgUrl":null,"offset":null,"limit":null},{"name":"zhenjiang1-3","device_id":"1703328887","user_id":6,"device_no":"3G","deviceName":"张三","deviceType":"1","mobilePhone":"13333333333","lonType":"E","longitude":"118.89193477622145","lon":"118.8869050","latType":"N","latitude":"32.0910872034354","lat":"32.093310","lastActive":1579657334000,"powerLevel":"63","posAlert":null,"bs":"0","fgcs":"0","iot_node_status":16,"gy":null,"dy":null,"xl":null,"alertflag":1,"scene_id":1,"imgUrl":null,"offset":null,"limit":null}],"totalCount":6,"paged":1,"pageSize":4,"totalPage":2}
     */

//    private String msg;
//    private int code;
//    private DataBeanX data;
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
//    public DataBeanX getData() {
//        return data;
//    }
//
//    public void setData(DataBeanX data) {
//        this.data = data;
//    }

    public static class DataBeanX {
        /**
         * data : [{"name":"zhenjiang1-4","device_id":"1703328863","user_id":6,"device_no":"3G","deviceName":"李四","deviceType":"1","mobilePhone":"13444444444","lonType":"E","longitude":"118.89293750026594","lon":"113.890249","latType":"N","latitude":"32.091399538547854","lat":"22.586239","lastActive":1582005335000,"powerLevel":"50","posAlert":null,"bs":"0","fgcs":"0","iot_node_status":16,"gy":null,"dy":null,"xl":null,"alertflag":1,"scene_id":1,"imgUrl":null,"offset":null,"limit":null},{"name":"zhenjiang1-5","device_id":"1703328917","user_id":6,"device_no":"3G","deviceName":"王五","deviceType":"1","mobilePhone":"13555555555","lonType":"E","longitude":"118.8920729989301","lon":"118.8870433","latType":"N","latitude":"32.09118409821006","lat":"32.093407","lastActive":1579663955000,"powerLevel":"4","posAlert":null,"bs":"4","fgcs":"0","iot_node_status":16,"gy":null,"dy":null,"xl":null,"alertflag":1,"scene_id":1,"imgUrl":null,"offset":null,"limit":null},{"name":"zhenjiang1-1","device_id":"1703317362","user_id":6,"device_no":"3G","deviceName":"刘一","deviceType":"1","mobilePhone":"13111111111","lonType":"E","longitude":"118.89209969810098","lon":"118.8870700","latType":"N","latitude":"32.09135704586863","lat":"32.093580","lastActive":1579663910000,"powerLevel":"3","posAlert":null,"bs":"0","fgcs":"0","iot_node_status":16,"gy":null,"dy":null,"xl":null,"alertflag":1,"scene_id":1,"imgUrl":null,"offset":null,"limit":null},{"name":"zhenjiang1-3","device_id":"1703328887","user_id":6,"device_no":"3G","deviceName":"张三","deviceType":"1","mobilePhone":"13333333333","lonType":"E","longitude":"118.89193477622145","lon":"118.8869050","latType":"N","latitude":"32.0910872034354","lat":"32.093310","lastActive":1579657334000,"powerLevel":"63","posAlert":null,"bs":"0","fgcs":"0","iot_node_status":16,"gy":null,"dy":null,"xl":null,"alertflag":1,"scene_id":1,"imgUrl":null,"offset":null,"limit":null}]
         * totalCount : 6
         * paged : 1
         * pageSize : 4
         * totalPage : 2
         */

        private int totalCount;
        private int paged;
        private int pageSize;
        private int totalPage;
        private List<DataBean> data;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getPaged() {
            return paged;
        }

        public void setPaged(int paged) {
            this.paged = paged;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Serializable {

            private static final long serialVersionUID = -5541875293843939192L;
            /**
             * name : zhenjiang1-4
             * device_id : 1703328863
             * user_id : 6
             * device_no : 3G
             * deviceName : 李四
             * deviceType : 1
             * mobilePhone : 13444444444
             * lonType : E
             * longitude : 118.89293750026594
             * lon : 113.890249
             * latType : N
             * latitude : 32.091399538547854
             * lat : 22.586239
             * lastActive : 1582005335000
             * powerLevel : 50
             * posAlert : null
             * bs : 0
             * fgcs : 0
             * iot_node_status : 16
             * gy : null
             * dy : null
             * xl : null
             * alertflag : 1
             * scene_id : 1
             * imgUrl : null
             * offset : null
             * limit : null
             * 设备信息新增两个字段  imgUrl：头像地址，address：定位地址
             */

            private String name;
            private String device_id;
            private int user_id;
            private String device_no;
            private String deviceName;
            private String deviceType;
            private String mobilePhone;
            private String lonType;
            private String longitude;
            private String lon;
            private String latType;
            private String latitude;
            private String lat;
            private long lastActive;
            private String powerLevel;
            private int posAlert;
            private String bs;
            private String fgcs;
            private int iot_node_status;
            private Object gy;
            private Object dy;
            private Object xl;
            private int alertflag = 1;
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

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
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

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
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

            public int getIot_node_status() {
                return iot_node_status;
            }

            public void setIot_node_status(int iot_node_status) {
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

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }
    }
}
