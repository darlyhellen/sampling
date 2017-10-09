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
import com.xiangxun.sampling.bean.PlannningData;
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
 * @TODO:展现点位信息的适配器
 */
public class PointAdapter extends ParentAdapter<Pointly> implements StickyListHeadersAdapter {

    private boolean isSence;

    //判断是哪个方案中的计划
    private PlannningData.Scheme planning;

    private MsgDialog msgDialog;

    private SamplingPointListener.SamplingPointInterface main;

    public PointAdapter(PlannningData.Scheme planning,List<Pointly> data, int resID, Context context, boolean isSence, SamplingPointListener.SamplingPointInterface main) {
        super(data, resID, context);
        this.planning = planning;
        this.main = main;
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
            hocker.iv = (ImageView) view.findViewById(R.id.id_item_planning_other);
            hocker.bg = (LinearLayout) view.findViewById(R.id.id_item_planning_linear);
            view.setTag(hocker);
        } else {
            hocker = (ViewHocker) view.getTag();
        }
        if (isSence) {
            if (s.data.isSampling == 0) {
                //未采样进行展示 ,已采集不进行展示
                view.setVisibility(View.VISIBLE);
                hocker.bg.setBackgroundColor(context.getResources().getColor(R.color.white));
                hocker.name.setText(s.data.code);
                hocker.name.setTextColor(context.getResources().getColor(R.color.gray));
                hocker.name.setTextSize(14);
                hocker.dept.setText(String.valueOf(s.data.longitude));
                hocker.dept.setTextColor(context.getResources().getColor(R.color.gray));
                hocker.dept.setTextSize(14);
                hocker.position.setText(String.valueOf(s.data.latitude));
                hocker.position.setTextColor(context.getResources().getColor(R.color.gray));
                hocker.position.setTextSize(14);
                final SenceSamplingSugar sugar = (SenceSamplingSugar) SharePreferHelp.getValue("sugar" + s.data.id);
                if (sugar != null) {
                    //这个点位已经有了草稿，可以进行提交
                    hocker.iv.setImageResource(R.mipmap.lighton);
                    hocker.iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //进行提示弹窗，询问用户是否确认修改上传状态。
                            msgDialog = new MsgDialog(context);
                            msgDialog.setTiele("是否确认上传状态信息？");
                            msgDialog.setButLeftListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    msgDialog.dismiss();
                                }
                            });
                            msgDialog.setButRightListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    msgDialog.dismiss();
                                    main.onItemImageClick(planning,sugar, s.data);
                                }
                            });
                            msgDialog.show();
                        }
                    });
                } else {
                    //这个点位还没有进行草稿保存。无法提交
                    hocker.iv.setImageResource(R.mipmap.lightoff);
                }
            } else {
                view.setVisibility(View.GONE);
            }
        } else {

            view.setVisibility(View.VISIBLE);
            hocker.bg.setBackgroundColor(context.getResources().getColor(R.color.white));
            hocker.name.setText(s.data.code);
            hocker.name.setTextColor(context.getResources().getColor(R.color.gray));
            hocker.name.setTextSize(14);
            hocker.dept.setText(String.valueOf(s.data.longitude));
            hocker.dept.setTextColor(context.getResources().getColor(R.color.gray));
            hocker.dept.setTextSize(14);
            hocker.position.setText(String.valueOf(s.data.latitude));
            hocker.position.setTextColor(context.getResources().getColor(R.color.gray));
            hocker.position.setTextSize(14);
            if (s.data.isSampling == 0&&!"DQ".equals(planning.sampleCode)) {
                hocker.iv.setImageResource(R.mipmap.ic_point_mark);
                hocker.iv.setVisibility(View.VISIBLE);
            } else {
                hocker.iv.setVisibility(View.INVISIBLE);
            }
        }
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

        if (isSence) {
            hocker.bg.setBackgroundResource(R.mipmap.title_bg);
            hocker.name.setText("点位编号");
            hocker.name.setTextColor(context.getResources().getColor(R.color.white));
            hocker.name.setTextSize(16);
            hocker.dept.setText("经度");
            hocker.dept.setTextColor(context.getResources().getColor(R.color.white));
            hocker.dept.setTextSize(16);
            hocker.position.setText("纬度");
            hocker.position.setTextColor(context.getResources().getColor(R.color.white));
            hocker.position.setTextSize(16);
        } else {
            hocker.bg.setBackgroundResource(R.mipmap.title_bg);
            hocker.name.setText("点位编号");
            hocker.name.setTextColor(context.getResources().getColor(R.color.white));
            hocker.name.setTextSize(16);
            hocker.dept.setText("经度");
            hocker.dept.setTextColor(context.getResources().getColor(R.color.white));
            hocker.dept.setTextSize(16);
            hocker.position.setText("纬度");
            hocker.position.setTextColor(context.getResources().getColor(R.color.white));
            hocker.position.setTextSize(16);
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
        ImageView iv;
    }
}
