package com.chengzhen.wearmanager.bean;

public class AlarmSetBean extends BaseResponse<AlarmSetBean.DataBean> {


    /**
     * msg : 获取成功
     * code : 0
     * data : {"id":null,"name":null,"password":null,"phone":null,"real_name":null,"avatar":null,"address":null,"newPassword":null,"dlbj":0,"dwbj":0,"hjbj":0}
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
         * id : null
         * name : null
         * password : null
         * phone : null
         * real_name : null
         * avatar : null
         * address : null
         * newPassword : null
         * dlbj : 0
         * dwbj : 0
         * hjbj : 0
         */

        private Object id;
        private Object name;
        private Object password;
        private Object phone;
        private Object real_name;
        private Object avatar;
        private Object address;
        private Object newPassword;
        private int dlbj;
        private int dwbj;
        private int hjbj;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getPassword() {
            return password;
        }

        public void setPassword(Object password) {
            this.password = password;
        }

        public Object getPhone() {
            return phone;
        }

        public void setPhone(Object phone) {
            this.phone = phone;
        }

        public Object getReal_name() {
            return real_name;
        }

        public void setReal_name(Object real_name) {
            this.real_name = real_name;
        }

        public Object getAvatar() {
            return avatar;
        }

        public void setAvatar(Object avatar) {
            this.avatar = avatar;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public Object getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(Object newPassword) {
            this.newPassword = newPassword;
        }

        public int getDlbj() {
            return dlbj;
        }

        public void setDlbj(int dlbj) {
            this.dlbj = dlbj;
        }

        public int getDwbj() {
            return dwbj;
        }

        public void setDwbj(int dwbj) {
            this.dwbj = dwbj;
        }

        public int getHjbj() {
            return hjbj;
        }

        public void setHjbj(int hjbj) {
            this.hjbj = hjbj;
        }
    }
}
