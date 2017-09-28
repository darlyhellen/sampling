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
@Table(name = "S_SENCE_MEDIA")
public class MediaSugar extends SugarRecord implements Serializable {

    //上传成功后获取的现场采样id
    @Column(name = "samplingId")
    private String samplingId;
    @Column(name = "type")
    private String type;
    @Column(name = "samplingCode")
    private String samplingCode;
    //图片视频资源
    @Column(name = "url")
    private String url;

    public String getSamplingId() {
        return samplingId;
    }

    public void setSamplingId(String samplingId) {
        this.samplingId = samplingId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSamplingCode() {
        return samplingCode;
    }

    public void setSamplingCode(String samplingCode) {
        this.samplingCode = samplingCode;
    }
}
