package com.xiangxun.sampling.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/17.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 方案计划的Java类
 */
public class PlannningData {

    public class ResultData implements Serializable {
        public List<Scheme> result;

        public String resDesc;

        public int resCode;
        //时间戳
        public String resTime;

    }

    public class Scheme implements Serializable {
        public String id;
        //计划ID
        public String planId;
        //地块ID
        public String blockId;
        //采样样品
        public String sampleCode;
        //方案编号
        public String code;
        //方案名称
        public String name;
        //采样选址
        public String regionId;
        //选址名称
        public String regionName;
        //制定单位
        public String dept;
        //创建人
        public String createId;
        //创建时间
        public String createTime;
        //
        public int status;
        //点位个数
        public int quantity;
        //是否查看过
        private boolean userSee;

        public boolean isUserSee() {
            return userSee;
        }

        public void setUserSee(boolean userSee) {
            this.userSee = userSee;
        }
    }
}
