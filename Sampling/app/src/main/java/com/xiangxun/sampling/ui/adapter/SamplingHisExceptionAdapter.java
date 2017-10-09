package com.xiangxun.sampling.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.ParentAdapter;
import com.xiangxun.sampling.bean.HisExceptionInfo.HisException;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Zhangyuhui/Darly on 2017/7/10.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:地块异常列表
 */
public class SamplingHisExceptionAdapter extends ParentAdapter<HisException> implements StickyListHeadersAdapter {


    public SamplingHisExceptionAdapter(List<HisException> data, int resID, Context context) {
        super(data, resID, context);
    }

    @Override
    public View HockView(final int position, View view, ViewGroup parent, int resID, Context context, HisException info) {
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
        hocker.name.setText(info.landBlockName);
        hocker.name.setTextColor(context.getResources().getColor(R.color.gray));
        hocker.name.setTextSize(14);
        hocker.dept.setText(String.valueOf(info.errorTime));
        hocker.dept.setTextColor(context.getResources().getColor(R.color.gray));
        hocker.dept.setTextSize(14);
        hocker.position.setText(String.valueOf(info.describe));
        hocker.position.setTextColor(context.getResources().getColor(R.color.gray));
        hocker.position.setTextSize(14);
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
        hocker.name.setText("地块名称");
        hocker.name.setTextColor(context.getResources().getColor(R.color.white));
        hocker.name.setTextSize(16);
        hocker.dept.setText("异常时间");
        hocker.dept.setTextColor(context.getResources().getColor(R.color.white));
        hocker.dept.setTextSize(16);
        hocker.position.setText("说明");
        hocker.position.setTextColor(context.getResources().getColor(R.color.white));
        hocker.position.setTextSize(16);
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
