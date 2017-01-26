package com.cocoagarage.application.goodspeaks.Adapters;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cocoagarage.application.goodspeaks.Data.DataManager;
import com.cocoagarage.application.goodspeaks.Models.SpeechProject;
import com.cocoagarage.application.goodspeaks.R;
import com.cocoagarage.application.goodspeaks.Utility;

import java.util.ArrayList;


public class CompetentCommunicationAdapter extends BaseAdapter {

    private final Activity context;
    private ArrayList<SpeechProject> speechProjects;

    private TextView textView_bottombar;
    private TextView textView_circle;
    private TextView textView_Name;
    private TextView textView_Title;
    private TextView textView_Date;
    private TextView textView_topbar;

    public CompetentCommunicationAdapter(Activity context) {
        super();
        this.context = context;
        this.speechProjects = DataManager.sharedManager().getSpeechProjects(context);
    }

    @Override
    public Object getItem(int i) {
        return speechProjects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return speechProjects.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView= inflater.inflate(R.layout.single_cc, null, true);

        textView_topbar = (TextView) rowView.findViewById(R.id.textView_topbar);
        textView_bottombar = (TextView)rowView.findViewById(R.id.textView_bottombar);
        textView_circle = (TextView)rowView.findViewById(R.id.textView_circle);
        textView_Name = (TextView)rowView.findViewById(R.id.textView_name);
        textView_Title = (TextView)rowView.findViewById(R.id.textView_title);
        textView_Date = (TextView)rowView.findViewById(R.id.textView_date);


        SpeechProject speechProject = speechProjects.get(position);
        textView_Name.setText(speechProject.getProjectTitle());
        textView_Title.setText(speechProject.getSpeechTitle());
        if (speechProject.getCompletionDate() != null) {
            textView_Date.setText(Utility.convertDateToString(speechProject.getCompletionDate()));
        } else {
            textView_Date.setText("NA");
        }

        if(position==0) {
            textView_topbar.setVisibility(View.INVISIBLE);
        }
        if(position==(speechProjects.size()-1)) {
            textView_bottombar.setVisibility(View.INVISIBLE);
        }

        if (speechProject.getCompleted()) {
            // TODO: Check this condition.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                textView_circle.setBackground(context.getResources().getDrawable(R.drawable.tick));
            }
        } else {
            textView_circle.setText(String.valueOf(position+1));
        }
        return rowView;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        speechProjects = DataManager.sharedManager().getSpeechProjects(context);
    }
}