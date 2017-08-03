package com.xiangxun.sampling.common.retrofit;

import android.os.Environment;

import com.xiangxun.sampling.base.AppBuildConfig;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.base.XiangXunApplication;

/**
 * @ClassName: Api.java
 * @Description: 所有链接头部公共部分
 * @author: aaron_han
 * @date: 2015-1-14 下午01:08:27
 */
public class Api {
    /**
     * true为测试机
     */
    private static final boolean DEBUGURL = AppBuildConfig.DEBUGURL;


    public static String Root = Environment.getExternalStorageDirectory() + "/Sampling/";
    public static String VIDEO = Root.concat("video/");
    public static String SENCE = VIDEO.concat("sence/");

    public static String getUrlHead() {
        if (DEBUGURL) {
            return "http://" + SystemCfg.getServerIP(XiangXunApplication.getInstance()) + ":" + SystemCfg.getServerPort(XiangXunApplication.getInstance());
        }
        return "http://" + SystemCfg.getServerIP(XiangXunApplication.getInstance()) + ":" + SystemCfg.getServerPort(XiangXunApplication.getInstance());
    }

    public static final String CHAOTU = "http://10.10.15.201:8090/iserver/services/map-MianZhuShi2/rest/maps/绵竹市";
    public static final String TESTCHAOTU = "http://193.169.100.232:8090/iserver/services/map-MianZhuShi2/rest/maps/绵竹市";
    //测试环境
    public static final boolean TESTING = true;
    //是否是虚拟机测试
    public static final boolean TESTPHONE = true;
    public static final double latitude = 31.320828804632135465;
    public static final double longitude = 104.24860444448522;

    public static String getSipIp() {
        return SystemCfg.getServerIP(XiangXunApplication.getInstance());
    }
}
