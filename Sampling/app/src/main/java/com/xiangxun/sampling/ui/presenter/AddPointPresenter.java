package com.xiangxun.sampling.ui.presenter;


import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.PlannningData.Point;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.ui.biz.AddPointListener;
import com.xiangxun.sampling.ui.biz.AddPointListener.AddPointInterface;
import com.xiangxun.sampling.ui.main.AddNewPointPlanningActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;

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


    public void addPoint(Scheme planning) {
        biz.onStart(loading);
        biz.addPostPoint((AddNewPointPlanningActivity) view, planning, view.longitude(), view.latitude(), new FrameListener<List<PlannningData.Pointly>>() {
            @Override
            public void onSucces(List<PlannningData.Pointly> result) {
                biz.onStop(loading);
                view.onLoginSuccess(result);
            }

            @Override
            public void onFaild(int code, String info) {
                biz.onStop(loading);
                view.onLoginFailed(info);
            }
        });

    }

    public void updataPoint(Scheme planning, PlannningData.Pointly point) {

        biz.onStart(loading);
        biz.updataPostPoint(planning, point, new FrameListener<List<PlannningData.Pointly>>() {
            @Override
            public void onSucces(List<PlannningData.Pointly> result) {

            }

            @Override
            public void onFaild(int code, String info) {

            }
        });
    }
}