package com.xiangxun.sampling.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.ParentAdapter;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.bean.Index;

import java.util.List;

/**
 * Author:Created by zhangyh2 on 2016/8/31 at 10:24.
 * Copyright (c) 2016 Organization Rich-Healthcare(D.L.) zhangyh2 All rights reserved.
 * TODO:首页菜单的适配器
 */

public class GridViewAdapter extends ParentAdapter<Index> {
    public GridViewAdapter(List<Index> data, int resID, Context context) {
        super(data, resID, context);
    }

    @Override
    public View HockView(int position, View view, ViewGroup parent, int resID, Context context, Index inte) {
        ViewHocker hocker = null;
        if (view == null) {
            hocker = new ViewHocker();
            view = LayoutInflater.from(context).inflate(resID, null);
            hocker.relative = (RelativeLayout) view.findViewById(R.id.grid_relative);
            hocker.relative.setLayoutParams(new AbsListView.LayoutParams(SystemCfg.getWidth(context) / 3, SystemCfg.getWidth(context) / 3));
            hocker.icon = (ImageView) view.findViewById(R.id.grid_round_image);
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(SystemCfg.getWidth(context) / 6, SystemCfg.getWidth(context) / 6);
            rl.addRule(RelativeLayout.CENTER_IN_PARENT);
            hocker.icon.setLayoutParams(rl);
            hocker.name = (TextView) view.findViewById(R.id.grid_descrip);
            view.setTag(hocker);
        } else {
            hocker = (ViewHocker) view.getTag();
        }

        hocker.icon.setImageResource(inte.getRes());
        hocker.name.setText(inte.getName());
        return view;
    }

    class ViewHocker {
        RelativeLayout relative;
        ImageView icon;
        TextView name;
    }
}
