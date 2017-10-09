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
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.service.ServicePresenter;
import com.xiangxun.sampling.ui.biz.SamplingDBListener;
import com.xiangxun.sampling.ui.biz.SamplingPointListener;
import com.xiangxun.sampling.widget.dialog.MsgDialog;
import com.xiangxun.sampling.widget.timeselecter.option.TextUtil;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:展现已采样数据适配器
 */
public class SamplingDBAdapter extends ParentAdapter<SenceSamplingSugar> implements StickyListHeadersAdapter {


    private MsgDialog msgDialog;

    private SamplingDBListener.SamplingDBInterface main;

    public SamplingDBAdapter(List<SenceSamplingSugar> data, int resID, Context context, SamplingDBListener.SamplingDBInterface main) {
        super(data, resID, context);
        this.main = main;
    }

    @Override
    public View HockView(int position, View view, ViewGroup parent, int resID, final Context context, final SenceSamplingSugar s) {
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
        hocker.bg.setBackgroundColor(context.getResources().getColor(R.color.white));
        hocker.name.setText(s.getCode());
        hocker.name.setTextColor(context.getResources().getColor(R.color.gray));
        hocker.name.setTextSize(14);
        hocker.dept.setTextColor(context.getResources().getColor(R.color.gray));
        hocker.dept.setTextSize(14);
        if ("BJTR".equals(s.getSamplingCode())){//背景土壤
            hocker.dept.setText(String.valueOf("背景土壤"));
        }else if ("SD".equals(s.getSamplingCode())){//农作物
            hocker.dept.setText(String.valueOf("农作物"));
        }else if ("NTTR".equals(s.getSamplingCode())){//农田土壤
            hocker.dept.setText(String.valueOf("农田土壤"));
        }else if ("WATER".equals(s.getSamplingCode())){//水采样
            hocker.dept.setText(String.valueOf("水样底泥"));
        }else if ("DQ".equals(s.getSamplingCode())){//大气沉降物
            hocker.dept.setText(String.valueOf("大气沉降"));
        }else if ("FL".equals(s.getSamplingCode())){//肥料
            hocker.dept.setText(String.valueOf("肥料"));
        }else {
            //其他错误的采样信息
            hocker.dept.setText(String.valueOf("其他"));
        }
        hocker.position.setText(TextUtil.isEmpty(s.getRegion_id())?"":s.getRegion_id());
        hocker.position.setTextColor(context.getResources().getColor(R.color.gray));
        hocker.position.setTextSize(14);
        hocker.iv.setVisibility(View.GONE);
//        //这个点位已经有了草稿，可以进行提交
//        hocker.iv.setImageResource(R.mipmap.lighton);
//        hocker.iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //进行提示弹窗，询问用户是否确认修改上传状态。
//                msgDialog = new MsgDialog(context);
//                msgDialog.setTiele("是否确认上传状态信息？");
//                msgDialog.setButLeftListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        msgDialog.dismiss();
//                    }
//                });
//                msgDialog.setButRightListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        msgDialog.dismiss();
//                        main.onItemImageClick(s.getSamplingId(),false);
//                    }
//                });
//                msgDialog.show();
//            }
//        });
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
        hocker.position.setText("采样地址");
        hocker.position.setTextColor(context.getResources().getColor(R.color.white));
        hocker.position.setTextSize(16);
        hocker.desc.setVisibility(View.GONE);
//        hocker.desc.setText("一键上传");
//        hocker.desc.setTextSize(16);
//        hocker.desc.setTextColor(context.getResources().getColor(R.color.white));
//        hocker.desc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //进行提示弹窗，询问用户是否确认修改上传状态。
//                msgDialog = new MsgDialog(context);
//                msgDialog.setTiele("是否确认上传所有状态信息？");
//                msgDialog.setButLeftListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        msgDialog.dismiss();
//                    }
//                });
//                msgDialog.setButRightListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        msgDialog.dismiss();
//                        main.onAllUpdateClick();
//                    }
//                });
//                msgDialog.show();
//            }
//        });
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
