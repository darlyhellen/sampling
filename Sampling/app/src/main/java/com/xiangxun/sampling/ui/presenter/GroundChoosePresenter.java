package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.GroundTypeInfo;
import com.xiangxun.sampling.bean.HisExceptionInfo;
import com.xiangxun.sampling.ui.biz.GroundChooseListener;
import com.xiangxun.sampling.ui.main.GroundChooseActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 指标查询页面操作
 */
public class GroundChoosePresenter {

    private String TAG = getClass().getSimpleName();
    private GroundChooseListener userBiz;
    private GroundChooseListener.GroundChooseInterface main;

    public GroundChoosePresenter(GroundChooseListener.GroundChooseInterface main) {
        this.main = main;
        this.userBiz = new GroundChooseListener();

    }


    /**
     * 获取地块信息。
     */
    public void block() {
        userBiz.getBlock(new FrameListener<GroundTypeInfo>() {
            @Override
            public void onSucces(GroundTypeInfo data) {
                main.onDateSuccess(data.result);
            }

            @Override
            public void onFaild(int code, String info) {
                main.onDateFailed(info);
            }
        });
    }
}