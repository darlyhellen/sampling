package com.xiangxun.sampling.ui.main;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.common.image.ImageLoaderUtil;
import com.xiangxun.sampling.widget.image.SmoothImageView;

/**
 * Created by Zhangyuhui/Darly on 2017/6/1.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:GridView图片点击放大的展示效果。
 */
public class ShowImageViewActivity extends BaseActivity implements OnClickListener {

    SmoothImageView imageView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        int mLocationX = getIntent().getIntExtra("locationX", 0);
        int mLocationY = getIntent().getIntExtra("locationY", 0);
        int mWidth = getIntent().getIntExtra("width", 0);
        int mHeight = getIntent().getIntExtra("height", 0);
        String url = getIntent().getStringExtra("url");

        imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        setContentView(imageView);
        if (url.startsWith("http")) {
            ImageLoaderUtil.getInstance().loadImageNor(url, imageView);
        } else {
            ImageLoader.getInstance().displayImage("file://" + url, imageView);
        }
    }

    @Override
    protected void loadData() {
        
    }

    @Override
    protected void initListener() {
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
