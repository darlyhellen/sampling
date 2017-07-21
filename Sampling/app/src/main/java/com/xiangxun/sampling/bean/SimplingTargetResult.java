package com.xiangxun.sampling.bean;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/21.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:
 */
public class SimplingTargetResult {
    public TargetData result;
    public int resCode;
    public String resDesc;

    public class TargetData {
        public int avaCount;
        public int pageSize;
        public int totalPageCount;
        public int totalSize;
        public int start;
        public int currentPageNo;
        public List<SimplingTarget> data;
    }
}
