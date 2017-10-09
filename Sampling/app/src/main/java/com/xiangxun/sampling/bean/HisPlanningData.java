package com.xiangxun.sampling.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Darly on 2017/9/28.
 */
public class HisPlanningData {

    public class HisPointData implements Serializable {
        public String resDesc;
        public int resCode;
        public List<HisPoint> result;
    }

    public class HisPoint implements Serializable {
        //点位id
        public String id;
        public String missionId;
        //编号
        public String code;
        public String SampleName;
        public String tableName;
        //採樣來源
        public String samplingSource;
        //经度
        public double longitude;
        //纬度
        public double latitude;
    }
}
