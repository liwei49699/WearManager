package com.chengzhen.wearmanager.bean;

import java.util.List;

public class HeartRateCharResopnse extends BaseResponse<HeartRateCharResopnse.DataBean>{

    /**
     * msg : 获取成功
     * code : 0
     * data : {"standard":{"gy_standard":"140","dy_standard":"90","xl_standard":"160","chxt_standard":"11.1","kfxt_standard":"7","tw_standard":"37","xy_standard":"95"},"xl":[{"sdata1":null,"sdata":"078","atime":1413087480000,"device_id":null,"device_no":null,"iot_sensor_type":null,"sub_type":null,"offset":null,"limit":null},{"sdata1":null,"sdata":"078","atime":1413087480000,"device_id":null,"device_no":null,"iot_sensor_type":null,"sub_type":null,"offset":null,"limit":null}]}
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
         * standard : {"gy_standard":"140","dy_standard":"90","xl_standard":"160","chxt_standard":"11.1","kfxt_standard":"7","tw_standard":"37","xy_standard":"95"}
         * xl : [{"sdata1":null,"sdata":"078","atime":1413087480000,"device_id":null,"device_no":null,"iot_sensor_type":null,"sub_type":null,"offset":null,"limit":null},{"sdata1":null,"sdata":"078","atime":1413087480000,"device_id":null,"device_no":null,"iot_sensor_type":null,"sub_type":null,"offset":null,"limit":null}]
         */

        private StandardBean standard;
        private List<XlBean> xl;

        public StandardBean getStandard() {
            return standard;
        }

        public void setStandard(StandardBean standard) {
            this.standard = standard;
        }

        public List<XlBean> getXl() {
            return xl;
        }

        public void setXl(List<XlBean> xl) {
            this.xl = xl;
        }

        public static class StandardBean {
            /**
             * gy_standard : 140
             * dy_standard : 90
             * xl_standard : 160
             * chxt_standard : 11.1
             * kfxt_standard : 7
             * tw_standard : 37
             * xy_standard : 95
             */

            private String gy_standard;
            private String dy_standard;
            private String xl_standard;
            private String chxt_standard;
            private String kfxt_standard;
            private String tw_standard;
            private String xy_standard;

            public String getGy_standard() {
                return gy_standard;
            }

            public void setGy_standard(String gy_standard) {
                this.gy_standard = gy_standard;
            }

            public String getDy_standard() {
                return dy_standard;
            }

            public void setDy_standard(String dy_standard) {
                this.dy_standard = dy_standard;
            }

            public String getXl_standard() {
                return xl_standard;
            }

            public void setXl_standard(String xl_standard) {
                this.xl_standard = xl_standard;
            }

            public String getChxt_standard() {
                return chxt_standard;
            }

            public void setChxt_standard(String chxt_standard) {
                this.chxt_standard = chxt_standard;
            }

            public String getKfxt_standard() {
                return kfxt_standard;
            }

            public void setKfxt_standard(String kfxt_standard) {
                this.kfxt_standard = kfxt_standard;
            }

            public String getTw_standard() {
                return tw_standard;
            }

            public void setTw_standard(String tw_standard) {
                this.tw_standard = tw_standard;
            }

            public String getXy_standard() {
                return xy_standard;
            }

            public void setXy_standard(String xy_standard) {
                this.xy_standard = xy_standard;
            }
        }

        public static class XlBean {
            /**
             * sdata1 : null
             * sdata : 078
             * atime : 1413087480000
             * device_id : null
             * device_no : null
             * iot_sensor_type : null
             * sub_type : null
             * offset : null
             * limit : null
             */

            private Object sdata1;
            private String sdata;
            private long atime;
            private Object device_id;
            private Object device_no;
            private Object iot_sensor_type;
            private Object sub_type;
            private Object offset;
            private Object limit;

            public Object getSdata1() {
                return sdata1;
            }

            public void setSdata1(Object sdata1) {
                this.sdata1 = sdata1;
            }

            public String getSdata() {
                return sdata;
            }

            public void setSdata(String sdata) {
                this.sdata = sdata;
            }

            public long getAtime() {
                return atime;
            }

            public void setAtime(long atime) {
                this.atime = atime;
            }

            public Object getDevice_id() {
                return device_id;
            }

            public void setDevice_id(Object device_id) {
                this.device_id = device_id;
            }

            public Object getDevice_no() {
                return device_no;
            }

            public void setDevice_no(Object device_no) {
                this.device_no = device_no;
            }

            public Object getIot_sensor_type() {
                return iot_sensor_type;
            }

            public void setIot_sensor_type(Object iot_sensor_type) {
                this.iot_sensor_type = iot_sensor_type;
            }

            public Object getSub_type() {
                return sub_type;
            }

            public void setSub_type(Object sub_type) {
                this.sub_type = sub_type;
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
