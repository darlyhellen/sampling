package com.xiangxun.sampling.ui.setting;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.widget.clearedit.ViewEditTextEx;
import com.xiangxun.sampling.widget.header.TitleView;


/**
 * @TODO:系统设置页面
 */
@ContentBinder(R.layout.activity_syscfg)
public class SystemSettingActivity extends BaseActivity {
    @ViewsBinder(R.id.tv_comm_title)
    private TitleView titleView;
    @ViewsBinder(R.id.syscfgset)
    private Button btnset;
    @ViewsBinder(R.id.edt_server)
    private ViewEditTextEx server;

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle(R.string.cssz);
    }

    @Override
    protected void loadData() {
        server.setTextViewText("管理平台地址");
        server.setEditTextOneHint("请输入管理平台IP");
        server.setEditTextTwoHint("请输入管理平台Port");
        server.setEditTextOneText(SystemCfg.getServerIP(this));
        server.setEditTextTwoText(SystemCfg.getServerPort(this));
        server.setEditTextTwoInputMode(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public void initListener() {
        btnset.setOnClickListener(new BtnSetListener());
        titleView.setLeftBackOneListener(R.mipmap.back_normal, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }

    private class BtnSetListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            SystemCfg.setServerIP(SystemSettingActivity.this, server.getEditTextOneText().toString());
            SystemCfg.setServerPort(SystemSettingActivity.this, server.getEditTextTwoText().toString());
            setResult(Activity.RESULT_OK);
            onBackPressed();
        }
    }
}
