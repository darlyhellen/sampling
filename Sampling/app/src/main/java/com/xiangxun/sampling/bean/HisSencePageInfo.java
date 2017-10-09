package com.xiangxun.sampling.bean;

import com.orm.dsl.Column;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/21.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:历史采样页面展示
 */
public class HisSencePageInfo {

    public int resCode;
    public String resDesc;
    public HisSencePage result;


    public class HisSencePage {
        public String id = "";
        public String createTime = "";
        public String checkStatus = "";
        public String status = "";
        public String createId = "";
        public String regionName = "";
        public String blockName = "";
        public String soilName = "";
        public String sampleName = "";

        //上传成功后获取的现场采样id
        public String samplingId;
        //上传返回的唯一编码
        public String code;
        //点位id
        public String pointId;
        //方案id
        public String schemeId;
        //任务ID
        public String missionId;
        //样品类型编码
        public String soil_type;
        //样品类型名称
        public String soil_name;
        //样品名称
        public String name;
        //样品深度
        public String depth;
        //采样类型
        public String test_item;
        //進行其他參數補充。
        //背景土壤
        public String ambient;//周围环境
        public String years;//成墙年份
        public String wallSource;//墙土来源下拉选
        public String typeCode;//样品类型下拉选
        //农作物
        public String position;//采样部位
        //样品名称
        public String samplingType;//已选择的采样类型编号
        public String samplingCode;//服务端返回的采样类型编号
        public String otherType;//其他选项选择的编号
        //农田土壤
        //采样深度
        //样品名称
        //(下拉选)样品类型
        //水采样
        //(下拉选)类型
        //样品类型
        public String riversName;//河流名称
        //大气
        public String containerVolume;//容器体积
        public String collectVolume;//收集量
        //肥料
        public String shopName;//店名
        public String shopkeeper;//店主
        public String tel;//联系方式
        public String dealManure;//经营肥料

        //位置名称
        public String region_id;
        //经度
        public String longitude;
        //纬度
        public String latitude;
        //图片信息
        public List<String> images;
        //视频信息
        public List<String> videos;
        public String typesamplyName;
        public String typesamplyCode;
        public String othersamplyName;
        public String othersamplyCode;


        public List<FileList> file;
    }

    public class FileList {
        public String fileName;
        public String filePath;
        public String businessType;

    }
}
