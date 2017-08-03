package com.xiangxun.sampling.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseFragment;
import com.xiangxun.sampling.bean.Index;
import com.xiangxun.sampling.binder.InitBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.ui.adapter.GridViewAdapter;
import com.xiangxun.sampling.ui.main.SamplingExPageActivity;
import com.xiangxun.sampling.ui.main.SamplingHisExceptionActivity;
import com.xiangxun.sampling.ui.main.SamplingHistoryActivity;
import com.xiangxun.sampling.ui.main.SamplingPlanningActivity;
import com.xiangxun.sampling.ui.main.SamplingSenceActivity;
import com.xiangxun.sampling.ui.main.SamplingTargetActivity;
import com.xiangxun.sampling.widget.listview.WholeGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyh2 MainActivity 下午3:27:48 TODO
 */
public class MainIndexFragment extends BaseFragment implements OnItemClickListener {

    private String TAG = getClass().getSimpleName();
    public View rootView;

    @ViewsBinder(R.id.id_fragment_index_grid)
    private WholeGridView gridView;
    private GridViewAdapter adapter;
    private List<Index> data;

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        DLog.d(getClass().getSimpleName(), "onCreateView()");
        rootView = inflater.inflate(R.layout.fragment_main_index, container, false);
        InitBinder.InitAll(this, rootView);
        return rootView;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hellen.base.BaseActivity#initView(android.os.Bundle)
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        adapter = new GridViewAdapter(null, R.layout.item_fragment_index, getActivity());
        gridView.setAdapter(adapter);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hellen.base.BaseActivity#loadData()
     */
    @Override
    protected void loadData() {
        // TODO Auto-generated metdhod stub
        if (data == null) {
            data = new ArrayList<Index>();
            data.add(new Index(0, "采样方案", R.drawable.grid_selecter_simp));
            data.add(new Index(1, "现场采样", R.drawable.grid_selecter_sence));
            data.add(new Index(2, "指标查询", R.drawable.grid_selecter_target));
            data.add(new Index(3, "历史采样", R.drawable.grid_selecter_his));
            data.add(new Index(4, "地块异常", R.drawable.grid_selecter_exc));
            data.add(new Index(5, "历史异常", R.drawable.grid_selecter_exchis));
        }
        adapter.setData(data);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hellen.base.BaseActivity#initListener()
     */
    @Override
    protected void initListener() {
        // TODO Auto-generated method stub
        gridView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Index index = (Index) parent.getItemAtPosition(position);
        if (index != null) {
            Intent intent = new Intent();
            switch (index.getId()) {
                case 0:
                    intent.setClass(getActivity(), SamplingPlanningActivity.class);
                    break;
                case 1:
                    intent.setClass(getActivity(), SamplingSenceActivity.class);
                    break;
                case 2:
                    intent.setClass(getActivity(), SamplingTargetActivity.class);
                    break;
                case 3:
                    intent.setClass(getActivity(), SamplingHistoryActivity.class);
                    break;
                case 4:
                    intent.setClass(getActivity(), SamplingExPageActivity.class);
                    break;
                case 5:
                    intent.setClass(getActivity(), SamplingHisExceptionActivity.class);
                    break;
                default:
                    break;
            }
            startActivity(intent);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DLog.d(getClass().getSimpleName(), "onViewCreated()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DLog.d(getClass().getSimpleName(), "onCreate()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DLog.d(getClass().getSimpleName(), "onDestroyView()");
    }


    @Override
    public void onResume() {
        DLog.d(getClass().getSimpleName(), "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        DLog.d(getClass().getSimpleName(), "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        DLog.d(getClass().getSimpleName(), "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        DLog.d(getClass().getSimpleName(), "onDestroy()");
        super.onDestroy();
    }

}
