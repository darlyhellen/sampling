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


    public static final int LISTSTATEFIRST = 0x1004;

    public static final int LISTSTATEREFRESH = 0x1005;

    public static final int LISTSTATELOADMORE = 0x1006;

    public static String Root = Environment.getExternalStorageDirectory() + "/Sampling/";
    public static String VIDEO = Root.concat("video/");
    public static String SENCE = VIDEO.concat("sence/");

    public static String getUrlHead() {
        if (DEBUGURL) {
            return "http://" + SystemCfg.getServerIP(XiangXunApplication.getInstance()) + ":" + SystemCfg.getServerPort(XiangXunApplication.getInstance());
        }
        return "http://" + SystemCfg.getServerIP(XiangXunApplication.getInstance()) + ":" + SystemCfg.getServerPort(XiangXunApplication.getInstance());
    }

    //http://10.10.15.201:8090公司外网超图地址
    public static String getMalink() {
        if (DEBUGURL) {
            return "http://" + SystemCfg.getGISServerIP(XiangXunApplication.getInstance()) + ":" + SystemCfg.getGISServerPort(XiangXunApplication.getInstance()) + "/iserver/services/map-MianZhuShi2/rest/maps/绵竹市";
        }
        return "http://" + SystemCfg.getGISServerIP(XiangXunApplication.getInstance()) + ":" + SystemCfg.getGISServerPort(XiangXunApplication.getInstance()) + "/iserver/services/map-MianZhuShi2/rest/maps/绵竹市";
    }

    //测试环境 仅仅调整了定位问题。写死的经纬度
    public static final boolean TESTING = true;
    //是否是虚拟机测试
    public static final boolean TESTPHONE = false;
    public static final double latitude = 31.320828804632135465;
    public static final double longitude = 104.24860444448522;
}
