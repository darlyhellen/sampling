package com.xiangxun.sampling.bean;

import java.io.Serializable;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 采样的点坐标
 */
public class SamplingKey implements Serializable {

    private String id;
    private SamplingPoint point;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SamplingPoint getPoint() {
        return point;
    }

    public void setPoint(SamplingPoint point) {
        this.point = point;
    }
}
