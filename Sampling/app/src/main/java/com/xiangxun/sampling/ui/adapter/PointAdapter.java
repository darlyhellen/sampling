package com.xiangxun.sampling.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.ParentAdapter;
import com.xiangxun.sampling.bean.PlannningData.Pointly;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:展现点位信息的适配器
 */
public class PointAdapter extends ParentAdapter<Pointly> implements StickyListHeadersAdapter {

    private boolean isSence;

    public PointAdapter(List<Pointly> data, int resID, Context context, boolean isSence) {
        super(data, resID, context);
        this.isSence = isSence;
    }

    @Override
    public View HockView(int position, View view, ViewGroup parent, int resID, final Context context, final Pointly s) {
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

        if (isSence) {
            hocker.bg.setBackgroundColor(context.getResources().getColor(R.color.white));
            hocker.name.setText(s.point.id);
            hocker.name.setTextColor(context.getResources().getColor(R.color.black));
            hocker.name.setTextSize(14);
            hocker.dept.setText(String.valueOf(s.point.latitude));
            hocker.dept.setTextColor(context.getResources().getColor(R.color.black));
            hocker.dept.setTextSize(14);
            hocker.position.setText(String.valueOf(s.point.longitude));
            hocker.position.setTextColor(context.getResources().getColor(R.color.black));
            hocker.position.setTextSize(14);
            hocker.desc.setText("采样记录");
            hocker.desc.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            hocker.desc.getPaint().setAntiAlias(true);//抗锯齿
            if (s.point.isUserSee()) {
                hocker.desc.setTextColor(context.getResources().getColor(R.color.gray));
            } else {
                hocker.desc.setTextColor(context.getResources().getColor(R.color.blue));
            }
            hocker.desc.setTextSize(14);
        } else {
            hocker.bg.setBackgroundColor(context.getResources().getColor(R.color.white));
            hocker.name.setText(s.point.id);
            hocker.name.setTextColor(context.getResources().getColor(R.color.black));
            hocker.name.setTextSize(14);
            hocker.dept.setText(String.valueOf(s.point.latitude));
            hocker.dept.setTextColor(context.getResources().getColor(R.color.black));
            hocker.dept.setTextSize(14);
            hocker.position.setText(String.valueOf(s.point.longitude));
            hocker.position.setTextColor(context.getResources().getColor(R.color.black));
            hocker.position.setTextSize(14);
            hocker.desc.setText("点击修改");
            hocker.desc.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            hocker.desc.getPaint().setAntiAlias(true);//抗锯齿
            if (s.point.isUserSee()) {
                hocker.desc.setTextColor(context.getResources().getColor(R.color.gray));
            } else {
                hocker.desc.setTextColor(context.getResources().getColor(R.color.blue));
            }
            hocker.desc.setTextSize(14);
        }
        return view;
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
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

        if (isSence) {
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
        }
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
    }
}
