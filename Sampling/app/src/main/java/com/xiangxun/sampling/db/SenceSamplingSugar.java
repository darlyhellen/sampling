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
    //上传返回的唯一编码
    @Column(name = "code")
    private String code;
    //点位id
    @Column(name = "pointId")
    private String pointId;
    //方案id
    @Column(name = "schemeId")
    private String schemeId;
    //任务ID
    @Column(name = "missionId")
    private String missionId;
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
    //進行其他參數補充。
    //背景土壤
    @Column(name = "ambient")
    private String ambient;//周围环境
    @Column(name = "years")
    private String years;//成墙年份
    @Column(name = "wallSource")
    private String wallSource;//墙土来源下拉选
    @Column(name = "typeCode")
    private String typeCode;//样品类型下拉选
    //农作物
    @Column(name = "position")
    private String position;//采样部位
    //样品名称
    @Column(name = "samplingType")
    private String samplingType;//已选择的采样类型编号
    @Column(name = "samplingCode")
    private String samplingCode;//服务端返回的采样类型编号
    private String otherType;//其他选项选择的编号
    //农田土壤
    //采样深度
    //样品名称
    //(下拉选)样品类型
    //水采样
    //(下拉选)类型
    //样品类型
    @Column(name = "riversName")
    private String riversName;//河流名称
    //大气
    @Column(name = "containerVolume")
    private String containerVolume;//容器体积
    @Column(name = "collectVolume")
    private String collectVolume;//收集量

    //肥料
    @Column(name = "shopName")
    private String shopName;//店名
    @Column(name = "shopkeeper")
    private String shopkeeper;//店主
    @Column(name = "tel")
    private String tel;//联系方式
    @Column(name = "dealManure")
    private String dealManure;//经营肥料

    //位置名称
    @Column(name = "region_id")
    private String region_id;
    //经度
    @Column(name = "longitude")
    private String longitude;
    //纬度
    @Column(name = "latitude")
    private String latitude;
    //图片信息
    private List<String> images;
    //视频信息
    private List<String> videos;

    @Column(name = "typesamplyName")
    private String typesamplyName;
    @Column(name = "typesamplyCode")
    private String typesamplyCode;
    @Column(name = "othersamplyName")
    private String othersamplyName;
    @Column(name = "othersamplyCode")
    private String othersamplyCode;
    public SenceSamplingSugar() {
    }

    public String getSamplingId() {
        return samplingId;
    }

    public void setSamplingId(String samplingId) {
        this.samplingId = samplingId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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


    public String getAmbient() {
        return ambient;
    }

    public void setAmbient(String ambient) {
        this.ambient = ambient;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getWallSource() {
        return wallSource;
    }

    public void setWallSource(String wallSource) {
        this.wallSource = wallSource;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSamplingType() {
        return samplingType;
    }

    public String getSamplingCode() {
        return samplingCode;
    }

    public void setSamplingCode(String samplingCode) {
        this.samplingCode = samplingCode;
    }

    public void setSamplingType(String samplingType) {
        this.samplingType = samplingType;
    }

    public String getOtherType() {
        return otherType;
    }

    public void setOtherType(String otherType) {
        this.otherType = otherType;
    }

    public String getRiversName() {
        return riversName;
    }

    public void setRiversName(String riversName) {
        this.riversName = riversName;
    }

    public String getContainerVolume() {
        return containerVolume;
    }

    public void setContainerVolume(String containerVolume) {
        this.containerVolume = containerVolume;
    }

    public String getCollectVolume() {
        return collectVolume;
    }

    public void setCollectVolume(String collectVolume) {
        this.collectVolume = collectVolume;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopkeeper() {
        return shopkeeper;
    }

    public void setShopkeeper(String shopkeeper) {
        this.shopkeeper = shopkeeper;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDealManure() {
        return dealManure;
    }

    public void setDealManure(String dealManure) {
        this.dealManure = dealManure;
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


    public String getTypesamplyName() {
        return typesamplyName;
    }

    public void setTypesamplyName(String typesamplyName) {
        this.typesamplyName = typesamplyName;
    }

    public String getTypesamplyCode() {
        return typesamplyCode;
    }

    public void setTypesamplyCode(String typesamplyCode) {
        this.typesamplyCode = typesamplyCode;
    }

    public String getOthersamplyName() {
        return othersamplyName;
    }

    public void setOthersamplyName(String othersamplyName) {
        this.othersamplyName = othersamplyName;
    }

    public String getOthersamplyCode() {
        return othersamplyCode;
    }

    public void setOthersamplyCode(String othersamplyCode) {
        this.othersamplyCode = othersamplyCode;
    }

    public SenceLandRegion.LandRegion getTypesamply() {
        SenceLandRegion.LandRegion region = new SenceLandRegion().new LandRegion();
        region.code = getTypesamplyCode();
        region.name = getTypesamplyName();
        return region;
    }

    public void setTypesamply(SenceLandRegion.LandRegion typesamply) {
        setTypesamplyCode(typesamply.code);
        setTypesamplyName(typesamply.name);
    }

    public SenceLandRegion.LandRegion getOthersamply() {
        SenceLandRegion.LandRegion region = new SenceLandRegion().new LandRegion();
        region.code = getOthersamplyCode();
        region.name = getOthersamplyName();
        return region;
    }

    public void setOthersamply(SenceLandRegion.LandRegion othersamply) {
        setOthersamplyCode(othersamply.code);
        setOthersamplyName(othersamply.name);
    }
}
