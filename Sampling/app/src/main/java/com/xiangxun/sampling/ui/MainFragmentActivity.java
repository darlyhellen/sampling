package com.xiangxun.sampling.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.UpdateData;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.fun.InfoCache;
import com.xiangxun.sampling.ui.biz.VersionListener.VersionInterface;
import com.xiangxun.sampling.ui.presenter.VersionPresenter;
import com.xiangxun.sampling.ui.setting.SettingActivity;
import com.xiangxun.sampling.widget.dialog.UpdateDialog;
import com.xiangxun.sampling.widget.header.TitleView;

import java.util.ArrayList;
import java.util.List;


/**
 * @TODO:项目首页，进行轮播展示，还有4个图表分类展示
 */
@ContentBinder(R.layout.activity_main)
public class MainFragmentActivity extends BaseActivity implements OnItemClickListener, OnClickListener, VersionInterface {

    private long exitTime = 0;
    @ViewsBinder(R.id.id_main_title)
    private TitleView titleView;

    @ViewsBinder(R.id.id_main_convenientBanner)
    private ConvenientBanner<Integer> banner;
    @ViewsBinder(R.id.id_main_iv)
    private ImageView iv;

    private List<Integer> data = new ArrayList<Integer>();

    private VersionPresenter presenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle(R.string.app_name);
        titleView.setRightViewRightOneListener(R.drawable.btn_selecter_mune, this);
        data.add(R.mipmap.ic_main_menu1);
        initFragments(MainIndexFragment.class, R.id.id_main_index);
    }

    @Override
    protected void loadData() {
        if (NetUtils.isNetworkAvailable(this)) {
            //DcNetWorkUtils.getVersoin(false, handler, this);
            presenter = new VersionPresenter(this);
            presenter.findVersion(XiangXunApplication.getAppVersionName());
        }
    }

    @Override
    public void initListener() {
        if (data.size() > 1) {
            banner.setVisibility(View.VISIBLE);
            iv.setVisibility(View.GONE);
            banner.setLayoutParams(new LinearLayout.LayoutParams(SystemCfg.getWidth(this), (int) (SystemCfg.getWidth(this) / 1.67)));
            banner.setPages(
                    new CBViewHolderCreator<LocalImageHolderView>() {
                        @Override
                        public LocalImageHolderView createHolder() {
                            return new LocalImageHolderView();
                        }
                    }, data)
                    //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                    .setPageIndicator(new int[]{R.mipmap.ic_point_normal, R.mipmap.ic_point_select})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .startTurning(5000)
                    .setOnItemClickListener(this);
        } else if (data.size() == 1) {
            banner.setVisibility(View.GONE);
            iv.setVisibility(View.VISIBLE);
            iv.setLayoutParams(new LinearLayout.LayoutParams(SystemCfg.getWidth(this), (int) (SystemCfg.getWidth(this) / 1.67)));
            iv.setBackgroundResource(data.get(0));
            iv.setOnClickListener(this);
        }
    }


    @Override
    public void onItemClick(int i) {
        DLog.i(getClass().getSimpleName(), "点击-->" + i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_view_operation_imageview_right:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            default:
                break;
        }
        DLog.i(getClass().getSimpleName(), "菜单");
    }

    private UpdateDialog updateDialog;// 更新版本提示框

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
                    updateDialog = new UpdateDialog(this, R.style.updateDialog, result.name, sb.toString(), result.saveUrl);
                }
                updateDialog.setCancelable(true);
                updateDialog.show();
            }
        }
    }

    @Override
    public void onVersionFailed(String info) {

    }

    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }

    /**
     * @TODO：此方式是轮播的展示补充
     */
    public class LocalImageHolderView implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Integer data) {
            imageView.setImageResource(data);
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastApp.showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }


//    @SuppressLint("HandlerLeak")
//    Handler handler = new Handler() {
//        private MsgDialog mUpdateDialog;
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case ConstantStatus.updateSuccess:// 获取版本更新数据成功
//                    MsgToast.geToast().cancel();
//                    String[] arrayStr = { "发现新版本,请更新!" };
//                    StringBuilder sb = new StringBuilder();
////				for (int i = 0, len = arrayStr.length; i < len; i++) {
////					sb.append(new StringBuilder(String.valueOf(i + 1)).append(".").toString());
////					sb.append(arrayStr[i]);
////					sb.append(i == len - 1 ? "\n" : "\n\n");
////				}
//                    String str = InfoCache.getInstance().getmData().remark;
//                    if(str !=null && str.length() > 0){
//                        sb.append(str);
//                    } else {
//                        sb.append("发现新版本,请更新!");
//                    }
//                    String url = InfoCache.getInstance().getmData().saveUrl.trim();
//                    if (updateDialog == null) {
//                        updateDialog = new UpdateDialog(MainFragmentActivity.this, R.style.updateDialog, InfoCache.getInstance().getmData().name, sb.toString(), url);
//                    }
//                    updateDialog.setCancelable(true);
//                    updateDialog.show();
//                    break;
//                case ConstantStatus.updateFalse:// 版本更新
//                    if (mUpdateDialog == null) {
//                        mUpdateDialog = new MsgDialog(MainFragmentActivity.this);
//                        mUpdateDialog.setTiele(Html.fromHtml(getText(R.string.update_tips_html).toString()));
//                        mUpdateDialog.setMsg(getString(R.string.latest_version_please_look));
//                        mUpdateDialog.setOnlyOneBut();
//                    }
//                    mUpdateDialog.show();
//                    break;
//            }
//        }
//    };


    @Override
    protected void onStart() {
        DLog.d(getClass().getSimpleName(), "onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        DLog.d(getClass().getSimpleName(), "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        DLog.d(getClass().getSimpleName(), "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        DLog.d(getClass().getSimpleName(), "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        DLog.d(getClass().getSimpleName(), "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DLog.d(getClass().getSimpleName(), "onDestroy()");
        super.onDestroy();
    }
}
