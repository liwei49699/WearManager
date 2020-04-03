package com.chengzhen.wearmanager.event;

public class AddDeviceEvent {

    public boolean success;
    public int type; //手表{1增加 2 修改 } 3体征设备增加
    public String name;
    public int position;
}
