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
import com.xiangxun.sampling.common.image.ImageLoaderUtil;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/10.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:现场采集页面，适配器效果，存放图片和视频文件。
 */
public class ImageAdapter extends ParentAdapter<String> {


    public ImageAdapter(List<String> data, int resID, Context context) {
        super(data, resID, context);
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        } else {
            if (data.size() > 3) {
                return 3;
            } else {
                return data.size();
            }
        }
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
        hocker.close.setVisibility(View.GONE);


        hocker.desc.setText("");
        if (info.contains("http://")) {
            ImageLoaderUtil.getInstance().loadImageNor(info, hocker.photo);
        } else {
            ImageLoader.getInstance().displayImage(
                    "file://" + info, hocker.photo);
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
