package com.xiangxun.sampling.ui.adapter;

import android.content.Context;
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

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/10.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:现场采集页面，适配器效果，存放图片和视频文件。
 */
public class SenceImageAdapter extends ParentAdapter<String> {
    public interface OnImageConsListener {
        void onConsImageListener(View v, int position);
    }

    private OnImageConsListener listener;


    public SenceImageAdapter(List<String> data, int resID, Context context, OnImageConsListener listener) {
        super(data, resID, context);
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return data.size() > 3 ? 3 : data.size();
    }

    @Override
    public View HockView(final int position, View view, ViewGroup parent, int resID, Context context, String info) {
        ViewHocker hocker = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resID, null);
            hocker = new ViewHocker();
            hocker.relative = (RelativeLayout) view.findViewById(R.id.id_iv_relative);
            hocker.relative.setLayoutParams(new AbsListView.LayoutParams(SystemCfg.getWidth(context) / 3, SystemCfg.getWidth(context) / 3));
            hocker.photo = (ImageView) view.findViewById(R.id.id_iv_photo);
            hocker.close = (ImageView) view.findViewById(R.id.id_iv_close);
            hocker.desc = (TextView) view.findViewById(R.id.id_tv_desc);
            view.setTag(hocker);
        } else {
            hocker = (ViewHocker) view.getTag();
        }
        if (parent.getChildCount() == position) { // 里面就是正常的position
            if (position == (data.size() - 1)) {
                hocker.photo.setImageResource(R.drawable.add_publish_image);
                hocker.photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
                hocker.close.setVisibility(View.GONE);
            } else {
                hocker.close.setVisibility(View.VISIBLE);
                hocker.desc.setText("");
                ImageLoader.getInstance().displayImage(
                        "file://" + data.get(position), hocker.photo);
                hocker.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onConsImageListener(v, position);
                    }
                });

            }
        }


        return view;
    }

    class ViewHocker {
        LinearLayout lin;
        ImageView image;
        RelativeLayout relative;
        ImageView photo;
        ImageView close;
        TextView desc;
    }
}
