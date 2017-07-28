package com.xiangxun.sampling.bean;

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
        public String missionId = "";
        public String createTime = "";
        public String checkStatus = "";
        public String status = "";
        public String name = "";
        public String longitude = "";
        public String latitude = "";
        public String code = "";
        public String depth = "";
        public String createId = "";
        public String regionName = "";
        public String blockName = "";
        public String sampleName = "";
        public List<FileList> file;
    }

    public class FileList {
        public String fileName;
        public String filePath;
        public String businessType;

    }
}
