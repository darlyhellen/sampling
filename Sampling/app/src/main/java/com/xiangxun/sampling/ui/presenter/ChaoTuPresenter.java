package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.HisPlanningData;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.ui.biz.ChaoTuListener;
import com.xiangxun.sampling.ui.main.ChaoTuActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;

import java.util.List;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 历史现场页面展示
 */
public class ChaoTuPresenter {

    private String TAG = getClass().getSimpleName();
    private ChaoTuListener biz;
    private ChaoTuListener.ChaoTuInterface view;
    private LoadDialog loading;

    public ChaoTuPresenter(ChaoTuListener.ChaoTuInterface view) {
        this.view = view;
        this.biz = new ChaoTuListener();
        loading = new LoadDialog((ChaoTuActivity) view);
        loading.setTitle(R.string.st_loading);
    }


    public void point(final String id,String code) {
        biz.onStart(loading);
        biz.postHisPoint(id, code, new FrameListener<List<HisPlanningData.HisPoint>>() {

            @Override
            public void onSucces(List<HisPlanningData.HisPoint> result) {
                biz.onStop(loading);
                view.onDateSuccess(result);
            }

            @Override
            public void onFaild(int code, String info) {
                biz.onStop(loading);
                ToastApp.showToast(info);
                view.onDateFailed(info);
            }
        });
    }
}