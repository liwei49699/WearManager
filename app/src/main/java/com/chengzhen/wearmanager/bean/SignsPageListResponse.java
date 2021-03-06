package com.chengzhen.wearmanager.bean;

import java.util.List;

public class SignsPageListResponse extends BaseResponse<SignsPageListResponse.DataBeanX>{


    /**
     * msg : 获取成功
     * code : 0
     * data : {"data":[{"id":33,"name":"体征设备1","device_id":"14F000029","user_id":1,"device_no":"AAL","deviceName":"嘎嘎","deviceType":"2","mobilePhone":"187","lonType":null,"longitude":null,"lon":null,"latType":null,"latitude":null,"lat":null,"lastActive":1585719105000,"powerLevel":null,"posAlert":0,"bs":null,"fgcs":null,"iot_node_status":17,"gy":null,"gy_time":null,"dy":null,"dy_time":null,"xl":null,"xl_time":null,"alertflag":0,"scene_id":1,"imgUrl":null,"address":null,"node_user_id":32,"kfxt":"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000","kfxt_time":1585719105000,"chxt":"9.6","chxt_time":1585718922000,"offset":null,"limit":null},{"id":32,"name":"体征设备","device_id":"A00027815","user_id":1,"device_no":"AAL","deviceName":"test12","deviceType":"2","mobilePhone":"18751996113","lonType":null,"longitude":null,"lon":null,"latType":null,"latitude":null,"lat":null,"lastActive":1585713563000,"powerLevel":null,"posAlert":0,"bs":null,"fgcs":null,"iot_node_status":17,"gy":"132","gy_time":1585713563000,"dy":"092","dy_time":1585713563000,"xl":"078","xl_time":1585713563000,"alertflag":0,"scene_id":1,"imgUrl":"http://127.0.0.1:8080/lpro-lgb/","address":null,"node_user_id":31,"kfxt":null,"kfxt_time":null,"chxt":null,"chxt_time":null,"offset":null,"limit":null},{"id":29,"name":"cestee11","device_id":"1233","user_id":1,"device_no":"3212","deviceName":"test12","deviceType":"2","mobilePhone":"18751996113","lonType":null,"longitude":null,"lon":null,"latType":null,"latitude":null,"lat":null,"lastActive":null,"powerLevel":null,"posAlert":0,"bs":null,"fgcs":null,"iot_node_status":null,"gy":null,"gy_time":null,"dy":null,"dy_time":null,"xl":null,"xl_time":null,"alertflag":0,"scene_id":1,"imgUrl":"http://127.0.0.1:8080/lpro-lgb/","address":null,"node_user_id":31,"kfxt":null,"kfxt_time":null,"chxt":null,"chxt_time":null,"offset":null,"limit":null},{"id":36,"name":"test12","device_id":"5555555","user_id":3,"device_no":"AAL","deviceName":"test12","deviceType":"2","mobilePhone":"18751996113","lonType":null,"longitude":null,"lon":null,"latType":null,"latitude":null,"lat":null,"lastActive":null,"powerLevel":null,"posAlert":0,"bs":null,"fgcs":null,"iot_node_status":null,"gy":null,"gy_time":null,"dy":null,"dy_time":null,"xl":null,"xl_time":null,"alertflag":0,"scene_id":1,"imgUrl":"http://127.0.0.1:8080/lpro-lgb/","address":null,"node_user_id":31,"kfxt":null,"kfxt_time":null,"chxt":null,"chxt_time":null,"offset":null,"limit":null}],"totalCount":4,"paged":1,"pageSize":20,"totalPage":1}
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
         * data : [{"id":33,"name":"体征设备1","device_id":"14F000029","user_id":1,"device_no":"AAL","deviceName":"嘎嘎","deviceType":"2","mobilePhone":"187","lonType":null,"longitude":null,"lon":null,"latType":null,"latitude":null,"lat":null,"lastActive":1585719105000,"powerLevel":null,"posAlert":0,"bs":null,"fgcs":null,"iot_node_status":17,"gy":null,"gy_time":null,"dy":null,"dy_time":null,"xl":null,"xl_time":null,"alertflag":0,"scene_id":1,"imgUrl":null,"address":null,"node_user_id":32,"kfxt":"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000","kfxt_time":1585719105000,"chxt":"9.6","chxt_time":1585718922000,"offset":null,"limit":null},{"id":32,"name":"体征设备","device_id":"A00027815","user_id":1,"device_no":"AAL","deviceName":"test12","deviceType":"2","mobilePhone":"18751996113","lonType":null,"longitude":null,"lon":null,"latType":null,"latitude":null,"lat":null,"lastActive":1585713563000,"powerLevel":null,"posAlert":0,"bs":null,"fgcs":null,"iot_node_status":17,"gy":"132","gy_time":1585713563000,"dy":"092","dy_time":1585713563000,"xl":"078","xl_time":1585713563000,"alertflag":0,"scene_id":1,"imgUrl":"http://127.0.0.1:8080/lpro-lgb/","address":null,"node_user_id":31,"kfxt":null,"kfxt_time":null,"chxt":null,"chxt_time":null,"offset":null,"limit":null},{"id":29,"name":"cestee11","device_id":"1233","user_id":1,"device_no":"3212","deviceName":"test12","deviceType":"2","mobilePhone":"18751996113","lonType":null,"longitude":null,"lon":null,"latType":null,"latitude":null,"lat":null,"lastActive":null,"powerLevel":null,"posAlert":0,"bs":null,"fgcs":null,"iot_node_status":null,"gy":null,"gy_time":null,"dy":null,"dy_time":null,"xl":null,"xl_time":null,"alertflag":0,"scene_id":1,"imgUrl":"http://127.0.0.1:8080/lpro-lgb/","address":null,"node_user_id":31,"kfxt":null,"kfxt_time":null,"chxt":null,"chxt_time":null,"offset":null,"limit":null},{"id":36,"name":"test12","device_id":"5555555","user_id":3,"device_no":"AAL","deviceName":"test12","deviceType":"2","mobilePhone":"18751996113","lonType":null,"longitude":null,"lon":null,"latType":null,"latitude":null,"lat":null,"lastActive":null,"powerLevel":null,"posAlert":0,"bs":null,"fgcs":null,"iot_node_status":null,"gy":null,"gy_time":null,"dy":null,"dy_time":null,"xl":null,"xl_time":null,"alertflag":0,"scene_id":1,"imgUrl":"http://127.0.0.1:8080/lpro-lgb/","address":null,"node_user_id":31,"kfxt":null,"kfxt_time":null,"chxt":null,"chxt_time":null,"offset":null,"limit":null}]
         * totalCount : 4
         * paged : 1
         * pageSize : 20
         * totalPage : 1
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

        public static class DataBean {
            /**
             * id : 33
             * name : 体征设备1
             * device_id : 14F000029
             * user_id : 1
             * device_no : AAL
             * deviceName : 嘎嘎
             * deviceType : 2
             * mobilePhone : 187
             * lonType : null
             * longitude : null
             * lon : null
             * latType : null
             * latitude : null
             * lat : null
             * lastActive : 1585719105000
             * powerLevel : null
             * posAlert : 0
             * bs : null
             * fgcs : null
             * iot_node_status : 17
             * gy : null
             * gy_time : null
             * dy : null
             * dy_time : null
             * xl : null
             * xl_time : null
             * alertflag : 0
             * scene_id : 1
             * imgUrl : null
             * address : null
             * node_user_id : 32
             * kfxt :             
             * kfxt_time : 1585719105000
             * chxt : 9.6
             * chxt_time : 1585718922000
             * offset : null
             * limit : null
             */

            private int id;
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
            private Object powerLevel;
            private int posAlert;
            private Object bs;
            private Object fgcs;
            private int iot_node_status;
            private Object gy;
            private long gy_time;
            private Object dy;
            private long dy_time;
            private Object xl;
            private long xl_time;
            private int alertflag;
            private int scene_id;
            private Object imgUrl;
            private Object address;
            private int node_user_id;
            private String kfxt;
            private long kfxt_time;
            private String chxt;
            private long chxt_time;
            private Object offset;
            private Object limit;

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

            public Object getPowerLevel() {
                return powerLevel;
            }

            public void setPowerLevel(Object powerLevel) {
                this.powerLevel = powerLevel;
            }

            public int getPosAlert() {
                return posAlert;
            }

            public void setPosAlert(int posAlert) {
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

            public long getGy_time() {
                return gy_time;
            }

            public void setGy_time(long gy_time) {
                this.gy_time = gy_time;
            }

            public Object getDy() {
                return dy;
            }

            public void setDy(Object dy) {
                this.dy = dy;
            }

            public long getDy_time() {
                return dy_time;
            }

            public void setDy_time(long dy_time) {
                this.dy_time = dy_time;
            }

            public Object getXl() {
                return xl;
            }

            public void setXl(Object xl) {
                this.xl = xl;
            }

            public long getXl_time() {
                return xl_time;
            }

            public void setXl_time(long xl_time) {
                this.xl_time = xl_time;
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

            public Object getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(Object imgUrl) {
                this.imgUrl = imgUrl;
            }

            public Object getAddress() {
                return address;
            }

            public void setAddress(Object address) {
                this.address = address;
            }

            public int getNode_user_id() {
                return node_user_id;
            }

            public void setNode_user_id(int node_user_id) {
                this.node_user_id = node_user_id;
            }

            public String getKfxt() {
                return kfxt;
            }

            public void setKfxt(String kfxt) {
                this.kfxt = kfxt;
            }

            public long getKfxt_time() {
                return kfxt_time;
            }

            public void setKfxt_time(long kfxt_time) {
                this.kfxt_time = kfxt_time;
            }

            public String getChxt() {
                return chxt;
            }

            public void setChxt(String chxt) {
                this.chxt = chxt;
            }

            public long getChxt_time() {
                return chxt_time;
            }

            public void setChxt_time(long chxt_time) {
                this.chxt_time = chxt_time;
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
}
