package com.xiangxun.sampling.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author zhangyh2 a 上午11:07:02 TODO
 */
public class WholeListView extends ListView {

	public WholeListView(Context context) {
		// TODO Auto-generated method stub
		super(context);
	}

	public WholeListView(Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		super(context, attrs);
	}

	public WholeListView(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated method stub
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}