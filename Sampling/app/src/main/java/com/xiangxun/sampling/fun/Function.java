package com.xiangxun.sampling.fun;

import android.app.Activity;

/**
 * Created by Administrator on 2017/2/21.
 */

public class Function {
    private int titleId;
    private String description;
    private int iconId;
    private Class<? extends Activity> activityClass;

    public Function(int iconId, int titleId, String description, Class<? extends Activity> activityClass) {
        super();
        this.iconId = iconId;
        this.titleId = titleId;
        this.description = description;
        this.activityClass = activityClass;
    }

    public int getTitleId() {
        return titleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        description = value;
    }

    public int getIconId() {
        return iconId;
    }

    public Class<? extends Activity> getActivityClass() {
        return activityClass;
    }
}
