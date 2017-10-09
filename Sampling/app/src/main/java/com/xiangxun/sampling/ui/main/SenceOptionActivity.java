package com.xiangxun.sampling.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.db.MediaSugar;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.ui.adapter.ImageAdapter;
import com.xiangxun.sampling.ui.adapter.VideoAdapter;
import com.xiangxun.sampling.widget.groupview.DetailView;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.listview.WholeGridView;
import com.xiangxun.sampling.widget.timeselecter.TimeSelector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    //顶部描述
    @ViewsBinder(R.id.id_user_sence_description)
    private TextView id_user_sence_description;
    //位置信息
    @ViewsBinder(R.id.id_user_locations)
    private LinearLayout id_user_locations;
    @ViewsBinder(R.id.id_user_sence_address)
    private DetailView address;
    @ViewsBinder(R.id.id_user_sence_lat)
    private DetailView latitude;
    @ViewsBinder(R.id.id_user_sence_lont)
    private DetailView longitude;
    //选项一
    @ViewsBinder(R.id.id_user_sence_type)
    private DetailView type;
    //下拉选项一
    @ViewsBinder(R.id.id_user_sence_typeed_show)
    private RelativeLayout id_user_sence_typeed_show;
    @ViewsBinder(R.id.id_user_sence_typeed)
    private DetailView id_user_sence_typeed;
    //选项二
    @ViewsBinder(R.id.id_user_sence_name)
    private DetailView name;
    //选项三
    @ViewsBinder(R.id.id_user_sence_params)
    private DetailView params;
    //选项四
    @ViewsBinder(R.id.id_user_sence_other)
    private DetailView other;
    //下拉选项二
    @ViewsBinder(R.id.id_user_sence_project_show)
    private RelativeLayout id_user_sence_project_show;
    @ViewsBinder(R.id.id_user_sence_project)
    private DetailView id_user_sence_project;
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
        //根據sugar中的simplyCode進行判斷展示。
        if (sugar != null) {
            id_user_sence_description.setText("您的采样编码为： " + sugar.getCode());
            if (sugar.getSamplingCode().equals("BJTR")){//背景土壤
                id_user_sence_project_show.setVisibility(View.GONE);
                type.isEdit(false);
                type.setInfo("*采样类型:", sugar.getTypesamply().name, "");
                id_user_sence_typeed.isEdit(false);
                id_user_sence_typeed.setInfo("*墙土来源:", sugar.getOthersamply().name, null);
                other.setVisibility(View.GONE);
                name.isEdit(false);
                name.setInfo("*周围环境:", sugar.getAmbient(),null);
                params.isEdit(false);
                params.setInfo("*成墙年份:", sugar.getYears(), null);
            }else if (sugar.getSamplingCode().equals("SD")){//农作物
                id_user_sence_project_show.setVisibility(View.GONE);
                type.isEdit(false);
                type.setInfo("*采样类型:", sugar.getTypesamply().name, "");
                id_user_sence_typeed_show.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
                other.setVisibility(View.GONE);
                params.isEdit(false);
                params.setInfo("*采样部位:", sugar.getPosition(), null);
            }else if (sugar.getSamplingCode().equals("NTTR")){//农田土壤
                id_user_sence_project_show.setVisibility(View.GONE);
                type.isEdit(false);
                type.setInfo("*采样类型:", sugar.getTypesamply().name, "");
                id_user_sence_typeed_show.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
                other.setVisibility(View.GONE);
                params.isEdit(false);
                params.setInfo("*样品深度(CM):", sugar.getDepth(), null);
            }else if (sugar.getSamplingCode().equals("WATER")){//水采样
                id_user_sence_project_show.setVisibility(View.GONE);
                type.isEdit(false);
                type.setInfo("*采样类型:", sugar.getTypesamply().name, "");
                id_user_sence_typeed.isEdit(false);
                id_user_sence_typeed.setInfo("*样品来源:", sugar.getOthersamply().name, "");
                params.setVisibility(View.GONE);
                other.setVisibility(View.GONE);
                name.isEdit(false);
                name.setInfo("*河流名称:", sugar.getRiversName(), "");
            }else if (sugar.getSamplingCode().equals("DQ")){//大气沉降物
                type.setVisibility(View.GONE);
                id_user_sence_project_show.setVisibility(View.GONE);
                id_user_sence_typeed.isEdit(false);
                id_user_sence_typeed.setInfo("*点位信息:", sugar.getOthersamply().name, "");
                other.setVisibility(View.GONE);
                name.isEdit(false);
                name.setInfo("*容器体积(L):", sugar.getContainerVolume(), "");
                params.isEdit(false);
                params.setInfo("*收集量:", sugar.getCollectVolume(), "");
                //大氣採樣隱藏定位信息。
                id_user_locations.setVisibility(View.GONE);
            }else if (sugar.getSamplingCode().equals("FL")){//肥料
                id_user_sence_project_show.setVisibility(View.GONE);
                id_user_sence_typeed_show.setVisibility(View.GONE);
                type.isEdit(false);
                type.setInfo("*店名:", sugar.getShopName(), "");
                name.isEdit(false);
                name.setInfo("*店主:", sugar.getShopkeeper(), "");
                params.isEdit(false);
                params.setInfo("*联系方式:",sugar.getTel(), "");
                other.isEdit(false);
                other.setInfo("*经营肥料:", sugar.getDealManure(), "");
            }else {
                //其他错误的采样信息
                id_user_sence_project_show.setVisibility(View.GONE);
                type.setVisibility(View.GONE);
                id_user_sence_typeed_show.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
                params.setVisibility(View.GONE);
                other.setVisibility(View.GONE);
            }
            if (!sugar.getSamplingCode().equals("DQ")) {
                address.isEdit(false);
                address.setInfo("*采样地点：", sugar.getRegion_id(), null);
                latitude.isEdit(false);
                latitude.setInfo("*经度：", sugar.getLongitude(), null);
                longitude.isEdit(false);
                longitude.setInfo("*纬度：", sugar.getLatitude(), null);
            }
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
