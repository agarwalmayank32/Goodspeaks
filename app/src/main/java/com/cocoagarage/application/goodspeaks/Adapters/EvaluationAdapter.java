package com.cocoagarage.application.goodspeaks.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cocoagarage.application.goodspeaks.R;

import java.util.ArrayList;

public class EvaluationAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> Evaluation_title = new ArrayList<>();
    private TextView textView_evaluation_title;


    public EvaluationAdapter(Activity context, ArrayList<String> title) {
        super(context, R.layout.single_evaluation,title);
        this.context = context;
        this.Evaluation_title=title;
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView= inflater.inflate(R.layout.single_evaluation, null, true);

        textView_evaluation_title = (TextView) rowView.findViewById(R.id.Evaluation_Text);
        textView_evaluation_title.setText(Evaluation_title.get(position));
        return rowView;
    }
}
