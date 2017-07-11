package com.xiangxun.sampling.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.SamplingPlanning;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.ui.StaticListener;
import com.xiangxun.sampling.ui.adapter.StickyAdapter;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.xlistView.ItemClickListenter;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:现场采样展示效果
 */
@ContentBinder(R.layout.activity_sampling_sence)
public class SamplingSenceActivity extends BaseActivity {
    @ViewsBinder(R.id.id_sence_title)
    private TitleView titleView;

    @ViewsBinder(R.id.id_sence_wlist)
    private StickyListHeadersListView wlist;
    @ViewsBinder(R.id.id_sence_text)
    private TextView textView;
    private List<SamplingPlanning> data;
    private StickyAdapter adapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("现场采样");
    }

    @Override
    protected void loadData() {
        if (data == null) {
            data = StaticListener.findData();
        }
        adapter = new StickyAdapter(data, R.layout.item_planning_list, this, true);
        wlist.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });

        wlist.setOnItemClickListener(new ItemClickListenter() {
            @Override
            public void NoDoubleItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                SamplingPlanning planning = (SamplingPlanning) parent.getItemAtPosition(position);
                Intent intent = new Intent(SamplingSenceActivity.this, SamplingPointActivity.class);
                intent.putExtra("SamplingPlanning", planning);
                intent.putExtra("SENCE", true);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        DLog.d(getClass().getSimpleName(), "onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        DLog.d(getClass().getSimpleName(), "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        DLog.d(getClass().getSimpleName(), "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        DLog.d(getClass().getSimpleName(), "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        DLog.d(getClass().getSimpleName(), "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DLog.d(getClass().getSimpleName(), "onDestroy()");
        super.onDestroy();
    }
}
