package com.xiangxun.sampling.bean;

import java.io.Serializable;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 采样的点坐标
 */
public class SamplingPoint implements Serializable {

    private String id;
    private float longitude;
    private float latitude;
    private String desc;
    private boolean samply;
    private boolean userSee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isSamply() {
        return samply;
    }

    public void setSamply(boolean samply) {
        this.samply = samply;
    }

    public boolean isUserSee() {
        return userSee;
    }

    public void setUserSee(boolean userSee) {
        this.userSee = userSee;
    }

    @Override
    public String toString() {
        return id.concat("=id").concat(",longitude=").concat(String.valueOf(longitude)).concat(",latitude=").concat(String.valueOf(latitude)).concat(",desc=").concat(desc).concat(",samply=").concat(String.valueOf(samply).concat(",userSee=").concat(String.valueOf(userSee)));
    }
}
