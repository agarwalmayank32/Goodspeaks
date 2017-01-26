package com.cocoagarage.application.goodspeaks.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocoagarage.application.goodspeaks.Data.DataManager;
import com.cocoagarage.application.goodspeaks.Models.LeadershipProject;
import com.cocoagarage.application.goodspeaks.Models.LeadershipRole;
import com.cocoagarage.application.goodspeaks.R;
import com.cocoagarage.application.goodspeaks.Utility;

import java.util.ArrayList;

public class CompetentLeadershipListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<LeadershipProject> mLeadershiProjects;
    private ExpandableListView mListview;

    public CompetentLeadershipListAdapter(Context context, ExpandableListView listView) {
        this.mContext = context;
        mLeadershiProjects = DataManager.sharedManager().getLeadershipProjects(context);
    }

    @Override
    public int getGroupCount() {
        return mLeadershiProjects.size();
    }

    @Override
    public int getChildrenCount(int var1) {
        LeadershipProject project = DataManager.sharedManager().getLeadershipProject(mContext, mLeadershiProjects.get(var1).get_ID());
        if (mLeadershiProjects.get(var1).getRoles() != null) {
          return project.getRoles().size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int var1) {
        return mLeadershiProjects.get(var1);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        LeadershipProject project = DataManager.sharedManager().getLeadershipProject(mContext, mLeadershiProjects.get(groupPosition).get_ID());
        return project.getRoles().get(childPosition);
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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_cl, null);
        }
        TextView header = (TextView)convertView.findViewById(R.id.list_header_cl);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.Indicator);
        if (!isExpanded) {
            imageView.setImageResource(R.drawable.down);
        } else {
            imageView.setImageResource(R.drawable.up);
        }
        header.setText(mLeadershiProjects.get(groupPosition).getTitle());
     return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LeadershipProject project = DataManager.sharedManager().getLeadershipProject(mContext, mLeadershiProjects.get(groupPosition).get_ID());
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_cl_item, null);
        }
        CheckedTextView header = (CheckedTextView) convertView.findViewById(R.id.checkedTextView);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.textView4);
        LeadershipRole role = project.getRoles().get(childPosition);
        header.setText(role.getRoleName());

        if (role.getCompleted()) {
            header.setChecked(true);
        } else {
            header.setChecked(false);
        }
        if (role.getCompletionDate() != null) {
            dateTextView.setText(Utility.convertDateToString(role.getCompletionDate()));
        } else {
            dateTextView.setText("");
        }
        convertView.setClickable(false);
        return convertView;
    }
    @Override
    public boolean isChildSelectable(int var1, int var2) {
        return true;
    }
}
