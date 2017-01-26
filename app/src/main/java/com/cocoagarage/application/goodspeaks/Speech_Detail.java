package com.cocoagarage.application.goodspeaks;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.cocoagarage.application.goodspeaks.Adapters.EvaluationAdapter;
import com.cocoagarage.application.goodspeaks.Models.PracticeSpeech;
import com.cocoagarage.application.goodspeaks.Models.SpeechProject;
import com.cocoagarage.application.goodspeaks.Adapters.PracticeSpeechAdapter;
import com.cocoagarage.application.goodspeaks.Data.DataManager;
import com.cocoagarage.application.goodspeaks.R;
import com.cocoagarage.application.goodspeaks.database.DBManager;

import java.util.ArrayList;
import java.util.Calendar;

public class Speech_Detail extends AppCompatActivity {


    String[] Practice_Speech_Title,Practice_Speech_Date;
    ArrayList<String> Evaluation_text= new ArrayList<>();
    PracticeSpeechAdapter ps_adapter;
    EvaluationAdapter evaluationsAdapter;

    TextView textViewTitle,textViewDate;
    Calendar myCalendar;
    Switch switch_completed;
    ListView evaluations_list,ps_list;

    SpeechProject speechProject = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        speechProject = getIntent().getParcelableExtra("SpeechProject");

        setTitle(speechProject.getProjectTitle());

        textViewTitle = (TextView)findViewById(R.id.textViewTitle);
        textViewDate = (TextView)findViewById(R.id.textViewDate);
        switch_completed = (Switch)findViewById(R.id.switch_completed);
        ps_list = (ListView)findViewById(R.id.listview_recordings);
        evaluations_list = (ListView)findViewById(R.id.listview_evaluations);

        /*Practice_Speech_Title = new String[2];
        Practice_Speech_Date = new String[2];
        Practice_Speech_Title[0] = "Practice Speech 1";
        Practice_Speech_Date[0] = "Date";
        Practice_Speech_Title[1] = "Practice Speech 2";
        Practice_Speech_Date[1] = "Date";
        */

        setPracticeSpeechDetail();


      //  Evaluation_text.add("Your evaluations will be displayed here");
        if (speechProject.getEvaluations() != null) {
            Evaluation_text.addAll(speechProject.getEvaluations());
        } else {
            Evaluation_text.add(getText(R.string.evaluation_enter_hint).toString());
        }
        evaluationsAdapter = new EvaluationAdapter(this, Evaluation_text);
        evaluations_list.setAdapter(evaluationsAdapter);
        evaluations_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClickEvaluationDialogModify(i);
            }
        });
        setListViewHeightBasedOnChildren(evaluations_list);
        initialiseView();

        switch_completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchValueChanged(b);
            }
        });
    }

    public void setPracticeSpeechDetail()
    {
        int count = DBManager.sharedManager(Speech_Detail.this).getPracticeSpeeches(speechProject.get_ID()).size();

        Practice_Speech_Title = new String[count];
        Practice_Speech_Date = new String[count];
        final String[] Practice_Speech_Path = new String[count];

        for(int i=0;i<count;i++)
        {
            PracticeSpeech practiceSpeech = DBManager.sharedManager(Speech_Detail.this).getPracticeSpeeches(speechProject.get_ID()).get(i);
            Practice_Speech_Title[i] = practiceSpeech.getSpeechTitle();
            Practice_Speech_Date[i] = practiceSpeech.getDate();
            Practice_Speech_Path[i] = practiceSpeech.getPath();
        }

        ps_adapter = new PracticeSpeechAdapter(this,Practice_Speech_Title,Practice_Speech_Date);
        ps_list.setAdapter(ps_adapter);
        setListViewHeightBasedOnChildren(ps_list);
        ps_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Speech_Detail.this,RecordedSpeech.class);
                intent.putExtra("Speech_Project_ID",speechProject.get_ID());
                intent.putExtra("Practice_Speech_Title",Practice_Speech_Title[i]);
                intent.putExtra("Practice_Speech_Path",Practice_Speech_Path[i]);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setPracticeSpeechDetail();
        ps_adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickTitleDialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_view);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        TextView heading = (TextView)dialog.findViewById(R.id.DialogHeading);
        heading.setText(R.string.enter_title_heading);

        final EditText text = (EditText)dialog.findViewById(R.id.DialogText);
        if (! speechProject.getSpeechTitle().equalsIgnoreCase(getString(R.string.title_sample))) {
            text.setText(speechProject.getSpeechTitle());
        } else {
            text.setHint(textViewTitle.getText());
        }

        Button dialogButton = (Button) dialog.findViewById(R.id.DialogButton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewTitle.setText(text.getText());
                String speechTitle = text.getText().toString();
                speechProject.setSpeechTitle(speechTitle);
                dialog.dismiss();
                saveSpeechProject(speechProject);
            }
        });
        dialog.show();
    }

    public void onClickDateDialog(View view) {

        int day,month,year;
        myCalendar = Calendar.getInstance();
        day = myCalendar.get(Calendar.DAY_OF_MONTH);
        month = myCalendar.get(Calendar.MONTH);
        year = myCalendar.get(Calendar.YEAR);


        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
            {
                textViewDate.setText(Utility.convertDateToString(Utility.convertToDate(year, monthOfYear+1, dayOfMonth)));
                speechProject.setCompletionDate(Utility.convertToDate(year, monthOfYear+1, dayOfMonth));
                saveSpeechProject(speechProject);
            }};

        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }


    public void switchValueChanged(Boolean isChecked) {
        speechProject.setCompleted(isChecked);
        saveSpeechProject(speechProject);
    }

    public void onClickCreatePractice(View view) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("speechObject",speechProject);
        Intent intent = new Intent(Speech_Detail.this,Practice_Speech.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onClickEvaluationDialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_view);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        TextView heading = (TextView)dialog.findViewById(R.id.DialogHeading);
        heading.setText(R.string.enter_evaluation_heading);

        final EditText text = (EditText)dialog.findViewById(R.id.DialogText);


        Button dialogButton = (Button) dialog.findViewById(R.id.DialogButton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Not valid text just return.
                if (text.getText().toString().equals("")) {
                    dialog.dismiss();
                    return;
                }
                // Valid text.
                if (Evaluation_text.get(0).equalsIgnoreCase(getString(R.string.evaluation_enter_hint))) {
                    Evaluation_text.clear();
                }
                Evaluation_text.add(text.getText().toString());
                evaluationsAdapter.notifyDataSetChanged();
                dialog.dismiss();
                setListViewHeightBasedOnChildren(evaluations_list);
                speechProject.addEvaluation(text.getText().toString());
                saveSpeechProject(speechProject);
            }
        });
        dialog.show();
    }

    public void onClickEvaluationDialogModify(final int i)
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_view);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        TextView heading = (TextView)dialog.findViewById(R.id.DialogHeading);
        heading.setText("Update Evaluation");

        final String currentEvaluationText = Evaluation_text.get(i);

        // No need to edit if hint evaluation is clicked.
        if (currentEvaluationText.equalsIgnoreCase(getString(R.string.evaluation_enter_hint))) {
            return;
        }


        final EditText text = (EditText)dialog.findViewById(R.id.DialogText);
        text.setText(currentEvaluationText);
        Button dialogButton = (Button) dialog.findViewById(R.id.DialogButton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Not valid text just return.
                if (text.getText().toString().equals("")) {
                    dialog.dismiss();
                    return;
                }
                Evaluation_text.set(i,text.getText().toString());
                evaluationsAdapter.notifyDataSetChanged();
                dialog.dismiss();
                setListViewHeightBasedOnChildren(evaluations_list);
                speechProject.addEvaluation(text.getText().toString());
                saveSpeechProject(speechProject);
            }
        });
        dialog.show();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            //int listViewWidth = listView.getWidth() - paddingleft - paddingright
            int listViewWidth = listView.getWidth()-32;
            int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);
            listItem.measure(widthSpec, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    void initialiseView() {
        textViewTitle.setText(speechProject.getSpeechTitle());
        if (speechProject.getCompletionDate() != null) {
            textViewDate.setText(Utility.convertDateToString(speechProject.getCompletionDate()));
        }
        switch_completed.setChecked(speechProject.getCompleted());
    }
    public void saveSpeechProject(SpeechProject updatedSpeechProject) {
        DataManager.sharedManager().updateSpeechProject(this, updatedSpeechProject);
    }
}
