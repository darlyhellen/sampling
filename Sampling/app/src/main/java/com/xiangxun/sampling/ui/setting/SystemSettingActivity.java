package com.xiangxun.sampling.ui.setting;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.widget.groupview.DetailView;
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
    private DetailView server;
    @ViewsBinder(R.id.edt_port)
    private DetailView port;
    @ViewsBinder(R.id.edt_gis_server)
    private DetailView gis_server;
    @ViewsBinder(R.id.edt_gis_port)
    private DetailView gis_port;

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle(R.string.cssz);
    }

    @Override
    protected void loadData() {
        server.isEdit(true);
        if (TextUtils.isEmpty(SystemCfg.getServerIP(this))) {
            server.setInfo("接口服务地址：", " ", "请输入接口服务端IP");
        } else {
            server.setInfo("接口服务地址：", SystemCfg.getServerIP(this), null);
        }
        port.isEdit(true);
        if (TextUtils.isEmpty(SystemCfg.getServerPort(this))) {
            port.setInfo("接口服务端口：", " ", "请输入接口服务端Port");
        } else {
            port.setInfo("接口服务端口：", SystemCfg.getServerPort(this), null);
        }
        gis_server.isEdit(true);
        if (TextUtils.isEmpty(SystemCfg.getGISServerIP(this))) {
            gis_server.setInfo("地图服务地址：", " ", "请输入地图服务端IP");
        } else {
            gis_server.setInfo("地图服务地址：", SystemCfg.getGISServerIP(this), null);
        }
        gis_port.isEdit(true);
        if (TextUtils.isEmpty(SystemCfg.getGISServerPort(this))) {
            gis_port.setInfo("地图服务端口：", " ", "请输入地图服务端Port");
        } else {
            gis_port.setInfo("地图服务端口：", SystemCfg.getGISServerPort(this), null);
        }
    }

    @Override
    public void initListener() {
        btnset.setOnClickListener(new BtnSetListener());
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }

    private class BtnSetListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            SystemCfg.setServerIP(SystemSettingActivity.this, server.getText());
            SystemCfg.setServerPort(SystemSettingActivity.this, port.getText());
            SystemCfg.setGISServerIP(SystemSettingActivity.this, gis_server.getText());
            SystemCfg.setGISServerPort(SystemSettingActivity.this, gis_port.getText());
            setResult(Activity.RESULT_OK);
            onBackPressed();
        }
    }
}
