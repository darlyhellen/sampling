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

    private String name;

    private String type;

    private String deep;

    private String proj;

    private String note;

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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeep() {
        return deep;
    }

    public void setDeep(String deep) {
        this.deep = deep;
    }

    public String getProj() {
        return proj;
    }

    public void setProj(String proj) {
        this.proj = proj;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return id.concat("=id").concat(",longitude=").concat(String.valueOf(longitude)).concat(",latitude=").concat(String.valueOf(latitude)).concat(",desc=").concat(desc).concat(",samply=").concat(String.valueOf(samply).concat(",userSee=").concat(String.valueOf(userSee)));
    }
}
