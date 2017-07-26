package com.xiangxun.sampling.db;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;
import com.xiangxun.sampling.bean.SenceLandRegion;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/21.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:现场采样页面生成的数据库，表，通过这张表进行大数据上传操作
 */
@Table(name = "S_SENCE_SAMPLING")
public class SenceSamplingSugar extends SugarRecord implements Serializable {

    //上传成功后获取的现场采样id
    @Column(name = "samplingId")
    private String samplingId;
    //点位id
    @Column(name = "pointId")
    private String pointId;
    //方案id
    @Column(name = "schemeId")
    private String schemeId;
    //任务ID
    @Column(name = "missionId")
    private String missionId;
    //位置名称
    @Column(name = "region_id")
    private String region_id;
    //经度
    @Column(name = "longitude")
    private String longitude;
    //纬度
    @Column(name = "latitude")
    private String latitude;
    //样品类型编码
    @Column(name = "soil_type")
    private String soil_type;
    //样品类型名称
    @Column(name = "soil_name")
    private String soil_name;
    //样品名称
    @Column(name = "name")
    private String name;
    //样品深度
    @Column(name = "depth")
    private String depth;
    //采样类型
    @Column(name = "test_item")
    private String test_item;

    //图片信息
    @Column(name = "images")
    private List<String> images;
    //视频信息
    @Column(name = "videos")
    private List<String> videos;


    private SenceLandRegion.LandRegion result;


    public SenceSamplingSugar() {
    }

    public String getSamplingId() {
        return samplingId;
    }

    public void setSamplingId(String samplingId) {
        this.samplingId = samplingId;
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

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
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

    public String getSoil_type() {
        return soil_type;
    }

    public void setSoil_type(String soil_type) {
        this.soil_type = soil_type;
    }


    public String getSoil_name() {
        return soil_name;
    }

    public void setSoil_name(String soil_name) {
        this.soil_name = soil_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getTest_item() {
        return test_item;
    }

    public void setTest_item(String test_item) {
        this.test_item = test_item;
    }

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
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


    public SenceLandRegion.LandRegion getResult() {
        return result;
    }

    public void setResult(SenceLandRegion.LandRegion result) {
        this.result = result;
    }
}
