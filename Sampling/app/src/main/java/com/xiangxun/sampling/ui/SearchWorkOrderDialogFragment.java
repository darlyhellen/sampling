package com.xiangxun.sampling.ui;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableRow;

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
public class SearchWorkOrderDialogFragment extends DialogFragment {


    public interface SearchListener {
        void findParamers(String sampleName, String target, String over);
    }

    private View view;
    private TitleView titleView;
    private EditText name;
    private EditText num;
    private Switch switchs;

    private SearchListener listener;

    private String over;


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
        boolean switche = getArguments().getBoolean("SWITCH");
        titleView = (TitleView) view.findViewById(R.id.id_search_header);
        titleView.setTitle("查询");
        name = (EditText) view.findViewById(R.id.id_search_name);
        num = (EditText) view.findViewById(R.id.id_search_num);
        switchs = (Switch) view.findViewById(R.id.id_search_switch);
        TableRow sw = (TableRow) view.findViewById(R.id.id_search_switch_show);
        if (switche) {
            //展现切换开关
            sw.setVisibility(View.VISIBLE);
            name.setText(getArguments().getString("SampleName"));
            num.setText(getArguments().getString("Target"));
            over = getArguments().getString("Over");
            if ("0".equals(over)) {
                //未超标
                switchs.setChecked(false);
            } else {
                switchs.setChecked(true);
                over = "1";
            }
        } else {
            sw.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        switchs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    over = "1";//Over open;
                } else {
                    over = "0";
                }
            }
        });


        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getActivity().onBackPressed();
            }
        });

        titleView.setRightViewRightTextOneListener("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击获取筛选结果
                String sampleName = name.getText().toString().trim();
                String target = num.getText().toString().trim();
                ((SearchListener) getActivity()).findParamers(sampleName, target, over);
                dismiss();
            }
        });
    }
}