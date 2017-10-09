package com.xiangxun.sampling.ui;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
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
    private String cla;

    //指标查询

    private LinearLayout targetSearch;
    private EditText name;
    private Switch switchs;
    private LinearLayout tagerClick;
    private TextView tager;
    private String over;
    //历史信息
    private LinearLayout hiSearch;
    private EditText hisName;
    private LinearLayout samplyNameClick;
    private TextView samplyName;

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
        cla = getArguments().getString("CLASS");
        targetSearch = (LinearLayout) view.findViewById(R.id.id_target);
        name = (EditText) view.findViewById(R.id.id_target_name);
        switchs = (Switch) view.findViewById(R.id.id_target_switch);
        tagerClick = (LinearLayout) view.findViewById(R.id.id_target_targ_click);
        tager = (TextView) view.findViewById(R.id.id_target_targ);


        hiSearch = (LinearLayout) view.findViewById(R.id.id_his);
        hisName = (EditText) view.findViewById(R.id.id_his_name);
        samplyNameClick = (LinearLayout) view.findViewById(R.id.id_his_samplyname_click);
        samplyName = (TextView) view.findViewById(R.id.id_his_samplyname);
        if ("SamplingTargetActivity".equals(cla)) {
            //展现切换开关
            targetSearch.setVisibility(View.VISIBLE);
            hiSearch.setVisibility(View.GONE);
            name.setText(getArguments().getString("SampleName"));

            over = getArguments().getString("Over");
            String targets = getArguments().getString("Target");
            if (TextUtils.isEmpty(targets)) {
                tager.setText(null);
            } else {
                if ("ph".endsWith(targets)) {
                    tager.setText("PH值");
                }
                if ("cadmium".endsWith(targets)) {
                    tager.setText("镉");
                }
                if ("availabl".endsWith(targets)) {
                    tager.setText("有效态镉");
                }
            }
            if (TextUtils.isEmpty(over)) {
                //未超标
                over = "0";
                switchs.setChecked(false);
            } else if ("0".equals(over)) {
                switchs.setChecked(false);
            } else {
                switchs.setChecked(true);
                over = "1";
            }
        }
        if ("SamplingHistoryActivity".equals(cla)) {
            //进入历史信息搜索
            targetSearch.setVisibility(View.GONE);
            hiSearch.setVisibility(View.VISIBLE);
            hisName.setText(getArguments().getString("hisName"));
            samplyName.setText(getArguments().getString("samplyName"));
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
                dismiss();
            }
        });

        titleView.setRightViewRightTextOneListener("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击获取筛选结果

                if ("SamplingTargetActivity".equals(cla)) {
                    //展现切换开关
                    String sampleName = name.getText().toString().trim();
                    String target = tager.getText().toString().trim();
                    if (TextUtils.isEmpty(target)) {
                        ((SearchListener) getActivity()).findParamers(sampleName, "", over);
                    }
                    if ("PH值".equals(target)) {
                        ((SearchListener) getActivity()).findParamers(sampleName, "ph", over);
                    }
                    if ("镉".equals(target)) {
                        ((SearchListener) getActivity()).findParamers(sampleName, "cadmium", over);
                    }
                    if ("有效态镉".equals(target)) {
                        ((SearchListener) getActivity()).findParamers(sampleName, "availabl", over);
                    }
                }
                if ("SamplingHistoryActivity".equals(cla)) {
                    //进入历史信息搜索
                    String hisname = hisName.getText().toString().trim();
                    String sam = samplyName.getText().toString().trim();
                    ((SearchListener) getActivity()).findParamers(hisname, sam, null);

                }

                dismiss();
            }
        });

        tagerClick.setOnClickListener(this);
        samplyNameClick.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_target_targ_click:
                //点击获取下拉列表.
                SelectItemDialog dialog = new SelectItemDialog(getActivity(), new String[]{"PH值", "镉", "有效态镉"}, "请选择指标");
                dialog.setSelectResultItemClick(this);
                dialog.show();
                break;
            case R.id.id_his_samplyname_click:
                //点击获取下拉列表.
                SelectItemDialog samply = new SelectItemDialog(getActivity(), new String[]{"全部","背景土壤", "农作物", "水样底泥","大气沉降", "肥料", "农田土壤"}, "请选择类型");
                samply.setSelectResultItemClick(this);
                samply.show();
                break;
        }
    }


    @Override
    public void resultOnClick(String result, String title) {
        if ("请选择指标".equals(title)) {
            tager.setText(result);
        }
        if ("请选择类型".equals(title)){
            samplyName.setText(result);
        }
    }

}