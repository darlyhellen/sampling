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
import com.xiangxun.sampling.bean.HisPlanningData;
import com.xiangxun.sampling.bean.PlannningData.Pointly;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.ui.biz.SamplingPointListener;
import com.xiangxun.sampling.widget.dialog.MsgDialog;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:历史点位展示
 */
public class HistoryPointAdapter extends ParentAdapter<HisPlanningData.HisPoint> implements StickyListHeadersAdapter {


    public HistoryPointAdapter(List<HisPlanningData.HisPoint> data, int resID, Context context) {
        super(data, resID, context);
    }

    @Override
    public View HockView(int position, View view, ViewGroup parent, int resID, final Context context, final HisPlanningData.HisPoint s) {
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

        //未采样进行展示 ,已采集不进行展示
        view.setVisibility(View.VISIBLE);
        hocker.bg.setBackgroundColor(context.getResources().getColor(R.color.white));
        hocker.name.setText(s.code);
        hocker.name.setTextColor(context.getResources().getColor(R.color.gray));
        hocker.name.setTextSize(14);
        hocker.dept.setText(String.valueOf(s.SampleName));
        hocker.dept.setTextColor(context.getResources().getColor(R.color.gray));
        hocker.dept.setTextSize(14);
        hocker.position.setText(String.valueOf(s.samplingSource));
        hocker.position.setTextColor(context.getResources().getColor(R.color.gray));
        hocker.position.setTextSize(14);
        hocker.iv.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        ViewHocker hocker = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_planning_header, null);
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
        hocker.name.setText("采样编号");
        hocker.name.setTextColor(context.getResources().getColor(R.color.white));
        hocker.name.setTextSize(16);
        hocker.dept.setText("采样类型");
        hocker.dept.setTextColor(context.getResources().getColor(R.color.white));
        hocker.dept.setTextSize(16);
        hocker.position.setText("采样来源");
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
