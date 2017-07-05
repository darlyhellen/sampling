package com.xiangxun.sampling.fun;

import android.view.View;
import android.widget.ListView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.widget.header.TitleView;


/**
 * Created by Administrator on 2017/2/21.
 */

public abstract class BaseFunctionList extends BaseFunFragment {
    private Function[] functions;

    private String Title;
    private ListView functionListView;
    private FunctionListAdapter adapter;

    @Override
    public void initView(View mParentView) {
        functionListView = (ListView) mParentView.findViewById(R.id.lv_functions);
        adapter = new FunctionListAdapter(getActivity(), functionListView);
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
    }


    public void setFunctions(Function[] functions) {
        this.functions = functions;
        adapter.setData(functions);
        functionListView.setAdapter(adapter);
    }

    public void setShowArrow(boolean value) {
        adapter.setShowArrow(value);
    }

    public ListView getFunctionListView() {
        return functionListView;
    }

    public FunctionListAdapter getAdapter() {
        return adapter;
    }

    public void load() {
    }
}
