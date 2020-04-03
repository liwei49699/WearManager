package com.chengzhen.wearmanager.bean;

import java.util.List;

public class BloodPressureListResponse extends BaseResponse<BloodPressureListResponse.DataBeanX>{

    /**
     * msg : 获取成功
     * code : 0
     * data : {"data":[{"sdata1":"092","sdata":"132","atime":1413087480000,"device_id":null,"device_no":null,"iot_sensor_type":null,"sub_type":null,"offset":null,"limit":null},{"sdata1":"092","sdata":"132","atime":1413087480000,"device_id":null,"device_no":null,"iot_sensor_type":null,"sub_type":null,"offset":null,"limit":null}],"totalCount":2,"paged":1,"pageSize":5,"totalPage":1}
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
         * data : [{"sdata1":"092","sdata":"132","atime":1413087480000,"device_id":null,"device_no":null,"iot_sensor_type":null,"sub_type":null,"offset":null,"limit":null},{"sdata1":"092","sdata":"132","atime":1413087480000,"device_id":null,"device_no":null,"iot_sensor_type":null,"sub_type":null,"offset":null,"limit":null}]
         * totalCount : 2
         * paged : 1
         * pageSize : 5
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
             * sdata1 : 092
             * sdata : 132
             * atime : 1413087480000
             * device_id : null
             * device_no : null
             * iot_sensor_type : null
             * sub_type : null
             * offset : null
             * limit : null
             */

            private String sdata1;
            private String sdata;
            private long atime;
            private Object device_id;
            private Object device_no;
            private Object iot_sensor_type;
            private Object sub_type;
            private Object offset;
            private Object limit;

            public String getSdata1() {
                return sdata1;
            }

            public void setSdata1(String sdata1) {
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
