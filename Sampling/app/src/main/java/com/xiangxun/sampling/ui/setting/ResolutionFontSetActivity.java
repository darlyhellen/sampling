package com.xiangxun.sampling.ui.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.widget.dialog.SelectItemDialog;
import com.xiangxun.sampling.widget.header.TitleView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ContentBinder(R.layout.activity_picsize)
public class ResolutionFontSetActivity extends BaseActivity implements OnClickListener, SelectItemDialog.SelectResultItemClick {
    private TitleView titleView;
    private Camera camera;
    private Camera.Parameters par;
    private SharedPreferences mysp = null;
    public static final String PREFERENCE_NAME = "xxsyscfg";

    private TextView textPicSize, textFontSize;
    private LinearLayout setPicSize, setFontSize;

    private SelectItemDialog picSizeDialog;
    private SelectItemDialog fontSizeDialog;

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView = (TitleView) findViewById(R.id.tv_comm_title);
        titleView.setTitle(R.string.zpyzt);
        textPicSize = (TextView) findViewById(R.id.tv_pic_size);
        textFontSize = (TextView) findViewById(R.id.tv_font_size);
        setPicSize = (LinearLayout) findViewById(R.id.rl_pic_size);
        setFontSize = (LinearLayout) findViewById(R.id.rl_font_size);
    }

    @Override
    protected void loadData() {
        mysp = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        camera = Camera.open();
        par = camera.getParameters();
        List<Size> piclist = par.getSupportedPictureSizes();
        if (!mysp.contains("viopicwidth")) {
            for (Size l : piclist) {
                if (l.height <= 2048 && l.width <= 1536) {
                    if (l.height > 640 && l.width > 480) {
                        SystemCfg.setVioPicHight(ResolutionFontSetActivity.this, l.height);
                        SystemCfg.setVioPicWidth(ResolutionFontSetActivity.this, l.width);
                    }
                    break;
                }
            }
        }

        final List<String> strs = new ArrayList<String>();
        Iterator<Size> itor = piclist.iterator();
        while (itor.hasNext()) {
            Camera.Size cur = itor.next();
            if (cur.width <= 2048 && cur.height <= 1536) {
                if (cur.height > 640 && cur.width > 480) {
                    strs.add(cur.width + "*" + cur.height);
                }
            }
        }
        int l = strs.size();
        String[] str = new String[l];
        for (int c = 0; c < l; c++) {
            str[c] = strs.get(c).toString();
        }

        camera.release();
        picSizeDialog = new SelectItemDialog(this, str, getResources().getString(R.string.picSize));
        String textsizes[] = {"16", "24", "32"};
        fontSizeDialog = new SelectItemDialog(this, textsizes, getResources().getString(R.string.txtSize));

        textPicSize.setText(SystemCfg.getVioPicWidth(this) + "*" + SystemCfg.getVioPicHight(this));
        textFontSize.setText(String.valueOf(SystemCfg.getTextSize(this)));
    }


    @Override
    public void initListener() {
        setPicSize.setOnClickListener(this);
        setFontSize.setOnClickListener(this);
        picSizeDialog.setSelectResultItemClick(this);
        fontSizeDialog.setSelectResultItemClick(this);
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pic_size:
                picSizeDialog.show();
                break;
            case R.id.rl_font_size:
                fontSizeDialog.show();
                break;
        }
    }

    @Override
    public void resultOnClick(String result, String title) {
        if (result.contains("*")) {
            int index = result.indexOf("*");
            String pw = result.substring(0, index);
            String ph = result.substring(index + 1);

            textPicSize.setText(result);

            SystemCfg.setVioPicWidth(ResolutionFontSetActivity.this, Integer.valueOf(pw));
            SystemCfg.setVioPicHight(ResolutionFontSetActivity.this, Integer.valueOf(ph));
        } else {
            SystemCfg.setTextSize(ResolutionFontSetActivity.this, Integer.parseInt(result));
            textFontSize.setText(result);
        }
        ToastApp.showToast(R.string.settingsuccess);
    }
}
