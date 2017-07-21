package com.xiangxun.sampling.db;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/21.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:现场采样页面生成的数据库，表，通过这张表进行大数据上传操作
 */
@Table(name = "S_SENCE_SAMPLING")
public class SenceSamplingSugar extends SugarRecord {

    //点位id
    @Column(name = "pointId")
    private String pointId;
    //方案id
    @Column(name = "schemeId")
    private String schemeId;
    //位置名称
    @Column(name = "address")
    private String address;
    //经度
    @Column(name = "longitude")
    private String longitude;
    //纬度
    @Column(name = "latitude")
    private String latitude;

    //采样类型
    @Column(name = "type")
    private String type;
    //样品名称
    @Column(name = "name")
    private String name;
    //样品深度
    @Column(name = "params")
    private String params;
    //待测项目
    @Column(name = "project")
    private String project;
    //说明
    @Column(name = "other")
    private String other;
    @Column(name = "images")
    private List<String> images;
    @Column(name = "videos")
    private List<String> videos;


    public SenceSamplingSugar() {
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }
}
