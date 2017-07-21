package com.xiangxun.sampling.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.bean.SenceLandRegion;
import com.xiangxun.sampling.common.Tools;

import java.util.List;

/**
 * @TODO：现场采样选择地块和选择采样类型的选择器。
 */
public class SelectTypeRegionDialog extends Dialog {
    private Context mContext = null;
    private View mCustomView = null;
    private List<SenceLandRegion.LandRegion> clearItems;
    private String mTitle = null;
    private TextView mTvCancle = null;
    private TextView mTvPublishSelectTitle = null;
    private ListView mLvPublishTypes = null;
    private AffairsRegionTypeAdapter adapter;
    private SenceLandRegion.LandRegion result;

    public SelectTypeRegionDialog(Context context, List<SenceLandRegion.LandRegion> clearItems, String title) {
        super(context, R.style.PublishDialog);
        this.mContext = context;
        this.clearItems = clearItems;
        mTitle = title;
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mCustomView = inflater.inflate(R.layout.dial_publish_select_dialog, null);
        setContentView(mCustomView);
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.width = dm.widthPixels - Tools.dip2px(mContext, 5.0f);
        lp.height = LayoutParams.WRAP_CONTENT;
        initView();
    }

    @Override
    public void show() {
        super.show();
    }

    private void initView() {
        mTvCancle = (TextView) mCustomView.findViewById(R.id.tv_publish_select_dialog_cancle);
        mTvPublishSelectTitle = (TextView) mCustomView.findViewById(R.id.tv_publish_select_dialog_title);
        mTvPublishSelectTitle.setText(mTitle);
        mLvPublishTypes = (ListView) mCustomView.findViewById(R.id.lv_publish_select_dialog);
        adapter = new AffairsRegionTypeAdapter(mContext, clearItems);
        mLvPublishTypes.setAdapter(adapter);
        mLvPublishTypes.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                result = clearItems.get(0);
                if (selectResultItemClick != null) {
                    result = clearItems.get(position);
                    selectResultItemClick.resultOnClick(result, mTitle);
                }
                dismiss();
            }
        });

        mTvCancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public class AffairsRegionTypeAdapter extends BaseAdapter {
        private List<SenceLandRegion.LandRegion> clearItems = null;
        private LayoutInflater mInflater = null;

        public AffairsRegionTypeAdapter(Context context, List<SenceLandRegion.LandRegion> clearItems) {
            mInflater = LayoutInflater.from(context);
            this.clearItems = clearItems;
        }

        @Override
        public int getCount() {
            return clearItems.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.dial_publish_select_dialog_item, null);
                holder = new ViewHolder();
                holder.mTvStyleName = (TextView) convertView.findViewById(R.id.tv_publish_slect_dialog_string);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mTvStyleName.setText(clearItems.get(position).name + "");
            return convertView;
        }

        private class ViewHolder {
            private TextView mTvStyleName = null;
        }
    }

    private SelectResultItemClick selectResultItemClick;

    public void setSelectResultItemClick(SelectResultItemClick selectResultItemClick) {
        this.selectResultItemClick = selectResultItemClick;
    }

    public interface SelectResultItemClick {
        public void resultOnClick(SenceLandRegion.LandRegion result, String title);
    }

}
