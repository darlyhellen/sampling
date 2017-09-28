package com.xiangxun.sampling.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.orm.SugarRecord;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.SamplingPlanning;
import com.xiangxun.sampling.bean.UpdateData;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.fun.BaseFunctionList;
import com.xiangxun.sampling.fun.Function;
import com.xiangxun.sampling.fun.InfoCache;
import com.xiangxun.sampling.ui.LoginActivity;
import com.xiangxun.sampling.ui.StaticListener;
import com.xiangxun.sampling.ui.StaticListener.RefreshMainUIListener;
import com.xiangxun.sampling.ui.biz.SettingFragmentListener.SettingFragmentInterface;
import com.xiangxun.sampling.ui.biz.VersionListener.VersionInterface;
import com.xiangxun.sampling.ui.main.SamplingDbActivity;
import com.xiangxun.sampling.ui.presenter.SettingFragmentPresenter;
import com.xiangxun.sampling.ui.presenter.VersionPresenter;
import com.xiangxun.sampling.widget.dialog.LoadDialog;
import com.xiangxun.sampling.widget.dialog.MsgDialog;
import com.xiangxun.sampling.widget.dialog.UpdateDialog;

import java.io.File;
import java.util.List;


public class SettingFragment extends BaseFunctionList implements SettingFragmentInterface, VersionInterface ,RefreshMainUIListener {
    private int isNoLoginFlag;
    private View mParentView = null;
    private ListView functionListView;

    private MsgDialog msgDialog;
    private int deleteDay = 0;
    /**
     * 更新版本提示框
     */
    private UpdateDialog updateDialog;// 更新版本提示框

    private SettingFragmentPresenter presenter;
    private VersionPresenter versionPresenter;

    private Function[] functions = {
            new Function(R.mipmap.set_password, R.string.set_fun1, "提供密码修改功能", ChangePasswordActivity.class),
//            new Function(R.mipmap.set_system, R.string.set_fun2, "设置系统相关参数", SystemSettingActivity.class),
            new Function(R.mipmap.set_cleardata, R.string.set_fun3, "清除缓存数据", null),
            new Function(R.mipmap.set_font, R.string.set_fun4, "在WiFi情况下上传采样图片视频", SamplingDbActivity.class),
            new Function(R.mipmap.set_update, R.string.set_fun5, "检查更新", null),
            new Function(R.mipmap.set_loginout, R.string.set_out, "", null)
    };

    private Function[] functionsNoLogin = {
            new Function(R.mipmap.set_system, R.string.set_fun2, "设置系统相关参数", SystemSettingActivity.class),
    };
    private MsgDialog mUpdateDialog;

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
        presenter = new SettingFragmentPresenter(this);
        versionPresenter = new VersionPresenter(this);
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
        upload();
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
        StaticListener.getInstance().setRefreshMainUIListener(this);
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
                                                                if (1 == position) {
                                                                    clearDateOnClick();
                                                                } else if (2 == position) {
                                                                    startActivity(new Intent(getActivity(), functions[position].getActivityClass()));
                                                                } else if (3 == position) {
                                                                    CheckUpdate();
                                                                } else {
                                                                    loginout();
                                                                }
                                                            }
                                                        }
                                                    }

            );
        }
    }

    private void upload() {
        int size = SugarRecord.listAll(SenceSamplingSugar.class).size();
        if (size != 0) {
            functions[2].setDescription(" New! " + size);
        }else {
            functions[2].setDescription("在WiFi情况下上传采样图片视频");
        }
    }


    private void loginout() {

        msgDialog = new MsgDialog(getActivity());
        msgDialog.setTiele("确认退出登录？");
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
                //进行退出登录操作，清理登录缓存。
                presenter.loginout();
            }
        });
        msgDialog.show();
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
        //进行缓存数据清空操作，包括数据库数据和文件数据。
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                RecursionDeleteFile(new File(Api.Root));
                XiangXunApplication.getInstance().createFiles();
                loadDialog.dismiss();
                ToastApp.showToast("删除成功");
            }
        }, 0);
    }

    /**
     * @param file TODO：删除文件夹下所有文件。
     */
    public void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            if (!file.getAbsolutePath().contains("video")) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    RecursionDeleteFile(f);
                }
                file.delete();
            }
        }
    }

    private void CheckUpdate() {
        if (NetUtils.isNetworkAvailable(getActivity())) {
            versionPresenter.findVersion(XiangXunApplication.getAppVersionName());
        } else {
            ToastApp.showToast("无网络,请检查网络是否正常连接!");
        }
    }

    private void showNew() {
        if (InfoCache.getInstance().isNewVer()) {
            functions[3].setDescription("New!");
        } else {
            String version = new StringBuilder(getString(R.string.curr_ver)).append(InfoCache.getInstance().getAppVersionName(getActivity())).toString();
            functions[3].setDescription(version);
        }
    }

    @Override
    public void onLoginSuccess() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        getActivity().startActivity(i);
        getActivity().onBackPressed();
    }

    @Override
    public void onLoginFailed(String info) {

    }

    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }

    @Override
    public void onVersionSuccess(UpdateData result) {
        if (result != null) {
            if (Integer.parseInt(result.version) > XiangXunApplication.getInstance().getVersionCode()) {
                StringBuilder sb = new StringBuilder();
                String str = result.remark;
                if (str != null && str.length() > 0) {
                    sb.append(str);
                } else {
                    sb.append("发现新版本,请更新!");
                }
                if (updateDialog == null) {
                    updateDialog = new UpdateDialog(getActivity(), R.style.updateDialog, result.name, sb.toString(), result.saveUrl);
                }
                updateDialog.setCancelable(true);
                updateDialog.show();
                showNew();
            } else {
                onVersionFailed("");
            }
        }
    }

    @Override
    public void onVersionFailed(String info) {
        if (mUpdateDialog == null) {
            mUpdateDialog = new MsgDialog(getActivity());
            mUpdateDialog.setTiele(Html.fromHtml(getText(R.string.update_tips_html).toString()));
            mUpdateDialog.setMsg(getString(R.string.latest_version_please_look));
            mUpdateDialog.setOnlyOneBut();
        }
        mUpdateDialog.show();
    }

    @Override
    public void refreshMainUI(List<SamplingPlanning> planningList) {
        //刷新UI
        initData();
    }
}