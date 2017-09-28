package com.xiangxun.sampling.widget.groupview;

import android.content.Context;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.widget.clearedit.ClearEditText;


/**
 * @TODO：自定义详情页面键值对组合控件
 */
public class DetailView extends LinearLayout {
    private Context mContext;
    private TextView name, value;
    private ClearEditText edit;
    private LinearLayout lin;

    private boolean flag;


    public DetailView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.group_view_detail, this, true);

        name = (TextView) findViewById(R.id.id_view_dt_name);
        value = (TextView) findViewById(R.id.id_view_dt_value);
        edit = (ClearEditText) findViewById(R.id.id_view_dt_edit);
        lin = (LinearLayout) findViewById(R.id.id_view_details);
    }

    public TextView getValue() {
        return value;
    }

    /**
     * @param flag true 代表输入框，false 代表展示框。
     */
    public void isEdit(boolean flag) {
        this.flag = flag;
        if (flag) {
            value.setVisibility(GONE);
            edit.setVisibility(VISIBLE);
        } else {
            value.setVisibility(VISIBLE);
            edit.setVisibility(GONE);
        }
    }

    public void setName(int titls) {
        name.setText(titls);
    }

    public void setName(String titls) {
        name.setText(titls);
    }

    public void setInfo(String tital, String content, String hit) {
        if (TextUtils.isEmpty(tital)) {
            lin.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(content)) {
                lin.setVisibility(VISIBLE);
                name.setText(tital);
            } else {
                lin.setVisibility(VISIBLE);
                name.setText(tital);
                if (flag) {
                    if (TextUtils.isEmpty(hit)) {
                        edit.setText(content);
                    } else {
                        SpannableString ss = new SpannableString(hit);//定义hint的值
                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);//设置字体大小 true表示单位是sp
                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        edit.setHint(new SpannedString(ss));
                    }
                } else {
                    if (TextUtils.isEmpty(hit)) {
                        value.setText(content);
                    } else {
                        SpannableString ss = new SpannableString(hit);//定义hint的值
                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);//设置字体大小 true表示单位是sp
                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        value.setHint(new SpannedString(ss));
                    }
                }
            }
        }
    }

    public void setInfo(int tital, String content, String hit) {
        if (tital == 0) {
            lin.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(content)) {
                lin.setVisibility(VISIBLE);
                name.setText(tital);
            } else {
                lin.setVisibility(VISIBLE);
                name.setText(tital);
                if (flag) {
                    if (TextUtils.isEmpty(hit)) {
                        edit.setText(content);
                    } else {
                        SpannableString ss = new SpannableString(hit);//定义hint的值
                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);//设置字体大小 true表示单位是sp
                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        edit.setHint(new SpannedString(ss));
                    }
                } else {
                    if (TextUtils.isEmpty(hit)) {
                        value.setText(content);
                    } else {
                        SpannableString ss = new SpannableString(hit);//定义hint的值
                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);//设置字体大小 true表示单位是sp
                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        value.setHint(new SpannedString(ss));
                    }
                }
            }
        }
    }

    public void setInfo(String tital, int content, String hit) {

        if (TextUtils.isEmpty(tital)) {
            lin.setVisibility(View.GONE);
        } else {
            if (content == 0) {
                lin.setVisibility(VISIBLE);
                name.setText(tital);
            } else {
                lin.setVisibility(VISIBLE);
                name.setText(tital);
                if (flag) {
                    if (TextUtils.isEmpty(hit)) {
                        edit.setText(content);
                    } else {
                        SpannableString ss = new SpannableString(hit);//定义hint的值
                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);//设置字体大小 true表示单位是sp
                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        edit.setHint(new SpannedString(ss));
                    }
                } else {
                    if (TextUtils.isEmpty(hit)) {
                        value.setText(content);
                    } else {
                        SpannableString ss = new SpannableString(hit);//定义hint的值
                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);//设置字体大小 true表示单位是sp
                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        value.setHint(new SpannedString(ss));
                    }
                }
            }
        }
    }

    public void setInfo(int tital, int content, String hit) {

        if (tital == 0) {
            lin.setVisibility(View.GONE);
        } else {
            if (content == 0) {
                lin.setVisibility(VISIBLE);
                name.setText(tital);
            } else {
                lin.setVisibility(VISIBLE);
                name.setText(tital);
                if (flag) {
                    if (TextUtils.isEmpty(hit)) {
                        edit.setText(content);
                    } else {
                        SpannableString ss = new SpannableString(hit);//定义hint的值
                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);//设置字体大小 true表示单位是sp
                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        edit.setHint(new SpannedString(ss));
                    }
                } else {
                    if (TextUtils.isEmpty(hit)) {
                        value.setText(content);
                    } else {
                        SpannableString ss = new SpannableString(hit);//定义hint的值
                        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);//设置字体大小 true表示单位是sp
                        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        value.setHint(new SpannedString(ss));
                    }
                }
            }
        }
    }


    public void setEditTextInputMode(int type) {
        edit.setInputType(type);
    }

    public void setEditTextMaxLin(int len) {
        edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(len)});
    }

    public String getText() {
        if (flag){
            if (edit ==null){
                return null;
            }else {
                return edit.getText().toString().trim();
            }
        }else {
            if (value ==null){
                return null;
            }else {
                return value.getText().toString().trim();
            }
        }
    }

    public void setEditText(String text) {
        if (TextUtils.isEmpty(text)) {
            edit.setText(null);
        } else {
            edit.setText(text);
        }
    }

    public String getHint(){
        if (value ==null){
            return null;
        }
        return value.getHint().toString().trim();
    }

    public String getName(){
        if (name ==null){
            return null;
        }
        return name.getText().toString().trim();
    }
}
