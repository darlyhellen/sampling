package com.xiangxun.sampling.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.xiangxun.sampling.R;

import java.util.Timer;


/**
 * @package: com.xiangxun.widget
 * @ClassName: UserCodeDialog.java
 * @Description: 自定义dialog
 * @author: 提供用户记录CODE的弹出框，并且间隔5秒才能进行点击操作。
 * @date: 2015-7-28 下午2:05:45
 */
public class UserCodeDialog extends Dialog {

	private Context mContext;
	private TextView mTvDiaTitle;
	private TextView mTvDiaMsg;
	private TextView mTvDiaMsg2;
	private TextView mTvDiaMsg3;
	private TextView mTvDiaBut1;
	private TextView mTvDiaBut2;
	private View mVerLine;

	public UserCodeDialog(Context context) {
		super(context, R.style.msg_dalog);
		mContext = context;
		init();
		initLinster();
		OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode== KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)	{
					return true;
				}else{
					return false;
				}
			}
		};
		setOnKeyListener(keylistener);
		setCancelable(false);
	}

	/**
	 * 
	 * @Title:
	 * @Description:
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void initLinster() {
		View.OnClickListener listener = new View.OnClickListener() {
			public void onClick(View v) {
				UserCodeDialog.this.dismiss();
			}
		};
		mTvDiaBut1.setOnClickListener(listener);
		mTvDiaBut2.setOnClickListener(listener);
	}

	/**
	 * 
	 * @Title:
	 * @Description:
	 * @param:
	 * @return: void
	 * @throws
	 */
	@SuppressLint("InflateParams")
	private void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.msg_dialog, null);
		mTvDiaTitle = (TextView) v.findViewById(R.id.dia_tv_title);
		mTvDiaMsg = (TextView) v.findViewById(R.id.dia_tv_msg);
		mTvDiaMsg2 = (TextView) v.findViewById(R.id.dia_tv_msg2);
		mTvDiaMsg3 = (TextView) v.findViewById(R.id.dia_tv_msg3);
		mTvDiaBut1 = (TextView) v.findViewById(R.id.dia_tv_but1);
		mTvDiaBut2 = (TextView) v.findViewById(R.id.dia_tv_but2);
		mVerLine = v.findViewById(R.id.view_ver_line);
		this.setContentView(v);
		this.setCanceledOnTouchOutside(false);
		// WindowManager m = ((Activity) mContext).getWindowManager();
		// Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高

		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		// p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
		p.width = (int) (dm.widthPixels * 0.80); // 宽度设置为屏幕的0.95

		getWindow().setAttributes(p); // 设置生效
	}

	/**
	 * 
	 * @Title:
	 * @Description:
	 * @param:
	 * @return: void
	 * @throws
	 */
	public void setMsg(CharSequence text) {
		mTvDiaMsg.setVisibility(View.VISIBLE);
		mTvDiaMsg.setText(text);
		mTvDiaMsg.setTextSize(24);
	}

	public void setMsg(int text) {
		mTvDiaMsg.setVisibility(View.VISIBLE);
		mTvDiaMsg.setText(text);
		mTvDiaMsg.setTextSize(24);
	}
	
	public void setMsgColor(int color) {
		mTvDiaMsg.setTextColor(color);
	}
	/**
	 * 
	 * @Title:
	 * @Description:
	 * @param:
	 * @return: void
	 * @throws
	 */
	public void setMsg2(CharSequence text) {
		mTvDiaMsg2.setVisibility(View.VISIBLE);
		mTvDiaMsg2.setText(text);
	}

	public void setMsg2(int text) {
		mTvDiaMsg2.setVisibility(View.VISIBLE);
		mTvDiaMsg2.setText(text);
	}

	/**
	 * 
	 * @Title:
	 * @Description:
	 * @param:
	 * @return: void
	 * @throws
	 */
	public void setMsg3(CharSequence text) {
		mTvDiaMsg3.setVisibility(View.VISIBLE);
		mTvDiaMsg3.setText(text);
	}

	/**
	 * 
	 * @Title:
	 * @Description:
	 * @param: @param text
	 * @return: void
	 * @throws
	 */
	public void setTiele(CharSequence text) {
		mTvDiaTitle.setText(text);
	}

	/**
	 * 
	 * @Title:
	 * @Description:
	 * @param: @param text
	 * @return: void
	 * @throws
	 */
	public void setButLeft(CharSequence text) {
		mTvDiaBut1.setText(text);
	}

	/**
	 * 
	 * @Title:
	 * @Description:
	 * @param: @param text
	 * @return: void
	 * @throws
	 */
	public void setButRight(CharSequence text) {
		mTvDiaBut2.setText(text);
	}

	/**
	 * 
	 * @Title:
	 * @Description:
	 * @param: @param text
	 * @return: void
	 * @throws
	 */
	public void setButRightColor(int color) {
		mTvDiaBut2.setTextColor(color);
	}

	/**
	 * 
	 * @Title:
	 * @Description: 按钮事件
	 * @param: @param listener
	 * @return: void
	 * @throws
	 */
	public void setButLeftListener(View.OnClickListener listener) {
		mTvDiaBut1.setOnClickListener(listener);
	}

	/**
	 * 
	 * @Title:
	 * @Description: 按钮事件
	 * @param: @param listener
	 * @return: void
	 * @throws
	 */
	public void setButRightListener(View.OnClickListener listener) {
		mTvDiaBut2.setOnClickListener(listener);
	}

	public void setOnlyOneBut() {
		mTvDiaBut1.setVisibility(View.GONE);
		mTvDiaBut2.setBackgroundResource(R.drawable.hit_selector_one);
		mVerLine.setVisibility(View.GONE);
	}

	@Override
	public void show() {
		try {
			if (mContext == null || ((Activity) mContext).isFinishing()) {
				return;
			}
			super.show();
			handler.sendEmptyMessage(12);
		} catch (Exception e) {
		}
	}
	private int time = 5;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 12:
					mTvDiaBut2.setText("确认(" + time + ")");
					mTvDiaBut2.setClickable(false);
					if (time>0) {
						handler.sendEmptyMessageDelayed(12, 1000);
						time--;
					}else {
						handler.sendEmptyMessage(11);
					}
					break;
				case 11:
					mTvDiaBut2.setText("确认");
					mTvDiaBut2.setClickable(true);
					break;
			}
		}
	};
}
