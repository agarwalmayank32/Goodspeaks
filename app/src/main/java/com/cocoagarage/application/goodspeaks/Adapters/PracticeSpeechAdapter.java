package com.cocoagarage.application.goodspeaks.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cocoagarage.application.goodspeaks.R;

public class PracticeSpeechAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private String[] Speech_Title,Speech_Date;

    private TextView textView_title;
    private TextView textView_date;


    public PracticeSpeechAdapter(Activity context, String[] title, String[] date) {
        super(context, R.layout.single_practice_speech,title);
        this.context = context;
        this.Speech_Title=title;
        this.Speech_Date=date;
    }

    @NonNull
    @Override
    public View getView(final int position, View view,@NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView= inflater.inflate(R.layout.single_practice_speech, null, true);

        textView_title = (TextView) rowView.findViewById(R.id.Speech_title);
        textView_date = (TextView) rowView.findViewById(R.id.Speech_Date);

        textView_title.setText(Speech_Title[position]);
        textView_date.setText(Speech_Date[position]);

        return rowView;
    }
}
