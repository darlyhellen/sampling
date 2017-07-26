package com.xiangxun.sampling.common.retrofit.paramer;

import java.util.List;
import java.util.Map;

/**
 * Created by Zhangyuhui/Darly on 2017/7/26.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 异常地块上传服务端参数集合
 */
public class ExceptionPageParamer {

    private String latitude;

    private String longitude;

    private String landid;

    private String declare;

    private String images;

    public ExceptionPageParamer() {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLandid() {
        return landid;
    }

    public void setLandid(String landid) {
        this.landid = landid;
    }

    public String getDeclare() {
        return declare;
    }

    public void setDeclare(String declare) {
        this.declare = declare;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
