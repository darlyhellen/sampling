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

    public class ResultPointData implements Serializable {

        public String resDesc;

        public int resCode;
        //时间戳
        public String resTime;

        public List<Pointly> result;

    }

    public class Pointly implements Serializable {
        public String id;
        public Point point;
    }

    public class Point implements Serializable {
        //点位id
        public String id;
        //方案id
        public String schemeId;
        //编号
        public String code;
        //经度
        public String longitude;
        //纬度
        public String latitude;
        //采样范围X
        public String rangeX;
        //采样范围Y
        public String rangeY;
        //所属区域编号
        public String areaCode;
        //是否采样点
        public Integer isSamplingPoint;
        //核查状态
        public Integer checkStatus;
        //是否发布
        public Integer isRelease;
        //是否采样
        public Integer isSampling;
        //创建人
        public String createId;
        //创建时间
        public String createTime;
        //修改人
        public String updateId;
        //修改时间
        public String updateTime;
        //地块ID
        public String blockId;
        //采样选址
        public String regionId;

        //是否已经采样
        private boolean samply;

        //是否查看过
        private boolean userSee;

        public boolean isUserSee() {
            return userSee;
        }

        public void setUserSee(boolean userSee) {
            this.userSee = userSee;
        }

        public boolean isSamply() {
            return samply;
        }

        public void setSamply(boolean samply) {
            this.samply = samply;
        }

    }
}
