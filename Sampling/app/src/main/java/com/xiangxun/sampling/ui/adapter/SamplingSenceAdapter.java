package com.xiangxun.sampling.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.SamplingSenceGroup;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.ui.main.ChaoTuActivity;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/9/25.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 展示现场采样二级列表
 * 在外层Expand中，他的所有二级条目都是一个，为什么，因为他具体的显示都交给了子ExpandableListView，二级条目的目的是为了把子ExpandableListView显示出来。
 */
public class SamplingSenceAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<SamplingSenceGroup.SenceGroup> groupData;

    public SamplingSenceAdapter(Context context, List<SamplingSenceGroup.SenceGroup> groupData) {
        this.context = context;
        this.groupData = groupData;
    }


    public void setData(List<SamplingSenceGroup.SenceGroup> groupData) {
        this.groupData = groupData;
        this.notifyDataSetInvalidated();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        int ret = 0;
        if (groupData != null) {
            ret = groupData.size();
        }
        return ret;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int ret = 0;
        if (groupData != null && groupData.get(groupPosition).result != null) {
            ret = groupData.get(groupPosition).result.size();
        }
        return ret;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupData.get(groupPosition).result.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group_expandablelistview, null);
            holder = new GroupViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.id_item_equip_iv);
            holder.tv_name = (TextView) convertView.findViewById(R.id.id_item_equip_name);
            holder.tv_num = (TextView) convertView.findViewById(R.id.id_item_equip_num);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        SamplingSenceGroup.SenceGroup groupData = this.groupData.get(groupPosition);
        holder.tv_name.setText(groupData.regType);


        if (groupData.regType.equals("水样底泥")){
            if (!Api.ISNEWVERSION) {
                holder.tv_num.setText("暂未开通");
                holder.tv_num.setTextColor(context.getResources().getColor(R.color.color999999));
            }else {
                if (groupData.shemeNum == 0){
                    holder.tv_num.setText("任务"+groupData.shemeNum+"个");
                    holder.tv_num.setTextColor(context.getResources().getColor(R.color.black));
                }else {
                    holder.tv_num.setText("任务"+groupData.shemeNum+"个");
                    holder.tv_num.setTextColor(context.getResources().getColor(R.color.red));
                }
            }
            holder.img.setImageResource(R.mipmap.dbssymbol1);
            if(!isExpanded){
                holder.img.setImageResource(R.mipmap.dbssymbol);
            }
        }else if (groupData.regType.equals("大气沉降")){
            if (groupData.shemeNum == 0){
                holder.tv_num.setText("任务"+groupData.shemeNum+"个");
                holder.tv_num.setTextColor(context.getResources().getColor(R.color.black));
            }else {
                holder.tv_num.setText("任务"+groupData.shemeNum+"个");
                holder.tv_num.setTextColor(context.getResources().getColor(R.color.red));
            }
            holder.img.setImageResource(R.mipmap.dqcjsymbol1);
            if(!isExpanded){
                holder.img.setImageResource(R.mipmap.dqcjsymbol);
            }
         }else if (groupData.regType.equals("农作物")){
            if (groupData.shemeNum == 0){
                holder.tv_num.setText("任务"+groupData.shemeNum+"个");
                holder.tv_num.setTextColor(context.getResources().getColor(R.color.black));
            }else {
                holder.tv_num.setText("任务"+groupData.shemeNum+"个");
                holder.tv_num.setTextColor(context.getResources().getColor(R.color.red));
            }
            holder.img.setImageResource(R.mipmap.nzwsymbol1);
            if(!isExpanded){
                holder.img.setImageResource(R.mipmap.nzwsymbol);
            }
        }else  if (groupData.regType.equals("农田土壤")){
            if (groupData.shemeNum == 0){
                holder.tv_num.setText("任务"+groupData.shemeNum+"个");
                holder.tv_num.setTextColor(context.getResources().getColor(R.color.black));
            }else {
                holder.tv_num.setText("任务"+groupData.shemeNum+"个");
                holder.tv_num.setTextColor(context.getResources().getColor(R.color.red));
            }
            holder.img.setImageResource(R.mipmap.nttrsymbol1);
            if(!isExpanded){
                holder.img.setImageResource(R.mipmap.nttrsymbol);
            }
        }else if (groupData.regType.equals("肥料")){
            if (!Api.ISNEWVERSION) {
                holder.tv_num.setText("暂未开通");
                holder.tv_num.setTextColor(context.getResources().getColor(R.color.color999999));
            }else {
                if (groupData.shemeNum == 0){
                    holder.tv_num.setText("任务"+groupData.shemeNum+"个");
                    holder.tv_num.setTextColor(context.getResources().getColor(R.color.black));
                }else {
                    holder.tv_num.setText("任务"+groupData.shemeNum+"个");
                    holder.tv_num.setTextColor(context.getResources().getColor(R.color.red));
                }
            }
            holder.img.setImageResource(R.mipmap.flsymbol1);
            if(!isExpanded){
                holder.img.setImageResource(R.mipmap.flsymbol);
            }
        }else if (groupData.regType.equals("背景土壤")){
            if (!Api.ISNEWVERSION) {
                holder.tv_num.setText("暂未开通");
                holder.tv_num.setTextColor(context.getResources().getColor(R.color.color999999));
            }else {
                if (groupData.shemeNum == 0){
                    holder.tv_num.setText("任务"+groupData.shemeNum+"个");
                    holder.tv_num.setTextColor(context.getResources().getColor(R.color.black));
                }else {
                    holder.tv_num.setText("任务"+groupData.shemeNum+"个");
                    holder.tv_num.setTextColor(context.getResources().getColor(R.color.red));
                }
            }
            holder.img.setImageResource(R.mipmap.bjtrsymbol1);
            if(!isExpanded){
                holder.img.setImageResource(R.mipmap.bjtrsymbol);
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        ChildViewHolder hocker = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_child_expandablelistview, parent, false);
            hocker = new ChildViewHolder();
            hocker.name = (TextView) view.findViewById(R.id.id_item_planning_name);
            hocker.dept = (TextView) view.findViewById(R.id.id_item_planning_dept);
            hocker.position = (TextView) view.findViewById(R.id.id_item_planning_place);
            hocker.iv = (ImageView) view.findViewById(R.id.id_item_planning_other);
            hocker.bg = (LinearLayout) view.findViewById(R.id.id_item_planning_linear);
            view.setTag(hocker);
        } else {
            hocker = (ChildViewHolder) view.getTag();
        }
        PlannningData.Scheme tag = (PlannningData.Scheme) SharePreferHelp.getValue("SenceActivity");
        final PlannningData.Scheme s = groupData.get(groupPosition).result.get(childPosition);
        if (tag != null&&tag.code.equals(s.code)){
            hocker.bg.setBackgroundColor(context.getResources().getColor(R.color.index_line));
        }else {
            hocker.bg.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        hocker.name.setText(s.code);
        hocker.name.setTextColor(context.getResources().getColor(R.color.color999999));
        hocker.name.setTextSize(14);
        hocker.dept.setText(s.missionName);
        hocker.dept.setTextColor(context.getResources().getColor(R.color.color999999));
        hocker.dept.setTextSize(14);
        hocker.position.setText(String.valueOf(s.regNum+"/"+s.quantity));
        hocker.position.setTextColor(context.getResources().getColor(R.color.color999999));
        hocker.position.setTextSize(14);
        hocker.iv.setImageResource(R.mipmap.ic_sence_location);
        hocker.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChaoTuActivity.class);
                intent.putExtra("isSence",true);
                intent.putExtra("Scheme", s);
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    class GroupViewHolder {
        ImageView img;
        TextView tv_name, tv_num;
    }

    class ChildViewHolder {
        LinearLayout bg;
        TextView name;
        TextView dept;
        TextView position;
        TextView desc;
        ImageView iv;
    }
}
