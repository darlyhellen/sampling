package com.xiangxun.sampling.common.http;

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
    private final boolean DEBUGURL = AppBuildConfig.DEBUGURL;
    public String urlHost = getUrlHead();
    /**
     * m模块下
     */
    public String urlHeadlogin = urlHost;
    /**
     * mobile模块下
     */
    public String urlHeadMobile = urlHost;
    public String urlHeadMpts = urlHost;
    // path
    public static String Root = Environment.getExternalStorageDirectory() + "/Sampling/";
    // 发布拍照path
    public static String xXPublishPictureDir = Root.concat("/publishPicture/");
    public static String VIDEO = Root.concat("video/");
    public static String SENCE = VIDEO.concat("sence/");

    public String getUrlHead() {
        if (DEBUGURL) {
            return "http://" + SystemCfg.getServerIP(XiangXunApplication.getInstance()) + ":" + SystemCfg.getServerPort(XiangXunApplication.getInstance());
        }
        return "http://" + SystemCfg.getServerIP(XiangXunApplication.getInstance()) + ":" + SystemCfg.getServerPort(XiangXunApplication.getInstance());
    }

    /**
     * SIP
     */
    public static String password = "123456yng";

    public static String getSipIp() {
        return SystemCfg.getServerIP(XiangXunApplication.getInstance());
    }
}
