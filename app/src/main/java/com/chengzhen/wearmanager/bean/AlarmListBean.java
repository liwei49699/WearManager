package com.chengzhen.wearmanager.bean;

import java.util.List;

public class AlarmListBean extends BaseResponse<AlarmListBean.DataBeanX>{

    /**
     * msg : 获取成功
     * code : 0
     * data : {"data":[{"name":"te报警","description":"te,传感器（干休所1号-test112-定位），当前定位为113.867535,22.567713 ,超出围栏范围，请及时处理。","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":2,"iot_trigger_alarm_level":40,"iot_alarm_process_status":47,"aid":null,"atime":1581565746000,"mid":null,"mtime":1581565883000,"user_id":null,"offset":null,"limit":null},{"name":"test报警","description":"test,传感器（干休所1号-test112-报警），当前为1.0 打开  等于1 打开，请及时处理。","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":4,"iot_trigger_alarm_level":39,"iot_alarm_process_status":47,"aid":null,"atime":1581563509000,"mid":null,"mtime":1581565894000,"user_id":null,"offset":null,"limit":null},{"name":"test报警","description":"test,传感器（干休所1号-test112-报警），当前为1.0 打开  等于1 打开，请及时处理。","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":4,"iot_trigger_alarm_level":39,"iot_alarm_process_status":47,"aid":null,"atime":1581563397000,"mid":null,"mtime":1581565894000,"user_id":null,"offset":null,"limit":null},{"name":"test报警","description":"test,传感器（干休所1号-test112-报警），当前为1.0 打开  等于1 打开，请及时处理。","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":4,"iot_trigger_alarm_level":39,"iot_alarm_process_status":47,"aid":null,"atime":1581562909000,"mid":null,"mtime":1581565894000,"user_id":null,"offset":null,"limit":null},{"name":"test报警","description":"test,传感器（干休所1号-test112-报警），当前为1.0 打开  等于1 打开，请及时处理。","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":4,"iot_trigger_alarm_level":39,"iot_alarm_process_status":47,"aid":null,"atime":1581562692000,"mid":null,"mtime":1581565894000,"user_id":null,"offset":null,"limit":null},{"name":"test报警","description":"test,传感器（干休所1号-test112-报警），当前为1.0 打开  等于1 打开，请及时处理。","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":4,"iot_trigger_alarm_level":39,"iot_alarm_process_status":47,"aid":null,"atime":1581519635000,"mid":null,"mtime":1581565890000,"user_id":null,"offset":null,"limit":null},{"name":"离线报警","description":"3G的设备已离线","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":null,"iot_trigger_alarm_level":41,"iot_alarm_process_status":47,"aid":null,"atime":1581476109000,"mid":null,"mtime":1581565894000,"user_id":null,"offset":null,"limit":null},{"name":"测试电量报警","description":"测试电量传感器（干休所1号-test112-电量数据），当前为80.0  低于100 ，请及时处理。","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":1,"iot_trigger_alarm_level":41,"iot_alarm_process_status":47,"aid":null,"atime":1581392326000,"mid":null,"mtime":1581518454000,"user_id":null,"offset":null,"limit":null}],"totalCount":8,"paged":1,"pageSize":20,"totalPage":1}
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
         * data : [{"name":"te报警","description":"te,传感器（干休所1号-test112-定位），当前定位为113.867535,22.567713 ,超出围栏范围，请及时处理。","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":2,"iot_trigger_alarm_level":40,"iot_alarm_process_status":47,"aid":null,"atime":1581565746000,"mid":null,"mtime":1581565883000,"user_id":null,"offset":null,"limit":null},{"name":"test报警","description":"test,传感器（干休所1号-test112-报警），当前为1.0 打开  等于1 打开，请及时处理。","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":4,"iot_trigger_alarm_level":39,"iot_alarm_process_status":47,"aid":null,"atime":1581563509000,"mid":null,"mtime":1581565894000,"user_id":null,"offset":null,"limit":null},{"name":"test报警","description":"test,传感器（干休所1号-test112-报警），当前为1.0 打开  等于1 打开，请及时处理。","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":4,"iot_trigger_alarm_level":39,"iot_alarm_process_status":47,"aid":null,"atime":1581563397000,"mid":null,"mtime":1581565894000,"user_id":null,"offset":null,"limit":null},{"name":"test报警","description":"test,传感器（干休所1号-test112-报警），当前为1.0 打开  等于1 打开，请及时处理。","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":4,"iot_trigger_alarm_level":39,"iot_alarm_process_status":47,"aid":null,"atime":1581562909000,"mid":null,"mtime":1581565894000,"user_id":null,"offset":null,"limit":null},{"name":"test报警","description":"test,传感器（干休所1号-test112-报警），当前为1.0 打开  等于1 打开，请及时处理。","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":4,"iot_trigger_alarm_level":39,"iot_alarm_process_status":47,"aid":null,"atime":1581562692000,"mid":null,"mtime":1581565894000,"user_id":null,"offset":null,"limit":null},{"name":"test报警","description":"test,传感器（干休所1号-test112-报警），当前为1.0 打开  等于1 打开，请及时处理。","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":4,"iot_trigger_alarm_level":39,"iot_alarm_process_status":47,"aid":null,"atime":1581519635000,"mid":null,"mtime":1581565890000,"user_id":null,"offset":null,"limit":null},{"name":"离线报警","description":"3G的设备已离线","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":null,"iot_trigger_alarm_level":41,"iot_alarm_process_status":47,"aid":null,"atime":1581476109000,"mid":null,"mtime":1581565894000,"user_id":null,"offset":null,"limit":null},{"name":"测试电量报警","description":"测试电量传感器（干休所1号-test112-电量数据），当前为80.0  低于100 ，请及时处理。","deviceName":null,"nodeName":null,"type":null,"node_id":6,"sensor_id":1,"iot_trigger_alarm_level":41,"iot_alarm_process_status":47,"aid":null,"atime":1581392326000,"mid":null,"mtime":1581518454000,"user_id":null,"offset":null,"limit":null}]
         * totalCount : 8
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
             * name : te报警
             * description : te,传感器（干休所1号-test112-定位），当前定位为113.867535,22.567713 ,超出围栏范围，请及时处理。
             * deviceName : null
             * nodeName : null
             * type : null
             * node_id : 6
             * sensor_id : 2
             * iot_trigger_alarm_level : 40
             * iot_alarm_process_status : 47
             * aid : null
             * atime : 1581565746000
             * mid : null
             * mtime : 1581565883000
             * user_id : null
             * "id":9,
             * offset : null
             * limit : null
             * imgUrl 图片
             * posAlert 是否在电子围栏外面  1在外面   0在里面
             */

            private String name;
            private String description;
            private String deviceName;
            private Object nodeName;
            private int type;
            private int node_id;
            private int sensor_id;
            private int iot_trigger_alarm_level;
            private int iot_alarm_process_status;
            private Object aid;
            private long atime;
            private Object mid;
            private long mtime;
            private Object user_id;
            private long id;
            private Object offset;
            private Object limit;
            private String imgUrl;
            private int posAlert;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getDeviceName() {
                return deviceName;
            }

            public void setDeviceName(String deviceName) {
                this.deviceName = deviceName;
            }

            public Object getNodeName() {
                return nodeName;
            }

            public void setNodeName(Object nodeName) {
                this.nodeName = nodeName;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getNode_id() {
                return node_id;
            }

            public void setNode_id(int node_id) {
                this.node_id = node_id;
            }

            public int getSensor_id() {
                return sensor_id;
            }

            public void setSensor_id(int sensor_id) {
                this.sensor_id = sensor_id;
            }

            public int getIot_trigger_alarm_level() {
                return iot_trigger_alarm_level;
            }

            public void setIot_trigger_alarm_level(int iot_trigger_alarm_level) {
                this.iot_trigger_alarm_level = iot_trigger_alarm_level;
            }

            public int getIot_alarm_process_status() {
                return iot_alarm_process_status;
            }

            public void setIot_alarm_process_status(int iot_alarm_process_status) {
                this.iot_alarm_process_status = iot_alarm_process_status;
            }

            public Object getAid() {
                return aid;
            }

            public void setAid(Object aid) {
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

            public long getMtime() {
                return mtime;
            }

            public void setMtime(long mtime) {
                this.mtime = mtime;
            }

            public Object getUser_id() {
                return user_id;
            }

            public void setUser_id(Object user_id) {
                this.user_id = user_id;
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

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public int getPosAlert() {
                return posAlert;
            }

            public void setPosAlert(int posAlert) {
                this.posAlert = posAlert;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }
        }
    }
}
