package com.chengzhen.wearmanager.bean;

public class LoginResponse {

    public int error; //	错误代码 0 无错误，非0为错误
    public String errorcode; //	错误消息
    public Data data;

    public static class Data{

        public String Account;//	账号
        public String RealName;//	单位名称
        public String Avatar;	//	单位头像
        public String MobilePhone;//	移动手机
        public String Address;	//	地址
        public String token;	//	令牌
    }
}
