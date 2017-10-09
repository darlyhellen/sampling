package com.xiangxun.sampling.ui.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.ParentAdapter;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.bean.SimplingTarget;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Zhangyuhui/Darly on 2017/7/10.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:现场采集页面，适配器效果，存放图片和视频文件。
 */
public class SamplingTargetAdapter extends ParentAdapter<SimplingTarget> implements StickyListHeadersAdapter {


    public SamplingTargetAdapter(List<SimplingTarget> data, int resID, Context context) {
        super(data, resID, context);
    }

    @Override
    public View HockView(final int position, View view, ViewGroup parent, int resID, Context context, SimplingTarget info) {
        ViewHocker hocker = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resID, null);
            hocker = new ViewHocker();
            hocker.name = (TextView) view.findViewById(R.id.id_item_planning_name);
            hocker.dept = (TextView) view.findViewById(R.id.id_item_planning_dept);
            hocker.position = (TextView) view.findViewById(R.id.id_item_planning_place);
            hocker.iv = (ImageView) view.findViewById(R.id.id_item_planning_other);
            hocker.bg = (LinearLayout) view.findViewById(R.id.id_item_planning_linear);
            view.setTag(hocker);
        } else {
            hocker = (ViewHocker) view.getTag();
        }
        hocker.bg.setBackgroundColor(context.getResources().getColor(R.color.white));
        hocker.name.setText(info.type_name);
        hocker.name.setTextColor(context.getResources().getColor(R.color.gray));
        hocker.name.setTextSize(14);
        hocker.dept.setText(String.valueOf(info.analy_name + ":"));
        hocker.dept.setTextColor(context.getResources().getColor(R.color.gray));
        hocker.dept.setTextSize(14);
        hocker.position.setText(String.valueOf(info.analy_value));
        hocker.position.setTextColor(context.getResources().getColor(R.color.gray));
        hocker.position.setTextSize(14);
        switch (info.isOver) {
            case 0:
                hocker.iv.setImageResource(R.mipmap.key_end);
                break;
            default:
                hocker.iv.setImageResource(R.mipmap.key_clear);
                break;
        }
        return view;
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        ViewHocker hocker = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_planning_header, viewGroup, false);
            hocker = new ViewHocker();
            hocker.name = (TextView) view.findViewById(R.id.id_item_planning_name);
            hocker.dept = (TextView) view.findViewById(R.id.id_item_planning_dept);
            hocker.position = (TextView) view.findViewById(R.id.id_item_planning_place);
            hocker.desc = (TextView) view.findViewById(R.id.id_item_planning_tvs);
            hocker.bg = (LinearLayout) view.findViewById(R.id.id_item_planning_linear);
            view.setTag(hocker);
        } else {
            hocker = (ViewHocker) view.getTag();
        }
        hocker.bg.setBackgroundResource(R.mipmap.title_bg);
        hocker.name.setText("样品");
        hocker.name.setTextColor(context.getResources().getColor(R.color.white));
        hocker.name.setTextSize(16);
        hocker.dept.setText("指标");
        hocker.dept.setTextColor(context.getResources().getColor(R.color.white));
        hocker.dept.setTextSize(16);
        hocker.position.setText("值（mg/kg）");
        hocker.position.setTextColor(context.getResources().getColor(R.color.white));
        hocker.position.setTextSize(16);
        hocker.desc.setText("是否超标");
        hocker.desc.setTextColor(context.getResources().getColor(R.color.white));
        hocker.desc.setTextSize(16);
        return view;
    }

    @Override
    public long getHeaderId(int i) {
        return 0;
    }

    class ViewHocker {
        LinearLayout bg;
        TextView name;
        TextView dept;
        TextView position;
        TextView desc;
        ImageView iv;

    }
}
