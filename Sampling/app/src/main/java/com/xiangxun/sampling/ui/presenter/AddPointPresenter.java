package com.xiangxun.sampling.ui.presenter;


import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.PlannningData.ResultPointData;
import com.xiangxun.sampling.bean.PlannningData.Pointly;
import com.xiangxun.sampling.bean.PlannningData.Point;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.retrofit.PontCacheHelper;
import com.xiangxun.sampling.common.retrofit.paramer.AddPointParamer;
import com.xiangxun.sampling.ui.biz.AddPointListener;
import com.xiangxun.sampling.ui.biz.AddPointListener.AddPointInterface;
import com.xiangxun.sampling.ui.main.AddNewPointPlanningActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 新增点位对应解决方案.
 */
public class AddPointPresenter {

    private String TAG = getClass().getSimpleName();
    private AddPointListener biz;
    private AddPointInterface view;

    private LoadDialog loading;

    public AddPointPresenter(AddPointInterface view) {
        this.view = view;
        this.biz = new AddPointListener();
        loading = new LoadDialog((AddNewPointPlanningActivity) view);
        loading.setTitle(R.string.st_loading);
    }


    public void addPoint(final Scheme planning) {
        biz.onStart(loading);
        biz.addPostPoint((AddNewPointPlanningActivity) view, planning, view.longitude(), view.latitude(), new FrameListener<ResultPointData>() {
            @Override
            public void onSucces(ResultPointData result) {
                biz.onStop(loading);
                //新增点位正常完成。返回并关闭页面。根据返回信息，刷新列表
                PontCacheHelper.cachePoint(planning.id, result);
                view.onLoginSuccess();
            }

            @Override
            public void onFaild(int code, String info) {
                biz.onStop(loading);
                ToastApp.showToast(info);
                //新增点位出错。
            }
        });

    }

    public void updataPoint(final Scheme planning, Pointly point) {

        biz.onStart(loading);
        biz.updataPostPoint(planning, point.data, new FrameListener<ResultPointData>() {
            @Override
            public void onSucces(ResultPointData result) {
                biz.onStop(loading);
                //修改点位正常完成。。返回并关闭页面。根据返回信息，刷新列表
                PontCacheHelper.cachePoint(planning.id, result);
                view.onLoginSuccess();
            }

            @Override
            public void onFaild(int code, String info) {
                biz.onStop(loading);
                ToastApp.showToast(info);
                //修改点位出错。
            }
        });
    }

}