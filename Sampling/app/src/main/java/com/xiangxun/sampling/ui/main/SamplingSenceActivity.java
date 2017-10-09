package com.xiangxun.sampling.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.bean.SamplingSenceGroup;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.ui.adapter.SamplingSenceAdapter;
import com.xiangxun.sampling.ui.biz.SamplingPlanningListener.SamplingPlanningInterface;
import com.xiangxun.sampling.ui.presenter.SamplingPlanningPresenter;
import com.xiangxun.sampling.widget.header.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:现场采样展示效果
 */
@ContentBinder(R.layout.activity_sence_ex)
public class SamplingSenceActivity extends BaseActivity implements SamplingPlanningInterface {
    @ViewsBinder(R.id.id_sence_title)
    private TitleView titleView;

    @ViewsBinder(R.id.id_sence_wlist)
    private ExpandableListView wlist;
    @ViewsBinder(R.id.id_sence_text)
    private TextView textView;
    private List<SamplingSenceGroup.SenceGroup> data;
    private SamplingSenceAdapter adapter;

    private SamplingPlanningPresenter presenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("现场采样");
        //在这里进行方案列表请求。获取到的信息，进行缓存。对修改的信息进行处理操作。
        presenter = new SamplingPlanningPresenter(this);
    }

    @Override
    protected void loadData() {
       // presenter.planning(null/*s == null ? null : ((ResultData) s).resTime*/);
        presenter.findPlanning();
    }

    @Override
    protected void initListener() {
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        //重写OnGroupClickListener，实现当展开时，ExpandableListView不自动滚动
        wlist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                SamplingSenceGroup.SenceGroup groupData = (SamplingSenceGroup.SenceGroup) adapter.getGroup(groupPosition);
                if (groupData ==  null){
                    return false;
                }
                if ("背景土壤".equals(groupData.regType)){//背景土壤
                   ToastApp.showToast(groupData.regType+"功能暂未开通，请等候新版本。");
                    return true;
                }else if ("农作物".equals(groupData.regType)){//农作物
                    //ToastApp.showToast(groupData.regType+"功能暂未开通，请等候新版本。");
                    return false;
                }else if ("农田土壤".equals(groupData.regType)){//农田土壤
                    //ToastApp.showToast(groupData.regType+"功能暂未开通，请等候新版本。");
                    return false;
                }else if ("水样底泥".equals(groupData.regType)){//水采样
                    ToastApp.showToast(groupData.regType+"功能暂未开通，请等候新版本。");
                    return true;
                }else if ("大气沉降".equals(groupData.regType)){//大气沉降物
                    //ToastApp.showToast(groupData.regType+"功能暂未开通，请等候新版本。");
                    return false;
                }else if ("肥料".equals(groupData.regType)){//肥料
                    ToastApp.showToast(groupData.regType+"功能暂未开通，请等候新版本。");
                    return true;
                }else {
                    ToastApp.showToast(groupData.regType+"功能暂未开通，请等候新版本。");
                    return true;
                }

//                if (parent.isGroupExpanded(groupPosition)) {
//                    parent.collapseGroup(groupPosition);
//                } else {
//                    //第二个参数false表示展开时是否触发默认滚动动画
//                    parent.expandGroup(groupPosition, false);
//                }
            }
        });
        wlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Scheme data = (Scheme) adapter.getChild(groupPosition, childPosition);
                DLog.i(getClass().getSimpleName(), groupPosition + "---" + childPosition + "---" + data);
                //到现场采集页面.
                if (data!=null&&data.sampleCode.equals("DQ")){
                    if (data.regNum >= data.quantity){
                        ToastApp.showToast("任务："+data.missionName+"下的点位已采样完成");
                        return false;
                    }
                }
                //進行數據緩存，并可以實現點擊標註
                SharePreferHelp.putValue("SenceActivity",data);
                SharePreferHelp.putValue("groupPosition",groupPosition);
                wlist.collapseGroup(groupPosition);
                wlist.expandGroup(groupPosition);
                Intent intent = new Intent(SamplingSenceActivity.this, SenceActivity.class);
                intent.putExtra("Scheme", data);
                startActivityForResult(intent, 1000);
                return false;
            }
        });
    }

    @Override
    public void onLoginSuccessV1(List<SamplingSenceGroup.SenceGroup> result) {
        data = result;
        if (data != null && data.size() > 0) {
            wlist.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            adapter = new SamplingSenceAdapter(this, data);
            wlist.setAdapter(adapter);
            refreshList();
        } else {
            wlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有采样的计划");
        }
    }

    //现场采样中获取采样计划列表
    @Override
    public void onLoginSuccess(List<Scheme> info) {
        //data = info;
        data = new ArrayList<SamplingSenceGroup.SenceGroup>();
        SamplingSenceGroup.SenceGroup daqi = new SamplingSenceGroup().new SenceGroup();
        daqi.regType = "大气采样";
        daqi.result = info;
        data.add(daqi);
        SamplingSenceGroup.SenceGroup tur = new SamplingSenceGroup().new SenceGroup();
        tur.regType = "土壤采样";
        tur.result = info;
        data.add(tur);
        if (data != null && data.size() > 0) {
            wlist.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            adapter.setData(data);
        } else {
            wlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有采样的计划");
        }
    }

    @Override
    public void onLoginFailed() {
        wlist.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText("没有采样的计划");
    }

    @Override
    public void end() {
    }

    @Override
    public void setDisableClick() {
    }

    @Override
    public void setEnableClick() {

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent datas) {
        super.onActivityResult(requestCode, resultCode, datas);
        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            presenter.findPlanning();
        }
    }


    private void refreshList(){
        if (wlist!=null){
            for (int i=0; i<wlist.getCount(); i++) {
                if (i == SharePreferHelp.getValue("groupPosition",-1)) {
                    wlist.expandGroup(i);
                }
            }
        }
    }
}
