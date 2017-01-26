package com.cocoagarage.application.goodspeaks.Models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.cocoagarage.application.goodspeaks.database.GoodspeaksContract;



public class PracticeSpeech extends BaseModel implements Parcelable {

    private String speechTitle;
    private String path;
    private String date = null;
    private String speechProjectId;

    public String getSpeechProjectId() {
        return speechProjectId;
    }

    public void setSpeechProjectId(String speechProjectId) {
        this.speechProjectId = speechProjectId;
    }
    public PracticeSpeech(String speechTitle)
    {
        this.speechTitle=speechTitle;
    }

    public String getSpeechTitle()
    {
        return speechTitle;
    }

    public void setSpeechTitle(String speechTitle)
    {
        this.speechTitle = speechTitle;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }


    @Override
    public String toString() {
        return "Speech Title : " + speechTitle + ", Path : " + path;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GoodspeaksContract.PracticeSpeachesEntry.COLUMN_TITLE, this.getSpeechTitle());
        contentValues.put(GoodspeaksContract.PracticeSpeachesEntry.COLUMN_PATH,this.getPath());
        if (this.date != null) {
            contentValues.put(GoodspeaksContract.PracticeSpeachesEntry.COLUMN_DATE,this.getDate());
        }
        contentValues.put(GoodspeaksContract.PracticeSpeachesEntry.COUMN_FK, this.speechProjectId);
        return contentValues;
    }

    public static PracticeSpeech getObjectFromContentValues(ContentValues contentValues) {
        String title = contentValues.getAsString(GoodspeaksContract.PracticeSpeachesEntry.COLUMN_TITLE);
        String _ID = contentValues.getAsString(GoodspeaksContract.PracticeSpeachesEntry._ID);
        String path = contentValues.getAsString(GoodspeaksContract.PracticeSpeachesEntry.COLUMN_PATH);
        String speechProjectId = contentValues.getAsString(GoodspeaksContract.PracticeSpeachesEntry.COUMN_FK);

        PracticeSpeech practiceSpeech = new PracticeSpeech(title);
        practiceSpeech.setPath(path);
        practiceSpeech.setDate(contentValues.getAsString(GoodspeaksContract.PracticeSpeachesEntry.COLUMN_DATE));
        practiceSpeech.set_ID(_ID);
        practiceSpeech.setSpeechProjectId(speechProjectId);
        return practiceSpeech;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(speechTitle);
        out.writeValue(path);
        out.writeValue(date);
    }

    public static final Parcelable.Creator<PracticeSpeech> CREATOR = new Parcelable.Creator<PracticeSpeech>() {
        public PracticeSpeech createFromParcel(Parcel in) {
            return new PracticeSpeech(in);
        }

        public PracticeSpeech[] newArray(int size) {
            return new PracticeSpeech[size];
        }
    };

    private PracticeSpeech(Parcel in) {
        _ID = in.readString();
        speechTitle = in.readString();
        path = in.readString();
        date = (String) in.readValue(getClass().getClassLoader());
    }
}
