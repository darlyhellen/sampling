package com.xiangxun.sampling.common.retrofit.paramer;

/**
 * Created by Zhangyuhui/Darly on 2017/7/20.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 现场采样参数上传。
 */
public class SenceParamer {
    //点位id
    public String id;
    //方案id
    public String schemeId;
    //位置名称
    public String address;
    //经度
    public String longitude;
    //纬度
    public String latitude;

    //采样类型
    public String type;
    //样品名称
    public String name;
    //样品深度
    public String params;
    //待测项目
    public String project;
    //说明
    public String other;

    public SenceParamer(String id, String schemeId, String address, String longitude, String latitude, String type, String name, String params, String project, String other) {
        this.id = id;
        this.schemeId = schemeId;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
        this.name = name;
        this.params = params;
        this.project = project;
        this.other = other;
    }
}
