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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.widget.dialog.SelectItemDialog;
import com.xiangxun.sampling.widget.dialog.SelectItemDialog.SelectResultItemClick;
import com.xiangxun.sampling.widget.header.TitleView;


/**
 * Created by Zhangyuhui/Darly on 2017/5/24.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:搜索条件列表窗口。
 */
public class SearchWorkOrderDialogFragment extends DialogFragment implements View.OnClickListener, SelectResultItemClick {

    public interface SearchListener {
        void findParamers(String sampleName, String target, String over);
    }

    private View view;
    private TitleView titleView;

    //指标查询

    private LinearLayout targetSearch;
    private EditText name;
    private Switch switchs;
    private LinearLayout tagerClick;
    private TextView tager;
    private String over;


    private SearchListener listener;


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
        titleView.setTitle("查询");
        String cla = getArguments().getString("CLASS");
        targetSearch = (LinearLayout) view.findViewById(R.id.id_target);
        name = (EditText) view.findViewById(R.id.id_target_name);
        switchs = (Switch) view.findViewById(R.id.id_target_switch);
        tagerClick = (LinearLayout) view.findViewById(R.id.id_target_targ_click);
        tager = (TextView) view.findViewById(R.id.id_target_targ);
        if ("SamplingTargetActivity".equals(cla)) {
            //展现切换开关
            targetSearch.setVisibility(View.VISIBLE);
            name.setText(getArguments().getString("SampleName"));
            tager.setText(getArguments().getString("Target"));
            over = getArguments().getString("Over");
            if ("0".equals(over)) {
                //未超标
                switchs.setChecked(false);
            } else {
                switchs.setChecked(true);
                over = "1";
            }
        }
        if ("SamplingHistoryActivity".equals(cla)) {
            //进入历史信息搜索
            targetSearch.setVisibility(View.GONE);
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
                String target = tager.getText().toString().trim();
                ((SearchListener) getActivity()).findParamers(sampleName, target, over);
                dismiss();
            }
        });

        tagerClick.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_target_targ_click:
                //点击获取下拉列表.
                SelectItemDialog dialog = new SelectItemDialog(getActivity(), new String[]{"ph", "cadmium", "availabl"}, "请选择指标");
                dialog.setSelectResultItemClick(this);
                dialog.show();
                break;
        }
    }


    @Override
    public void resultOnClick(String result, String title) {
        tager.setText(result);
    }

}