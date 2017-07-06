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
    private List<SamplingPoint> points;

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

    public List<SamplingPoint> getPoints() {
        return points;
    }

    public void setPoints(List<SamplingPoint> points) {
        this.points = points;
    }
}
