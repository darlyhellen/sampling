package com.xiangxun.sampling.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.xiangxun.sampling.common.ToastApp;


/**
 * Created by Zhangyuhui/Darly on 2017/7/19.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:
 */
public class MaxLengthWatcher implements TextWatcher {

    public interface MaxLengthUiListener {

        void onUiChanged(int num);
    }

    private int MAX_EN;// 最大英文/数字长度 一个汉字算两个字母
    private String regEx = "[\\u4e00-\\u9fa5]"; // unicode编码，判断是否为汉字

    private MaxLengthUiListener lengthUiListener;


    private EditText editText = null;

    public MaxLengthWatcher(int mAX_EN, EditText editText, MaxLengthUiListener lengthUiListener) {
        super();
        MAX_EN = mAX_EN;
        this.editText = editText;
        this.lengthUiListener = lengthUiListener;
    }


    private CharSequence temp;
    private boolean isEdit = true;
    private int selectionStart;
    private int selectionEnd;

    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

        selectionStart = editText.getSelectionStart();
        selectionEnd = editText.getSelectionEnd();
        if (temp.length() > MAX_EN) {
            ToastApp.showToast("输入文字长度不能大于" + MAX_EN);
            s.delete(selectionStart - 1, selectionEnd);
            int tempSelection = selectionStart;
            editText.setText(s);
            editText.setSelection(tempSelection);
            lengthUiListener.onUiChanged(MAX_EN);
        } else {
            lengthUiListener.onUiChanged(temp.length());
        }
    }

    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
        // TODO Auto-generated method stub
        temp = arg0;
    }

    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    private int getChineseCount(String str) {
        int count = 0;
        count = str.length();
//        Pattern p = Pattern.compile(regEx);
//        Matcher m = p.matcher(str);
//        while (m.find()) {
//            for (int i = 0; i <= m.groupCount(); i++) {
//                count = count + 1;
//            }
//        }
        return count;
    }
}
