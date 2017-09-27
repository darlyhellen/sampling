package com.xiangxun.sampling.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Darly on 2017/9/25.
 */
public class SamplingSenceGroup {
    public class SamplingSence implements Serializable {
        public List<SenceGroup> result;//一级菜单
        public String resDesc;
        public int resCode;
        //时间戳
        public String resTime;
        public String regionId;
    }
    public class SenceGroup implements Serializable {
        public int shemeNum;
        public String regType;
        public List<PlannningData.Scheme> result;//二级菜单
    }
}
