package com.surfergraphy.surf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.surfergraphy.surf.R;
import com.surfergraphy.surf.base.BaseType;
import com.surfergraphy.surf.base.data.Nation;

import java.util.ArrayList;

public class NavExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Nation> surfingSpots;

    public NavExpandableListAdapter(Context context, ArrayList<Nation> surfingSpots) {
        this.context = context;
        this.surfingSpots = surfingSpots;
    }

    @Override
    public int getGroupCount() {
        return surfingSpots.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return surfingSpots.get(groupPosition).getSurfingSpots().size();
    }

    @Override
    public Nation getGroup(int groupPosition) {
        return surfingSpots.get(groupPosition);
    }

    @Override
    public BaseType.LocationType getChild(int groupPosition, int childPosition) {
        return surfingSpots.get(groupPosition).getSurfingSpots().get(childPosition);
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
        View view;
        if (convertView == null) {
            view = getParentGenericView();
        } else {
            view = convertView;
        }
        ImageView icon = view.findViewById(R.id.icon);
        TextView text = view.findViewById(R.id.text_view_spot);
        Glide.with(context).load(surfingSpots.get(groupPosition).getNationLocationType().getDrawableId()).into(icon);
        text.setText(surfingSpots.get(groupPosition).getNationLocationType().getName());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = getChildGenericView();
        } else {
            view = convertView;
        }

        TextView text = view.findViewById(R.id.text_view_spot);
        text.setText(surfingSpots.get(groupPosition).getSurfingSpots().get(childPosition).getName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //Child의 View의 XML을 생성
    public View getChildGenericView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_child_menu, null);
        return view;
    }

    //Parent(Group)의 View의 XML을 생성
    public View getParentGenericView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_parent_menu, null);
        return view;
    }
}
