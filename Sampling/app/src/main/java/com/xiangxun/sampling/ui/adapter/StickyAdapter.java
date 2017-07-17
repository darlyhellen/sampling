package com.xiangxun.sampling.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.ParentAdapter;
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.bean.SamplingPlanning;
import com.xiangxun.sampling.ui.main.ChaoTuActivity;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:展示计划组的适配器
 */
public class StickyAdapter extends ParentAdapter<Scheme> implements StickyListHeadersAdapter {
    //是否現場採集 true 為現場
    private boolean isSence;

    public StickyAdapter(List<Scheme> data, int resID, Context context, boolean isSence) {
        super(data, resID, context);
        this.isSence = isSence;
    }

    @Override
    public View HockView(int position, View view, ViewGroup parent, int resID, final Context context, final Scheme s) {
        ViewHocker hocker = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resID, parent, false);
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
            hocker.name.setText(s.name);
            hocker.name.setTextColor(context.getResources().getColor(R.color.black));
            hocker.name.setTextSize(14);
            hocker.dept.setText(s.dept);
            hocker.dept.setTextColor(context.getResources().getColor(R.color.black));
            hocker.dept.setTextSize(14);
            hocker.position.setText(String.valueOf(s.quantity).concat("个"));
            hocker.position.setTextColor(context.getResources().getColor(R.color.black));
            hocker.position.setTextSize(14);
            hocker.desc.setText("点击查看范围");
            hocker.desc.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            hocker.desc.getPaint().setAntiAlias(true);//抗锯齿
            if (s.isUserSee()) {
                hocker.desc.setTextColor(context.getResources().getColor(R.color.gray));
            } else {
                hocker.desc.setTextColor(context.getResources().getColor(R.color.blue));
            }
            hocker.desc.setTextSize(14);
            hocker.desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s.setUserSee(true);
                    Intent intent = new Intent(context, ChaoTuActivity.class);
                    intent.putExtra("SamplingPlanning", s);
                    context.startActivity(intent);
                    notifyDataSetChanged();
                }
            });
        } else {
            hocker.bg.setBackgroundColor(context.getResources().getColor(R.color.white));
            hocker.name.setText(s.name);
            hocker.name.setTextColor(context.getResources().getColor(R.color.black));
            hocker.name.setTextSize(14);
            hocker.dept.setText(s.sampleCode);
            hocker.dept.setTextColor(context.getResources().getColor(R.color.black));
            hocker.dept.setTextSize(14);
            hocker.position.setText(s.regionName);
            hocker.position.setTextColor(context.getResources().getColor(R.color.black));
            hocker.position.setTextSize(14);
            hocker.desc.setText("点击查看");
            hocker.desc.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            hocker.desc.getPaint().setAntiAlias(true);//抗锯齿
            if (s.isUserSee()) {
                hocker.desc.setTextColor(context.getResources().getColor(R.color.gray));
            } else {
                hocker.desc.setTextColor(context.getResources().getColor(R.color.blue));
            }
            hocker.desc.setTextSize(14);
            hocker.desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s.setUserSee(true);
                    Intent intent = new Intent(context, ChaoTuActivity.class);
                    intent.putExtra("SamplingPlanning", s);
                    context.startActivity(intent);
                    notifyDataSetChanged();
                }
            });
        }
        return view;
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        ViewHocker hocker = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resID, viewGroup, false);
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
            hocker.name.setText("计划名称");
            hocker.name.setTextColor(context.getResources().getColor(R.color.white));
            hocker.name.setTextSize(16);
            hocker.dept.setText("采样样品");
            hocker.dept.setTextColor(context.getResources().getColor(R.color.white));
            hocker.dept.setTextSize(16);
            hocker.position.setText("采样点位数");
            hocker.position.setTextColor(context.getResources().getColor(R.color.white));
            hocker.position.setTextSize(16);
            hocker.desc.setText("");
            hocker.desc.setTextColor(context.getResources().getColor(R.color.white));
            hocker.desc.setTextSize(16);
        } else {
            hocker.bg.setBackgroundResource(R.mipmap.ic_set_user_info);
            hocker.name.setText("计划名称");
            hocker.name.setTextColor(context.getResources().getColor(R.color.white));
            hocker.name.setTextSize(16);
            hocker.dept.setText("采样样品");
            hocker.dept.setTextColor(context.getResources().getColor(R.color.white));
            hocker.dept.setTextSize(16);
            hocker.position.setText("采样选址");
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
