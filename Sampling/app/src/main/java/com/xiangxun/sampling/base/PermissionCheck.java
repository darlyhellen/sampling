package com.xiangxun.sampling.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

/**
 * Created by Zhangyuhui/Darly on 2017/8/3.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 新增权限管理代码逻辑实现功能。在API23+以上也就是安卓6.0以上的，进行了权限管理
 * 不进行权限匹配，会出现文件夹无法创建。摄像头无法访问等问题。
 */
public class PermissionCheck {


    private final Context mContext;

    public PermissionCheck(Context context) {
        mContext = context.getApplicationContext();
    }

    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        // 一个list，用来存放没有被授权的权限
        ArrayList<String> denidArray = new ArrayList<String>();
        // 遍历PERMISSIONS_GROUP，将没有被授权的权限存放进denidArray
        for (String permission : permissions) {
            int grantCode = ActivityCompat.checkSelfPermission(mContext, permission);
            if (grantCode == PackageManager.PERMISSION_DENIED) {
                denidArray.add(permission);
            }
        }
        if (denidArray.size() == 0) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 权限控制工具类：
     * 为了适配API23，即Android M 在清单文件中配置use permissions后，还要在程序运行的时候进行申请。
     * <p/>
     * ***整个权限的申请与处理的过程是这样的：
     * *****1.进入主Activity，首先申请所有的权限；
     * *****2.用户对权限进行授权，有2种情况：
     * ********1).用户Allow了权限，则表示该权限已经被授权，无须其它操作；
     * ********2).用户Deny了权限，则下次启动Activity会再次弹出系统的Permisssions申请授权对话框。
     * *****3.如果用户Deny了权限，那么下次再次进入Activity，会再次申请权限，这次的权限对话框上，会有一个选项“never ask again”：
     * ********1).如果用户勾选了“never ask again”的checkbox，下次启动时就必须自己写Dialog或者Snackbar引导用户到应用设置里面去手动授予权限；
     * ********2).如果用户未勾选上面的选项，若选择了Allow，则表示该权限已经被授权，无须其它操作；
     * ********3).如果用户未勾选上面的选项，若选择了Deny，则下次启动Activity会再次弹出系统的Permisssions申请授权对话框。
     */


    /**
     * 关于shouldShowRequestPermissionRationale函数的一点儿注意事项：
     * ***1).应用安装后第一次访问，则直接返回false；
     * ***2).第一次请求权限时，用户Deny了，再次调用shouldShowRequestPermissionRationale()，则返回true；
     * ***3).第二次请求权限时，用户Deny了，并选择了“never ask again”的选项时，再次调用shouldShowRequestPermissionRationale()时，返回false；
     * ***4).设备的系统设置中，禁止了应用获取这个权限的授权，则调用shouldShowRequestPermissionRationale()，返回false。
     */

    /**
     * 对权限字符串数组中的所有权限进行申请授权，如果用户选择了“never ask again”，则不会弹出系统的Permission申请授权对话框
     */

}
