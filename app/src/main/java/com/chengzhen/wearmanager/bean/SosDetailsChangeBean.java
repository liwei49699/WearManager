package com.chengzhen.wearmanager.bean;

public class SosDetailsChangeBean extends BaseResponse<SosDetailsChangeBean.DataBean>{
    /**
     * msg : 获取成功
     * code : 0
     * data : {"device_id":"1703328863","device_no":"3G","phone":"1","phone1":null,"phone2":null,"status":0,"aid":3,"atime":1582547078000,"mid":null,"mtime":null,"user_id":null}
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
         * device_id : 1703328863
         * device_no : 3G
         * phone : 1
         * phone1 : null
         * phone2 : null
         * status : 0
         * aid : 3
         * atime : 1582547078000
         * mid : null
         * mtime : null
         * user_id : null
         */

        private String device_id;
        private String device_no;
        private String phone;
        private String phone1;
        private String phone2;
        private int status;
        private int aid;
        private long atime;
        private Object mid;
        private Object mtime;
        private Object user_id;

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getDevice_no() {
            return device_no;
        }

        public void setDevice_no(String device_no) {
            this.device_no = device_no;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhone1() {
            return phone1;
        }

        public void setPhone1(String phone1) {
            this.phone1 = phone1;
        }

        public String getPhone2() {
            return phone2;
        }

        public void setPhone2(String phone2) {
            this.phone2 = phone2;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public Object getMid() {
            return mid;
        }

        public void setMid(Object mid) {
            this.mid = mid;
        }

        public Object getMtime() {
            return mtime;
        }

        public void setMtime(Object mtime) {
            this.mtime = mtime;
        }

        public Object getUser_id() {
            return user_id;
        }

        public void setUser_id(Object user_id) {
            this.user_id = user_id;
        }
    }
}
