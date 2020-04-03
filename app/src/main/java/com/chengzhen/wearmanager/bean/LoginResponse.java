package com.chengzhen.wearmanager.bean;

public class LoginResponse {

    /**
     * msg : 登录成功
     * code : 0
     * data : {"userInfo":{"id":3,"name":"lisi","password":null,"phone":"18751996110","real_name":"2231","avatar":null,"address":null},"token":"NWNkM2QzZWY5NWM3NGM1NmExYjJlOTE3NmM1MzQ4OTQ="}
     */

    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userInfo : {"id":3,"name":"lisi","password":null,"phone":"18751996110","real_name":"2231","avatar":null,"address":null}
         * token : NWNkM2QzZWY5NWM3NGM1NmExYjJlOTE3NmM1MzQ4OTQ=
         */

        private UserInfoBean userInfo;
        private String token;

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public static class UserInfoBean {
            /**
             * id : 3
             * name : lisi
             * password : null
             * phone : 18751996110
             * real_name : 2231
             * avatar : null
             * address : null
             */

            private int id;
            private String name;
            private String password;
            private String phone;
            private String real_name;
            private String avatar;
            private String address;

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

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getReal_name() {
                return real_name;
            }

            public void setReal_name(String real_name) {
                this.real_name = real_name;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
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
