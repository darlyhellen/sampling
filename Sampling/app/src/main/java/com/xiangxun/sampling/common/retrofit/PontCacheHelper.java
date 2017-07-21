package com.xiangxun.sampling.common.retrofit;

import android.text.TextUtils;

import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.PlannningData.Pointly;
import com.xiangxun.sampling.bean.PlannningData.ResultPointData;
import com.xiangxun.sampling.common.SharePreferHelp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/20.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 点位新增和修改，缓存修改类。包含点位信息变化等。
 */
public class PontCacheHelper {

    public static void cachePoint(String id, ResultPointData result) {
        List<Pointly> poly = new ArrayList<Pointly>();
        Object s = SharePreferHelp.getValue(id);
        if (s != null) {
            ResultPointData data = (ResultPointData) s;
            if (data.result != null && data.result.size() != 0) {
                for (Pointly po : result.result) {
                    boolean isUpdate = false;
                    for (int i = 0; i < data.result.size(); i++) {
                        if (po.unique.equals(data.result.get(i).unique)) {
                            //当返回的数据和缓存数据有相同的是，就是修改点位。
                            data.result.get(i).data = po.data;
                            isUpdate = true;
                        } else {
                            //点位新增
                            if (i == data.result.size() - 1 && !isUpdate) {
                                poly.add(po);
                            }
                        }
                    }
                }
                //在这里更新方案缓存。修改方案的点位个数 。
                Object st = SharePreferHelp.getValue("ResultData");
                if (st != null) {
                    PlannningData.ResultData resultsd = (PlannningData.ResultData) st;
                    for (PlannningData.Scheme sh : resultsd.result) {
                        if (sh.id.equals(id)) {
                            sh.quantity += poly.size();
                            break;
                        }
                    }
                    SharePreferHelp.putValue("ResultData", resultsd);
                }
                data.result.addAll(poly);
            } else {
                data.result = result.result;
            }
            //缓存最近更新的时间戳
            if (!TextUtils.isEmpty(result.resTime)) {
                data.resTime = result.resTime;
            }
            SharePreferHelp.putValue(id, data);
        } else {
            SharePreferHelp.putValue(id, result);
        }
    }
}
