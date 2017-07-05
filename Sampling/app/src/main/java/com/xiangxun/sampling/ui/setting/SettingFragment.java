package com.xiangxun.sampling.ui.setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.common.ConstantStatus;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.fun.BaseFunctionList;
import com.xiangxun.sampling.fun.Function;
import com.xiangxun.sampling.fun.InfoCache;
import com.xiangxun.sampling.widget.dialog.LoadDialog;
import com.xiangxun.sampling.widget.dialog.MsgDialog;
import com.xiangxun.sampling.widget.dialog.UpdateDialog;


public class SettingFragment extends BaseFunctionList {
    private int isNoLoginFlag;
    private View mParentView = null;
    private ListView functionListView;

    private MsgDialog msgDialog;
    private int deleteDay = 0;
    /**
     * 更新版本提示框
     */
    private UpdateDialog updateDialog;// 更新版本提示框

    private Function[] functions = {
            new Function(R.mipmap.set_password, R.string.set_fun1, "提供密码修改功能", ChangePasswordActivity.class),
            new Function(R.mipmap.set_system, R.string.set_fun2, "设置系统相关参数", SystemSettingActivity.class),
            new Function(R.mipmap.set_cleardata, R.string.set_fun3, "清除缓存数据", null),
            new Function(R.mipmap.set_font, R.string.set_fun4, "设置拍摄图片大小", ResolutionFontSetActivity.class),
            new Function(R.mipmap.set_update, R.string.set_fun5, "检查更新", null),
    };

    private Function[] functionsNoLogin = {
            new Function(R.mipmap.set_system, R.string.set_fun2, "设置系统相关参数", SystemSettingActivity.class),
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        private MsgDialog mUpdateDialog;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantStatus.updateSuccess:// 获取版本更新数据成功
                    StringBuilder sb = new StringBuilder();
                    String str = InfoCache.getInstance().getmData().remark;
                    if (str != null && str.length() > 0) {
                        sb.append(str);
                    } else {
                        sb.append("发现新版本,请更新!");
                    }
                    String url = InfoCache.getInstance().getmData().saveUrl.trim();
                    if (updateDialog == null) {
                        updateDialog = new UpdateDialog(getActivity(), R.style.updateDialog, InfoCache.getInstance().getmData().name, sb.toString(), url);
                    }
                    updateDialog.setCancelable(true);
                    updateDialog.show();
                    showNew();
                    break;
                case ConstantStatus.updateFalse:// 版本更新
                    if (mUpdateDialog == null) {
                        mUpdateDialog = new MsgDialog(getActivity());
                        mUpdateDialog.setTiele(Html.fromHtml(getText(R.string.update_tips_html).toString()));
                        mUpdateDialog.setMsg(getString(R.string.latest_version_please_look));
                        mUpdateDialog.setOnlyOneBut();
                    }
                    mUpdateDialog.show();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.function_list, container, false);
        super.onCreate(savedInstanceState);
        return mParentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isNoLoginFlag = getActivity().getIntent().getIntExtra("isFlag", 0);
        initView();
        initData();
        initListener();
    }

    public void initView() {
        super.initView(mParentView);
        functionListView = getFunctionListView();
        setShowArrow(true);
    }

    @Override
    public void initData() {
        super.initData();

        if (1 == isNoLoginFlag) {
            setFunctions(functionsNoLogin);
        } else {
            setFunctions(functions);

            showNew();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        if (1 == isNoLoginFlag) {

            functionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (null != functionsNoLogin[position].getActivityClass())
                        startActivity(new Intent(getActivity(), functionsNoLogin[position].getActivityClass()));
                }
            });
        } else {

            functionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (null != functions[position].getActivityClass())
                        startActivity(new Intent(getActivity(), functions[position].getActivityClass()));
                    else {
                        if (2 == position) {
                            clearDateOnClick();
                        } else if (4 == position)
                            CheckUpdate();
                    }
                }
            });
        }
    }

    public void clearDateOnClick() {
        msgDialog = new MsgDialog(getActivity());
        msgDialog.setTiele("确定要删除缓存数据？");
        msgDialog.setButLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgDialog.dismiss();
            }
        });
        msgDialog.setButRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgDialog.dismiss();
                Deldata();
            }
        });
        msgDialog.show();
    }

    private void Deldata() {
        final LoadDialog loadDialog = new LoadDialog(getActivity());
        loadDialog.setTitle("正在处理,请稍后...");
        loadDialog.show();

//        DBManager.getInstance().clearAccidentData();
//        DBManager.getInstance().clearDangerData();
//        DBManager.getInstance().clearIllegalData();
//        DBManager.getInstance().clearChangeData();
//        DBManager.getInstance().clearEnvFileData();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                loadDialog.dismiss();
                ToastApp.showToast("删除成功");
            }
        }, 2000);
    }

    private void CheckUpdate() {
        if (NetUtils.isNetworkAvailable(getActivity())) {
            ToastApp.showToast("检查更新中");
            //DcNetWorkUtils.getVersoin(true, handler, getActivity());
        } else {
            ToastApp.showToast("无网络,请检查网络是否正常连接!");
        }
    }

    private void showNew() {
        if (InfoCache.getInstance().isNewVer()) {
            functions[4].setDescription("New!");
        } else {
            String version = new StringBuilder(getString(R.string.curr_ver)).append(InfoCache.getInstance().getAppVersionName(getActivity())).toString();
            functions[4].setDescription(version);
        }
    }
}