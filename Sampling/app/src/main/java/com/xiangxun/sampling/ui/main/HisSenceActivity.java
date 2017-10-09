package com.xiangxun.sampling.ui.main;

import android.content.Intent;
import android.os.Bundle;
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
import com.xiangxun.sampling.bean.HisSencePageInfo;
import com.xiangxun.sampling.bean.SimplingTarget;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.db.MediaSugar;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.ui.adapter.ImageAdapter;
import com.xiangxun.sampling.ui.adapter.VideoAdapter;
import com.xiangxun.sampling.ui.biz.HisSenceListener;
import com.xiangxun.sampling.ui.biz.HisSenceListener.HisSenceInterface;
import com.xiangxun.sampling.ui.presenter.HisSencePresenter;
import com.xiangxun.sampling.widget.groupview.DetailView;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.listview.WholeGridView;

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
public class HisSenceActivity extends BaseActivity implements HisSenceInterface {
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
    @ViewsBinder(R.id.id_user_sence_video)
    private LinearLayout video;

    private ImageAdapter imageAdapter;

    private List<String> images;

    private HisSencePresenter presenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        //这句话解决了自动弹出输入按键
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        titleView.setTitle("现场采样");
        locationname.setText("现场采样定位：");
        video.setVisibility(View.GONE);
        String id = getIntent().getStringExtra("ID");
        String missionId = getIntent().getStringExtra("missionId");
        String tableName = getIntent().getStringExtra("tableName");
        presenter = new HisSencePresenter(this);
        presenter.sencehispage(id, missionId,tableName);
    }


    @Override
    protected void loadData() {
        //初始化图片和视频信息所在位置。
        images = new ArrayList<String>();
        imageAdapter = new ImageAdapter(images, R.layout.item_main_detail_image_adapter, this);
        imageGrid.setAdapter(imageAdapter);
    }

    @Override
    protected void initListener() {
        imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里跳转不同的图片页面
                String st = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(HisSenceActivity.this, ShowImageViewActivity.class);
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
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onDateSuccess(HisSencePageInfo.HisSencePage result) {
        if (result != null) {
            id_user_sence_description.setText("已采样的CODE："+result.code);
                if (result.samplingCode.equals("BJTR")){//背景土壤
                    id_user_sence_project_show.setVisibility(View.GONE);
                    type.isEdit(false);
                    type.setInfo("*采样类型:", result.typesamplyName, "");
                    id_user_sence_typeed.isEdit(false);
                    id_user_sence_typeed.setInfo("*墙土来源:",  result.othersamplyName, null);
                    other.setVisibility(View.GONE);
                    name.isEdit(false);
                    name.setInfo("*周围环境:", result.ambient,null);
                    params.isEdit(false);
                    params.setInfo("*成墙年份:",result.years, null);
                }else if (result.samplingCode.equals("SD")){//农作物
                    id_user_sence_project_show.setVisibility(View.GONE);
                    type.isEdit(false);
                    type.setInfo("*采样类型:", result.typesamplyName, "");
                    id_user_sence_typeed_show.setVisibility(View.GONE);
                    name.setVisibility(View.GONE);
                    other.setVisibility(View.GONE);
                    params.isEdit(false);
                    params.setInfo("*采样部位:", result.position, null);
                }else if (result.samplingCode.equals("NTTR")){//农田土壤
                    id_user_sence_project_show.setVisibility(View.GONE);
                    type.isEdit(false);
                    type.setInfo("*采样类型:", result.typesamplyName, "");
                    id_user_sence_typeed_show.setVisibility(View.GONE);
                    name.setVisibility(View.GONE);
                    other.setVisibility(View.GONE);
                    params.isEdit(false);
                    params.setInfo("*样品深度(CM):", result.depth, null);
                }else if (result.samplingCode.equals("WATER")){//水采样
                    id_user_sence_project_show.setVisibility(View.GONE);
                    type.isEdit(false);
                    type.setInfo("*采样类型:", result.typesamplyName, "");
                    id_user_sence_typeed.isEdit(false);
                    id_user_sence_typeed.setInfo("*样品来源:", result.othersamplyName, "");
                    params.setVisibility(View.GONE);
                    other.setVisibility(View.GONE);
                    name.isEdit(false);
                    name.setInfo("*河流名称:",result.riversName, "");
                }else if (result.samplingCode.equals("DQ")){//大气沉降物
                    type.setVisibility(View.GONE);
                    id_user_sence_project_show.setVisibility(View.GONE);
                    id_user_sence_typeed.isEdit(false);
                    id_user_sence_typeed.setInfo("*点位信息:", result.othersamplyName, "");
                    other.setVisibility(View.GONE);
                    name.isEdit(false);
                    name.setInfo("*容器体积(L):",result.containerVolume, "");
                    params.isEdit(false);
                    params.setInfo("*收集量:",result.collectVolume, "");
                    //大氣採樣隱藏定位信息。
                    id_user_locations.setVisibility(View.GONE);
                }else if (result.samplingCode.equals("FL")){//肥料
                    id_user_sence_project_show.setVisibility(View.GONE);
                    id_user_sence_typeed_show.setVisibility(View.GONE);
                    type.isEdit(false);
                    type.setInfo("*店名:",result.shopName, "");
                    name.isEdit(false);
                    name.setInfo("*店主:",result.shopkeeper, "");
                    params.isEdit(false);
                    params.setInfo("*联系方式:",result.tel, "");
                    other.isEdit(false);
                    other.setInfo("*经营肥料:",result.dealManure, "");
                }else {
                    //其他错误的采样信息
                    id_user_sence_project_show.setVisibility(View.GONE);
                    type.setVisibility(View.GONE);
                    id_user_sence_typeed_show.setVisibility(View.GONE);
                    name.setVisibility(View.GONE);
                    params.setVisibility(View.GONE);
                    other.setVisibility(View.GONE);
                }
                if (!result.samplingCode.equals("DQ")) {
                    address.isEdit(false);
                    address.setInfo("*采样地点：",result.regionName, null);
                    latitude.isEdit(false);
                    latitude.setInfo("*经度：",result.longitude, null);
                    longitude.isEdit(false);
                    longitude.setInfo("*纬度：",result.latitude, null);
                }
            if (result.file != null && result.file.size() > 0) {
                for (HisSencePageInfo.FileList fils : result.file) {
                    if (!fils.filePath.endsWith(".mp4")) {
                        images.add(fils.filePath);
                    }
                }
                imageAdapter.setData(images);
            }
        }else {
            id_user_sence_project_show.setVisibility(View.GONE);
            type.setVisibility(View.GONE);
            id_user_sence_typeed_show.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            params.setVisibility(View.GONE);
            other.setVisibility(View.GONE);
            id_user_locations.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDateFailed(String info) {
        id_user_sence_project_show.setVisibility(View.GONE);
        type.setVisibility(View.GONE);
        id_user_sence_typeed_show.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        params.setVisibility(View.GONE);
        other.setVisibility(View.GONE);
        id_user_locations.setVisibility(View.GONE);
    }

    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }
}
