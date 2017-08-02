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
        presenter = new HisSencePresenter(this);
        presenter.sencehispage(id, missionId);
    }


    @Override
    protected void loadData() {
        //已经有草稿状态。直接进行全参数设置
        type.isEdit(false);
        type.setInfo("采样类型:", " ", "");
        name.isEdit(false);
        name.setInfo("样品名称:", " ", null);
        params.isEdit(false);
        params.setInfo("样品深度:", " ", null);
        project.isEdit(false);
        project.setInfo("土壤类型:", " ", null);
        other.isEdit(false);
        other.setInfo("其他說明:", "", null);
        address.isEdit(false);
        address.setInfo("采样地点：", " ", null);
        latitude.isEdit(false);
        latitude.setInfo("经度：", " ", null);
        longitude.isEdit(false);
        longitude.setInfo("纬度：", " ", null);
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
            type.isEdit(false);
            type.setInfo("采样类型:", result.sampleName, "");
            name.isEdit(false);
            name.setInfo("样品名称:", result.name, null);
            params.isEdit(false);
            params.setInfo("样品深度:", result.depth, null);
            project.isEdit(false);
            project.setInfo("土壤类型:", result.soilNme, null);
            other.isEdit(false);
            other.setInfo("其他說明:", "", null);
            address.isEdit(false);
            address.setInfo("采样地点：", result.regionName, null);
            latitude.isEdit(false);
            latitude.setInfo("经度：", result.longitude, null);
            longitude.isEdit(false);
            longitude.setInfo("纬度：", result.latitude, null);
            if (result.file != null && result.file.size() > 0) {
                for (HisSencePageInfo.FileList fils : result.file) {
                    if (!fils.filePath.endsWith(".mp4")) {
                        images.add(fils.filePath);
                    }
                }
                imageAdapter.setData(images);
            }
        }
    }

    @Override
    public void onDateFailed(String info) {

    }

    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }
}
