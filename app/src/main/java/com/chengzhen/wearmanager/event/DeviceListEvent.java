package com.chengzhen.wearmanager.event;

import com.chengzhen.wearmanager.bean.DevicePageListResponse;

import java.util.List;

public class DeviceListEvent {

    public boolean success;
    public boolean refresh;
    public List<DevicePageListResponse.DataBeanX.DataBean> data;
}
