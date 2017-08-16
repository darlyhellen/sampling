package com.xiangxun.sampling.ui;


import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.SamplingKey;
import com.xiangxun.sampling.bean.SamplingPlanning;
import com.xiangxun.sampling.bean.SamplingPoint;
import com.xiangxun.sampling.common.dlog.DLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Zhangyuhui/Darly on 2017/5/25.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 强制实现静态接口。
 */
public class StaticListener {

    private static StaticListener instance;

    private StaticListener() {
    }

    public static StaticListener getInstance() {
        if (instance == null) {
            instance = new StaticListener();
        }
        DLog.i(instance);
        return instance;
    }

    private RefreshMainUIListener refreshMainUIListener;

    public void setRefreshMainUIListener(RefreshMainUIListener refreshMainUIListener) {
        this.refreshMainUIListener = refreshMainUIListener;
        DLog.i("RefreshMainUIListener is instance！" + instance + this.refreshMainUIListener);
    }

    public RefreshMainUIListener getRefreshMainUIListener() {
        DLog.i("RefreshMainUIListener is use！" + instance + this.refreshMainUIListener);
        if (refreshMainUIListener == null) {
            DLog.i("RefreshMainUIListener is not instance！ Please instance to use");
        }
        return refreshMainUIListener;
    }

    public interface RefreshMainUIListener {

        void refreshMainUI(List<SamplingPlanning> planningList);
    }

    public static List<SamplingPlanning> findData() {
        List<SamplingPlanning> data = new ArrayList<SamplingPlanning>();
        for (int i = 0; i < 10; i++) {
            SamplingPlanning planning = new SamplingPlanning();
            planning.setId(i + "");
            planning.setDepate("四川大学地址检测系");
            switch (i % 4) {
                case 0:
                    planning.setTitle("国家项目计划" + i);
                    planning.setPlace("曹家庄");
                    planning.setType("土壤");
                    planning.setSamplingexzample("农田土壤、水稻、玉米、大豆");
                    break;
                case 1:
                    planning.setTitle("城鎮项目计划" + i);
                    planning.setPlace("何家营");
                    planning.setType("地表水");
                    planning.setSamplingexzample("农田土壤、地表水、河水");
                    break;
                case 2:
                    planning.setTitle("省市项目计划" + i);
                    planning.setPlace("凯泰铭");
                    planning.setType("农作物");
                    planning.setSamplingexzample("水稻、玉米、大豆");
                    break;
                case 3:
                    planning.setTitle("區域项目计划" + i);
                    planning.setPlace("西高村");
                    planning.setType("地下水");
                    planning.setSamplingexzample("暗流河、地下水、根作物");
                    break;
            }
            planning.setPoints(findDouble(i));
            data.add(planning);
        }
        return data;
    }

    public static List<SamplingKey> findDouble(int i) {
        //分16组数据，每组100条
        String[] lis = XiangXunApplication.getInstance().getResources().getStringArray(R.array.mapPoint);
        List<SamplingKey> points = new ArrayList<SamplingKey>();

        for (int k = i * 100; k < (i + 1) * 100; k++) {
            String[] ar = lis[k].split(",");
            SamplingKey key = new SamplingKey();

            SamplingPoint point = new SamplingPoint();
            point.setId(k + "");
            point.setLatitude((float) convertToDecimalByString(ar[0] + "″"));
            point.setLongitude((float) convertToDecimalByString(ar[1] + "″"));
            point.setDesc(k + "说明文件");
            point.setSamply(new Random().nextBoolean());
            key.setId(k + "");
            key.setPoint(point);
            points.add(key);
        }
        return points;
    }


    public static double convertToDecimalByString(String latlng) {
        double du = Double.parseDouble(latlng.substring(0, latlng.indexOf("°")));
        double fen = Double.parseDouble(latlng.substring(latlng.indexOf("°") + 1, latlng.indexOf("′")));
        double miao = Double.parseDouble(latlng.substring(latlng.indexOf("′") + 1, latlng.indexOf("″")));
        if (du < 0)
            return -(Math.abs(du) + (fen + (miao / 60)) / 60);
        return du + (fen + (miao / 60)) / 60;

    }
}

