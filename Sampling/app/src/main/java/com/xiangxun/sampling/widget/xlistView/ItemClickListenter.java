package com.xiangxun.sampling.widget.xlistView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.Calendar;

/**
 * Created by Zhangyuhui/Darly on 2017/6/9.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:解决列表条目瞬间点击多次，打开多个页面的BUG。
 */
public abstract class ItemClickListenter implements OnItemClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            NoDoubleItemClickListener(parent, view, position, id);
        }
    }

    public abstract void NoDoubleItemClickListener(AdapterView<?> parent, View view, int position, long id);
}
