package com.xiangxun.sampling.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Window;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.binder.InitBinder;


/**
 * @author zhangyh2 BaseActivity $ 下午2:33:01 TODO
 */
public abstract class BaseActivity extends FragmentActivity {
    public int REQUEST_CODE = 0; // 请求码

    public String[] PERMISSIONS_GROUP = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @TargetApi(19)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initGlobalVariable();
        initView(savedInstanceState);
        loadData();
        initListener();
    }


    /**
     * 下午2:36:27
     *
     * @author zhangyh2 BaseActivity.java TODO
     * 初始化全局的一些变量。而且做好的静态变量。每个Activity里面的变量由自己来进行定义。
     */
    private void initGlobalVariable() {
        // TODO Auto-generated method stub
        InitBinder.InitAll(this);
    }

    /**
     * @param savedInstanceState 下午2:34:08
     * @author zhangyh2 BaseActivity.java TODO 初始化控件
     */

    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 下午2:34:10
     *
     * @author zhangyh2 BaseActivity.java TODO 加载数据
     */
    protected abstract void loadData();

    /**
     * 下午2:42:02
     *
     * @author zhangyh2 BaseFragment.java TODO 初始化坚挺事件
     */
    protected abstract void initListener();


    protected void initFragments(Class<?> cls, int resId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        try {
            fragment = (Fragment) cls.newInstance();
            transaction.add(resId, fragment);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        transaction.commit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        int4Right();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        int4Right();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (Build.VERSION.SDK_INT >= 23) {
//            // 缺少权限时, 进入权限配置页面
//            if (new PermissionCheck(this).lacksPermissions(PERMISSIONS_GROUP)) {
//                startPermissionsActivity();
//            }
//        } else {
//            XiangXunApplication.createFiles();
//        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS_GROUP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        } else if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
            XiangXunApplication.createFiles();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        out2Left();
    }

    public void out2Left() {
        // overridePendingTransition(R.anim.new_dync_no,
        // R.anim.new_dync_out_to_left);
        overridePendingTransition(R.anim.activity_nothing, R.anim.activity_out_to_buttom);
    }

    public void int4Right() {
        // overridePendingTransition(R.anim.new_dync_in_from_right,
        // R.anim.new_dync_no);
        overridePendingTransition(R.anim.right_in, R.anim.activity_nothing);
    }
}
