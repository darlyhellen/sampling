package com.xiangxun.sampling.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.ParentAdapter;
import com.xiangxun.sampling.bean.SamplingPlanning;
import com.xiangxun.sampling.bean.SamplingPoint;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:展现点位信息的适配器
 */
public class PointAdapter extends ParentAdapter<SamplingPoint> {

    public PointAdapter(List<SamplingPoint> data, int resID, Context context) {
        super(data, resID, context);
    }

    @Override
    public View HockView(int position, View view, ViewGroup parent, int resID, Context context, SamplingPoint s) {
        ViewHocker hocker = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resID, null);
            hocker = new ViewHocker();
            hocker.name = (TextView) view.findViewById(R.id.id_item_planning_name);
            hocker.dept = (TextView) view.findViewById(R.id.id_item_planning_dept);
            hocker.position = (TextView) view.findViewById(R.id.id_item_planning_place);
            hocker.desc = (TextView) view.findViewById(R.id.id_item_planning_other);
            hocker.bg = (LinearLayout) view.findViewById(R.id.id_item_planning_linear);
            view.setTag(hocker);
        } else {
            hocker = (ViewHocker) view.getTag();
        }

        if (position == 0) {
            hocker.bg.setBackgroundResource(R.mipmap.ic_set_user_info);
            hocker.name.setText("点位编号");
            hocker.name.setTextColor(context.getResources().getColor(R.color.white));
            hocker.name.setTextSize(16);
            hocker.dept.setText("经度");
            hocker.dept.setTextColor(context.getResources().getColor(R.color.white));
            hocker.dept.setTextSize(16);
            hocker.position.setText("纬度");
            hocker.position.setTextColor(context.getResources().getColor(R.color.white));
            hocker.position.setTextSize(16);
            hocker.desc.setText("");
            hocker.desc.setTextColor(context.getResources().getColor(R.color.white));
            hocker.desc.setTextSize(16);
        } else {
            hocker.bg.setBackgroundColor(context.getResources().getColor(R.color.white));
            hocker.name.setText(s.getId());
            hocker.name.setTextColor(context.getResources().getColor(R.color.black));
            hocker.name.setTextSize(14);
            hocker.dept.setText(String.valueOf(s.getLatitude()));
            hocker.dept.setTextColor(context.getResources().getColor(R.color.black));
            hocker.dept.setTextSize(14);
            hocker.position.setText(String.valueOf(s.getLongitude()));
            hocker.position.setTextColor(context.getResources().getColor(R.color.black));
            hocker.position.setTextSize(14);
            hocker.desc.setText("点击修改");
            hocker.desc.setTextColor(context.getResources().getColor(R.color.black));
            hocker.desc.setTextSize(14);
        }
        return view;
    }

    class ViewHocker {
        LinearLayout bg;
        TextView name;
        TextView dept;
        TextView position;
        TextView desc;
    }
}
