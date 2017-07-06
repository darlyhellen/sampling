package com.xiangxun.sampling.ui;


import com.xiangxun.sampling.bean.SamplingPlanning;
import com.xiangxun.sampling.bean.SamplingPoint;
import com.xiangxun.sampling.common.dlog.DLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
            planning.setId(UUID.randomUUID().toString());
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
            List<SamplingPoint> points = new ArrayList<SamplingPoint>();
            for (int j = 0; j < 50; j++) {
                SamplingPoint point = new SamplingPoint();
                point.setId(UUID.randomUUID().toString());
                point.setLatitude((float) (31.12490875901834 + new Random().nextInt(j + 1)));
                point.setLongitude((float) (104.12490875901834 + new Random().nextInt(j + 1)));
                point.setDesc(i + "说明文件" + j);
                point.setSamply(false);
                points.add(point);
            }
            planning.setPoints(points);
            DLog.i(points.size());
            data.add(planning);
        }
        return data;
    }


}

