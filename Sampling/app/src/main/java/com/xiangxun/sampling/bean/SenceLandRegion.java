package com.xiangxun.sampling.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/21.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:
 */
public class SenceLandRegion {

    public int resCode;
    public String resDesc;
    public List<LandRegion> result;

    public class LandRegion implements Serializable {
        public String id;
        public String pid;
        public String sort;
        public String code;
        public String remark;
        public String name;
    }


}
