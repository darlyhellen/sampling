package com.xiangxun.sampling.bean;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/25.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:
 */
public class SenceInfo {

    public int resCode;
    public String resDesc;
    public List<SenceObj> result;

    public class SenceObj {
        public String missionId;
        public String samplingCode;
        public String id;
        public String createTime;
        public String checkStatus;
        public String status;
        public String name;
        public String longitude;
        public String latitude;
        public String code;
        public String depth;
        public String createId;
        public String regionName;
    }
}
