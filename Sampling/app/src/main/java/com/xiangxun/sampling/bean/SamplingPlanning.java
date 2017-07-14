package com.xiangxun.sampling.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:采样计划
 */
public class SamplingPlanning implements Serializable {

    private String id;
    private String title;
    private String depate;
    private String place;
    private String type;
    private String samplingexzample;
    private List<SamplingKey> points;

    private boolean userSee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepate() {
        return depate;
    }

    public void setDepate(String depate) {
        this.depate = depate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSamplingexzample() {
        return samplingexzample;
    }

    public void setSamplingexzample(String samplingexzample) {
        this.samplingexzample = samplingexzample;
    }

    public List<SamplingKey> getPoints() {
        return points;
    }

    public void setPoints(List<SamplingKey> points) {
        this.points = points;
    }

    public boolean isUserSee() {
        return userSee;
    }

    public void setUserSee(boolean userSee) {
        this.userSee = userSee;
    }
}
