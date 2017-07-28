package com.xiangxun.sampling.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.PlannningData.Pointly;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.bean.SenceLandRegion;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.db.MediaSugar;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.ui.adapter.ImageAdapter;
import com.xiangxun.sampling.ui.adapter.SenceImageAdapter;
import com.xiangxun.sampling.ui.adapter.SenceImageAdapter.OnImageConsListener;
import com.xiangxun.sampling.ui.adapter.SenceVideoAdapter;
import com.xiangxun.sampling.ui.adapter.SenceVideoAdapter.OnVideoConsListener;
import com.xiangxun.sampling.ui.adapter.VideoAdapter;
import com.xiangxun.sampling.ui.biz.SenceListener.SenceInterface;
import com.xiangxun.sampling.ui.presenter.SencePresenter;
import com.xiangxun.sampling.widget.dialog.MsgDialog;
import com.xiangxun.sampling.widget.dialog.SelectTypeRegionDialog;
import com.xiangxun.sampling.widget.groupview.DetailView;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.listview.WholeGridView;
import com.xiangxun.video.camera.VCamera;
import com.xiangxun.video.ui.WechatRecoderActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/7.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 現場上传的的展示頁面
 */
@ContentBinder(R.layout.activity_sence)
public class SenceOptionActivity extends BaseActivity {
    @ViewsBinder(R.id.id_user_sence_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_user_locations_name)
    private TextView locationname;
    @ViewsBinder(R.id.id_user_sence_address)
    private DetailView address;
    @ViewsBinder(R.id.id_user_sence_lat)
    private DetailView latitude;
    @ViewsBinder(R.id.id_user_sence_lont)
    private DetailView longitude;
    @ViewsBinder(R.id.id_user_sence_type)
    private DetailView type;
    @ViewsBinder(R.id.id_user_sence_name)
    private DetailView name;
    @ViewsBinder(R.id.id_user_sence_params)
    private DetailView params;
    @ViewsBinder(R.id.id_user_sence_project)
    private DetailView project;
    @ViewsBinder(R.id.id_user_sence_other)
    private DetailView other;
    @ViewsBinder(R.id.id_user_sence_location)
    private ImageView loca;
    @ViewsBinder(R.id.id_user_sence_video_submit)
    private Button submit;


    @ViewsBinder(R.id.id_user_sence_image_grid)
    private WholeGridView imageGrid;
    @ViewsBinder(R.id.id_user_sence_video_grid)
    private WholeGridView videoGrid;

    private ImageAdapter imageAdapter;
    private VideoAdapter videoAdapter;

    private List<String> images;
    private List<String> videos;


    //进行草稿保存状态。草稿状态存在。则进行原始数据展示。
    private SenceSamplingSugar sugar;

    @Override
    protected void initView(Bundle savedInstanceState) {
        //这句话解决了自动弹出输入按键
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        titleView.setTitle("现场采样");
        locationname.setText("现场采样定位：");
        sugar = (SenceSamplingSugar) getIntent().getSerializableExtra("SenceSamplingSugar");
    }


    @Override
    protected void loadData() {

        if (sugar != null) {
            //已经有草稿状态。直接进行全参数设置
            type.isEdit(false);
            type.setInfo("采样类型:", sugar.getTest_item(), "");
            name.isEdit(false);
            name.setInfo("样品名称:", sugar.getName(), null);
            params.isEdit(false);
            params.setInfo("样品深度:", sugar.getDepth(), null);
            project.isEdit(false);
            project.setInfo("土壤类型:", sugar.getSoil_name(), null);
            project.setTag(sugar.getResult());
            other.isEdit(false);
            other.setInfo("其他說明:", "", null);
            address.isEdit(false);
            address.setInfo("采样地点：", sugar.getRegion_id(), null);
            latitude.isEdit(false);
            latitude.setInfo("经度：", sugar.getLongitude(), null);
            longitude.isEdit(false);
            longitude.setInfo("纬度：", sugar.getLatitude(), null);
            //初始化图片和视频信息所在位置。
            List<MediaSugar> media = MediaSugar.find(MediaSugar.class, "samplingId = ?", sugar.getSamplingId());
            images = new ArrayList<String>();
            videos = new ArrayList<String>();
            for (MediaSugar me : media) {
                if ("video".equals(me.getType())) {
                    //视频信息
                    videos.add(me.getUrl());
                } else {
                    images.add(me.getUrl());
                }
            }
            imageAdapter = new ImageAdapter(images, R.layout.item_main_detail_image_adapter, this);
            imageGrid.setAdapter(imageAdapter);
            videoAdapter = new VideoAdapter(videos, R.layout.item_main_detail_video_adapter, this);
            videoGrid.setAdapter(videoAdapter);
        }
    }

    @Override
    protected void initListener() {
        imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里跳转不同的图片页面
                String st = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(SenceOptionActivity.this, ShowImageViewActivity.class);
                intent.putExtra("position", position);
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                intent.putExtra("locationX", location[0]);//必须
                intent.putExtra("locationY", location[1]);//必须
                intent.putExtra("url", st);
                intent.putExtra("width", view.getWidth());//必须
                intent.putExtra("height", view.getHeight());//必须
                startActivity(intent);
            }
        });
        videoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里跳转不同的视频页面
                String st = (String) parent.getItemAtPosition(position);
                //跳转到视频录制页面
            }
        });


        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }
}
