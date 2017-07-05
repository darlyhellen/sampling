package com.xiangxun.sampling.widget.wheelview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.common.ConstantStatus;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/4.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 滑轮选择，日期等操作
 */
public class WheelHelper {


    public static void showDateTimePicker(Context context, final TextView publishVioTime) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        final Dialog dialog = new Dialog(context);
        dialog.setTitle(R.string.checkDate);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.time_layout, null);

        // year
        final WheelView wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(ConstantStatus.START_YEAR, ConstantStatus.END_YEAR));
        wv_year.setCyclic(true);// can be recycled
        wv_year.setLabel(context.getResources().getString(R.string.year));// add
        // text
        wv_year.setCurrentItem(year - ConstantStatus.START_YEAR);// init

        // month
        final WheelView wv_month = (WheelView) view.findViewById(R.id.month);
        wv_month.setAdapter(new NumericWheelAdapter(1, 12));
        wv_month.setCyclic(true);
        wv_month.setLabel(context.getResources().getString(R.string.month));
        wv_month.setCurrentItem(month);

        // day
        final WheelView wv_day = (WheelView) view.findViewById(R.id.day);
        wv_day.setCyclic(true);
        // set the day
        if (list_big.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
        } else if (list_little.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
        } else {

            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
            else
                wv_day.setAdapter(new NumericWheelAdapter(1, 28));
        }
        wv_day.setLabel(context.getResources().getString(R.string.date));
        wv_day.setCurrentItem(day - 1);

        final WheelView wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
        wv_hours.setCyclic(true);
        wv_hours.setCurrentItem(hour);

        final WheelView wv_mins = (WheelView) view.findViewById(R.id.mins);
        wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
        wv_mins.setCyclic(true);
        wv_mins.setCurrentItem(minute);

        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + ConstantStatus.START_YEAR;

                if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    else
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
        };

        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;

                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if (((wv_year.getCurrentItem() + ConstantStatus.START_YEAR) % 4 == 0 && (wv_year.getCurrentItem() + ConstantStatus.START_YEAR) % 100 != 0) || (wv_year.getCurrentItem() + ConstantStatus.START_YEAR) % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    else
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
        };
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);

        Button btn_sure = (Button) view.findViewById(R.id.btn_datetime_sure);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_datetime_cancel);
        // sure
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                StringBuffer sb = new StringBuffer();
                sb.append((wv_year.getCurrentItem() + ConstantStatus.START_YEAR));
                sb.append("-");
                if (wv_month.getCurrentItem() + 1 < 10)
                    sb.append("0");
                sb.append((wv_month.getCurrentItem() + 1));
                sb.append("-");
                if (wv_day.getCurrentItem() + 1 < 10)
                    sb.append("0");
                sb.append(wv_day.getCurrentItem() + 1);
                sb.append(" ");
                if (wv_hours.getCurrentItem() < 10)
                    sb.append("0");
                sb.append(wv_hours.getCurrentItem());
                sb.append(":");
                if (wv_mins.getCurrentItem() < 10)
                    sb.append("0");
                sb.append(wv_mins.getCurrentItem());
                sb.append(":00");

                publishVioTime.setText(sb.toString());

                dialog.dismiss();
            }
        });
        // cancel
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

}
