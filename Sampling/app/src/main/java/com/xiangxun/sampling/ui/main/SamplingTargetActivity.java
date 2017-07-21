package com.xiangxun.sampling.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.SimplingTarget;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.ui.adapter.SamplingTargetAdapter;
import com.xiangxun.sampling.ui.biz.TargetListener.TargetInterface;
import com.xiangxun.sampling.ui.presenter.TargetPresenter;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.xlistView.ItemClickListenter;
import com.xiangxun.sampling.widget.xlistView.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:指标查询查询各个功能
 */
@ContentBinder(R.layout.activity_sampling_planning)
public class SamplingTargetActivity extends BaseActivity implements TargetInterface, XListView.IXListViewListener {

    @ViewsBinder(R.id.id_planning_title)
    private TitleView titleView;

    @ViewsBinder(R.id.id_planning_wlist)
    private XListView xlist;
    @ViewsBinder(R.id.id_planning_text)
    private TextView textView;
    private String textDes;

    private SamplingTargetAdapter adapter;

    private List<SimplingTarget> data;

    private TargetPresenter presenter;
    //至关重要的一个参数

    private int currentPage = 1;
    private int PageSize = 10;
    private int totalSize = 0;
    private String workorder;
    private String devicename;
    private String devicenum;
    private String deviceip;
    private int listState = Api.LISTSTATEFIRST;


    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("指标查询");
        presenter = new TargetPresenter(this);
        //请求列表
        presenter.analysis();
    }

    @Override
    protected void loadData() {
        data = new ArrayList<SimplingTarget>();
        adapter = new SamplingTargetAdapter(data, R.layout.item_planning_list, this);
    }

    @Override
    protected void initListener() {
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        titleView.setRightViewRightTextOneListener("筛选", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击筛选，获取删选列表。进行数据重新请求
            }
        });


        xlist.setPullLoadEnable(true);
        xlist.setXListViewListener(this);
        xlist.setOnItemClickListener(new ItemClickListenter() {
            @Override
            public void NoDoubleItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                DLog.i("onItemClick--" + position);
            }
        });
    }


    @Override
    public void onRefresh(View v) {
        currentPage = 1;
        listState = Api.LISTSTATEREFRESH;
        presenter.analysis();
    }

    @Override
    public void onLoadMore(View v) {
        if (totalSize < PageSize) {
            ToastApp.showToast("已经是最后一页");
            xlist.removeFooterView(xlist.mFooterView);
        } else {
            currentPage++;
            listState = Api.LISTSTATELOADMORE;
            presenter.analysis();
        }
    }

    protected void setWorkOrderData(List<SimplingTarget> orderBeans) {
        xlist.removeFooterView(xlist.mFooterView);
        if (orderBeans.size() > PageSize - 1) {
            xlist.addFooterView(xlist.mFooterView);
        }
        switch (listState) {
            case Api.LISTSTATEFIRST:
                data.clear();
                data.addAll(orderBeans);
                adapter.setData(data);
                xlist.smoothScrollToPosition(1);
                break;
            case Api.LISTSTATEREFRESH:
                data.clear();
                data.addAll(orderBeans);
                adapter.setData(data);
                break;
            case Api.LISTSTATELOADMORE:
                data.addAll(orderBeans);
                adapter.setData(data);
                break;
        }
        totalSize = orderBeans.size();
    }

    // xLisView 停止
    private void stopXListView() {
        xlist.stopRefresh();
        xlist.stopLoadMore();
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


    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }

    @Override
    public void onDateSuccess(List<SimplingTarget> result) {
        stopXListView();
        setWorkOrderData(result);
        if (xlist.getCount() > 1) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(textDes);
        }
    }

    @Override
    public void onDateFailed(String info) {
        stopXListView();
    }
}
