package com.xiangxun.sampling.common.retrofit.paramer;

/**
 * Created by Zhangyuhui/Darly on 2017/7/18.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:
 */
public class AddPointParamer {
    private String schemeId;
    private String blockId;
    private String regionId;
    private String longitude;
    private String latitude;

    public AddPointParamer(String schemeId, String blockId, String regionId, String longitude, String latitude) {
        this.schemeId = schemeId;
        this.blockId = blockId;
        this.regionId = regionId;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
