package com.xiangxun.sampling.bean;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/21.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:
 */
public class HisExceptionInfo {

    public int resCode;
    public String resDesc;

    public List<HisException> result;
    public String regionId;


    public class HisException {
        public String id = "";
        public String createTime = "";
        public String landBlockId = "";
        public String landBlockName = "";
        public String errorTime = "";
        public String createId = "";
        public String describe = "";
    }
}
