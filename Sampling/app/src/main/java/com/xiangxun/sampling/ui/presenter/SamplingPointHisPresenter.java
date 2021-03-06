package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.HisPlanningData;
import com.xiangxun.sampling.bean.PlannningData.ResultPointData;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.ui.biz.SamplingPointHisListener;
import com.xiangxun.sampling.ui.biz.SamplingPointHisListener.SamplingPointHisInterface;
import com.xiangxun.sampling.ui.main.SamplingHisPointActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;

import java.util.List;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 历史点位页点位列表操作
 */
public class SamplingPointHisPresenter {

    private String TAG = getClass().getSimpleName();
    private SamplingPointHisListener biz;
    private SamplingPointHisInterface view;
    private LoadDialog loading;

    public SamplingPointHisPresenter(SamplingPointHisInterface view) {
        this.view = view;
        this.biz = new SamplingPointHisListener();
        loading = new LoadDialog((SamplingHisPointActivity) view);
        loading.setTitle(R.string.st_loading);
    }


    public void point(final String id,String code) {
        biz.onStart(loading);
        biz.postHisPoint(id, code, new FrameListener<List<HisPlanningData.HisPoint>>() {

            @Override
            public void onSucces(List<HisPlanningData.HisPoint> result) {
                biz.onStop(loading);
                view.onLoginSuccess(result);
            }

            @Override
            public void onFaild(int code, String info) {
                biz.onStop(loading);
                ToastApp.showToast(info);
                view.onLoginFailed();
            }
        });

    }


}