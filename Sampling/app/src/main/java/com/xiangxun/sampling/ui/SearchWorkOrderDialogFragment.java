package com.xiangxun.sampling.ui;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.widget.header.TitleView;


/**
 * Created by Zhangyuhui/Darly on 2017/5/24.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:搜索条件列表窗口。
 */
public class SearchWorkOrderDialogFragment extends DialogFragment implements View.OnClickListener {


    public interface SearchListener {
        void findParamers(String statu, String name, String num, String ip);
    }

    private View view;
    private TitleView titleView;
    private EditText name;
    private EditText num;
    private EditText ip;

    //重写onCreateView方法
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.fragment_dialog_search, container);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        window.setLayout(SystemCfg.getWidth(getActivity()), SystemCfg.getHeight(getActivity()));//这2行,和上面的一样,注意顺序就行;
    }

    private void initView() {
        titleView = (TitleView) view.findViewById(R.id.id_search_header);
        name = (EditText) view.findViewById(R.id.id_search_name);
        num = (EditText) view.findViewById(R.id.id_search_num);
    }

    private void initListener() {
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getActivity().onBackPressed();
            }
        });

        titleView.setRightViewLeftOneListener(R.mipmap.ic_title_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击获取筛选结果

            }
        });
    }

    @Override
    public void onClick(View v) {

    }

}