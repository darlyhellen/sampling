package com.xiangxun.sampling.bean;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/21.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 地块信息
 */
public class GroundTypeInfo {

    public int resCode;
    public String resDesc;

    public List<Ground> result;


    public class Ground {
        public String id;
        public String createTime;
        public String soilType;
        public String area;
        public String name;
        public String typeCode;
        public String code;
        public String createId;
        public String regionId;
        public double longitude;
        public double latitude;
        public int isError;
    }
}
