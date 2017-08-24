
package com.xiangxun.sampling.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.HisExceptionPageInfo;
import com.xiangxun.sampling.bean.SimplingTarget;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.image.ImageLoaderUtil;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.ui.adapter.ImageAdapter;
import com.xiangxun.sampling.ui.adapter.SenceImageAdapter;
import com.xiangxun.sampling.ui.biz.HEPListener;
import com.xiangxun.sampling.ui.biz.HEPListener.HEPInterface;
import com.xiangxun.sampling.ui.presenter.HEPPresenter;
import com.xiangxun.sampling.widget.groupview.DetailView;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.listview.WholeGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:地块异常展示详情，不让用户进行任何修改。
 */
@ContentBinder(R.layout.activity_sampling_exception_show)
public class SamplingHisExceptionPageActivity extends BaseActivity implements HEPInterface {

    @ViewsBinder(R.id.id_exception_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_user_sence_address)
    private DetailView address;
    @ViewsBinder(R.id.id_user_sence_lat)
    private DetailView latitude;
    @ViewsBinder(R.id.id_user_sence_lont)
    private DetailView longitude;
    //类型
    @ViewsBinder(R.id.id_exception_select)
    private TextView select;
    //地块信息
    @ViewsBinder(R.id.id_exception_land)
    private TextView land;
    @ViewsBinder(R.id.id_exception__declare)
    private TextView declare;
    @ViewsBinder(R.id.id_exception_gird)
    private WholeGridView gridView;
    private List<String> images;
    private ImageAdapter imageAdapter;

    private HEPPresenter presenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        //这句话解决了自动弹出输入按键
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        titleView.setTitle("地块异常");
        presenter = new HEPPresenter(this);
        presenter.getExc(getIntent().getStringExtra("id"));
    }

    @Override
    protected void loadData() {
        images = new ArrayList<String>();
        imageAdapter = new ImageAdapter(images, R.layout.item_main_detail_image_adapter, this);
        gridView.setAdapter(imageAdapter);
    }


    @Override
    protected void initListener() {

        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里跳转不同的图片页面
                String st = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(SamplingHisExceptionPageActivity.this, ShowImageViewActivity.class);
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
    }

    @Override
    public void onDateSuccess(HisExceptionPageInfo.HisExceptionPage result) {
        if (result != null) {
            address.isEdit(false);
            address.setInfo("位置：", result.regionName, null);
            latitude.isEdit(false);
            latitude.setInfo("经度：", result.longitude, null);
            longitude.isEdit(false);
            longitude.setInfo("纬度：", result.latitude, null);
            //类型
            select.setText(result.regionName);
            //地块信息
            land.setText(result.landBlockName);
            declare.setText(result.describe);
            for (HisExceptionPageInfo.FileList lis : result.fileList) {
                images.add(lis.filePath);
            }
            if (images != null) {
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
