package com.xiangxun.sampling.bean;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/21.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:
 */
public class HisExceptionPageInfo {

    public int resCode;
    public String resDesc;

    public HisExceptionPage result;


    public class HisExceptionPage {
        public String id = "";
        public String createTime = "";
        public String landBlockId = "";
        public String regionName = "";
        public String landBlockName = "";
        public String errorTime = "";
        public String createId = "";
        public String describe = "";
        public String longitude = "";
        public String latitude = "";
        public List<FileList> fileList;
    }

    public class FileList {
        public String fileName;
        public String filePath;
        public String businessType;

    }
}
