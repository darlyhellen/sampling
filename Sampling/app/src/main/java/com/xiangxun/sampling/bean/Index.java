package com.xiangxun.sampling.bean;

/**
 * Created by Zhangyuhui/Darly on 2017/7/5.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:首页展示模型
 */
public class Index {

    private String name;

    private int res;

    public Index(String name, int res) {
        this.name = name;
        this.res = res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
