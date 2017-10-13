package com.xiangxun.sampling.common.retrofit;

import android.os.Environment;

import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.base.XiangXunApplication;

/**
 * @ClassName: Api.java
 * @Description: 所有链接头部公共部分
 * @author: aaron_han
 * @date: 2015-1-14 下午01:08:27
 */
public class Api {
    //是否屏蔽功能 true 展示每個功能 false 则只展示三个选项可以点击。
    public static final boolean ISNEWVERSION = true;
    //测试环境 仅仅调整了定位问题。写死的经纬度true
    public static final boolean TESTING = true;
    //是否是虚拟机测试
    public static final boolean TESTPHONE = false;
    public static final double latitude = 31.2541698804632135465;
    public static final double longitude = 104.13541454448522;
    public static final String zhangbin = "绵竹市玉泉镇";
    /**
     * true正式环境。地址为绵竹。
     * false开发环境，地址为自己
     */
    public static final boolean DEBUGURL = false;


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


}
