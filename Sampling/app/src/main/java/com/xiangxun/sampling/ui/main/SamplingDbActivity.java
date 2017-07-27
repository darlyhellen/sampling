package com.xiangxun.sampling.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.orm.SugarRecord;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.ui.adapter.SamplingDBAdapter;
import com.xiangxun.sampling.ui.biz.SamplingDBListener.SamplingDBInterface;
import com.xiangxun.sampling.ui.presenter.SamplingDBPresenter;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.xlistView.ItemClickListenter;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:保存在本地的数据库信息，进行展示
 */
@ContentBinder(R.layout.activity_sampling_planning)
public class SamplingDbActivity extends BaseActivity implements SamplingDBInterface {
    @ViewsBinder(R.id.id_planning_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_planning_wlist)
    private StickyListHeadersListView wlist;
    @ViewsBinder(R.id.id_planning_text)
    private TextView textView;

    private List<SenceSamplingSugar> data;

    private SamplingDBAdapter adapter;

    private SamplingDBPresenter presenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("我的采样");
        data = SugarRecord.listAll(SenceSamplingSugar.class);
        presenter = new SamplingDBPresenter(this);
    }

    @Override
    protected void loadData() {
        adapter = new SamplingDBAdapter(data, R.layout.item_planning_list, this, this);
        if (data != null && data.size() != 0) {
            wlist.setAdapter(adapter);
            wlist.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else {
            wlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有提交的现场采样信息");
        }
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
                SenceSamplingSugar sugar = (SenceSamplingSugar) parent.getItemAtPosition(position);
                if (sugar != null) {
                    Intent intent = new Intent(SamplingDbActivity.this, SenceOptionActivity.class);
                    intent.putExtra("SenceSamplingSugar", sugar);
                    startActivity(intent);

                }
            }
        });
    }

    @Override
    public void onUpSuccess() {
        data = SugarRecord.listAll(SenceSamplingSugar.class);
        adapter.setData(data);
    }

    @Override
    public void onUpFailed() {

    }

    @Override
    public void onItemImageClick(String id, String pointId) {
        presenter.upAll(id,pointId);
    }

    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }
}
