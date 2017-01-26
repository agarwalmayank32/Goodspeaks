package com.cocoagarage.application.goodspeaks.Models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.cocoagarage.application.goodspeaks.Utility;
import com.cocoagarage.application.goodspeaks.database.GoodspeaksContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SpeechProject extends  BaseModel implements Parcelable {
    private String projectTitle;
    private String speechTitle = "Your speech title..";
    private Boolean completed = false;
    private Date completionDate= null;
    private ArrayList<String> evaluations;
    private Integer minimumTime = 0;

    private ArrayList<PracticeSpeech> practiceSpeeches;


    public ArrayList<PracticeSpeech> getPracticeSpeeches() {
        return practiceSpeeches;
    }

    public void setPracticeSpeeches(ArrayList<PracticeSpeech> practiceSpeeches) {
        this.practiceSpeeches = practiceSpeeches;
    }

    public Integer getMinimumTime() {
        return minimumTime;
    }

    public void setMinimumTime(Integer minimumTime) {
        this.minimumTime = minimumTime;
    }

    public ArrayList<String> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(ArrayList<String> evaluations) {
        this.evaluations = evaluations;
    }

    public void addEvaluation(String evaluation) {
        if (evaluation == null) {
            return;
        }
        if (evaluations == null) {
            evaluations = new ArrayList<>();
        }
        evaluations.add(evaluation);
    }

    //  private String[] speechObjectives;
    // TODO: practice speeches

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getSpeechTitle() {
        return speechTitle;
    }

    public void setSpeechTitle(String speechTitle) {
        this.speechTitle = speechTitle;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public  SpeechProject(String projectTitle) {
        this.projectTitle= projectTitle;
    }

    @Override
    public String toString() {
        return "Project Title : " + projectTitle + "," + "Speech Title : " + speechTitle;
    }

    public ContentValues getContentValues() {
        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(GoodspeaksContract.CCEntry.COLUMN_TITLE, this.getProjectTitle());
        contentValues.put(GoodspeaksContract.CCEntry.COLUMN_SPEECH_TITLE, this.getSpeechTitle());
        contentValues.put(GoodspeaksContract.CCEntry.COLUMN_COMPLETED, (this.completed) ? 1 : 0);
        contentValues.put(GoodspeaksContract.CCEntry.COLUMN_MINIMUM_TIME, this.getMinimumTime());
        if (this.evaluations != null) {
            contentValues.put(GoodspeaksContract.CCEntry.COLUMN_EVAUATIONS, TextUtils.join(";",this.evaluations));
        }
        if (this.completionDate != null) {
            contentValues.put(GoodspeaksContract.CCEntry.COLUMN_COMPLETION_DATE, Utility.convertDateToString(this.completionDate));
        }
        return contentValues;
    }

    public static SpeechProject getObjectFromContentValues(ContentValues contentValues) {
        String title = contentValues.getAsString(GoodspeaksContract.CCEntry.COLUMN_TITLE);
        String speechTitle = contentValues.getAsString(GoodspeaksContract.CCEntry.COLUMN_SPEECH_TITLE);
        Integer completed_int = contentValues.getAsInteger(GoodspeaksContract.CCEntry.COLUMN_COMPLETED);
        Boolean completed = (completed_int == 1);
        String evaluations = contentValues.getAsString(GoodspeaksContract.CCEntry.COLUMN_EVAUATIONS);
        Integer minimumTime = contentValues.getAsInteger(GoodspeaksContract.CCEntry.COLUMN_MINIMUM_TIME);
        String _ID = contentValues.getAsString(GoodspeaksContract.CCEntry._ID);
        ArrayList<String> evaluationsArray = null;
        if (evaluations != null) {
            evaluationsArray = new ArrayList<>(Arrays.asList(TextUtils.split(evaluations, ";")));
        }
        SpeechProject speechProject = new SpeechProject(title);
        Date completionDate = Utility.convertStringToDate(contentValues.getAsString(GoodspeaksContract.CCEntry.COLUMN_COMPLETION_DATE));
        speechProject.setSpeechTitle(speechTitle);
        speechProject.setCompleted(completed);
        speechProject.setEvaluations(evaluationsArray);
        speechProject.setCompletionDate(completionDate);
        speechProject.set_ID(_ID);
        speechProject.setMinimumTime(minimumTime);
        return speechProject;
    }

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(_ID);
        out.writeString(projectTitle);
        out.writeString(speechTitle);
        out.writeValue(completed);
        out.writeValue(completionDate);
        out.writeValue(evaluations);
        out.writeInt(minimumTime);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<SpeechProject> CREATOR = new Parcelable.Creator<SpeechProject>() {
        public SpeechProject createFromParcel(Parcel in) {
            return new SpeechProject(in);
        }

        public SpeechProject[] newArray(int size) {
            return new SpeechProject[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private SpeechProject(Parcel in) {
        _ID = in.readString();
        projectTitle = in.readString();
        speechTitle = in.readString();
        completed = (Boolean)in.readValue(getClass().getClassLoader());
        completionDate = (Date)in.readValue(getClass().getClassLoader());
        evaluations = (ArrayList<String>) in.readValue(getClass().getClassLoader());
        minimumTime = in.readInt();
    }
}