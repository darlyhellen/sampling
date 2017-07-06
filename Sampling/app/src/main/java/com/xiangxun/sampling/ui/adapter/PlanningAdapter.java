package com.xiangxun.sampling.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xiangxun.sampling.base.ParentAdapter;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:
 */
public class PlanningAdapter extends ParentAdapter<String> {

    public PlanningAdapter(List<String> data, int resID, Context context) {
        super(data, resID, context);
    }

    @Override
    public View HockView(int position, View view, ViewGroup parent, int resID, Context context, String s) {
        return null;
    }
}
