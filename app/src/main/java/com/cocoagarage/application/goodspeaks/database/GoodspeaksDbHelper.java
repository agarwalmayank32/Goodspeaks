package com.cocoagarage.application.goodspeaks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cocoagarage.application.goodspeaks.Models.LeadershipRole;
import com.cocoagarage.application.goodspeaks.Models.PracticeSpeech;
import com.cocoagarage.application.goodspeaks.Models.SpeechProject;
import com.cocoagarage.application.goodspeaks.Data.DataManager;
import com.cocoagarage.application.goodspeaks.Models.LeadershipProject;
import com.cocoagarage.application.goodspeaks.Utility;

import java.util.ArrayList;

public class GoodspeaksDbHelper extends SQLiteOpenHelper {

    private Context appContext = null;
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "goodspeaks.db";

    public GoodspeaksDbHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
        appContext = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("test", "OnCreate is called.");
        final String SQL_CREATE_CC_TABLE = "CREATE TABLE " + GoodspeaksContract.CCEntry.TABLE_NAME + " (" +
                GoodspeaksContract.CCEntry._ID + " INTEGER PRIMARY KEY, " +
                GoodspeaksContract.CCEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                GoodspeaksContract.CCEntry.COLUMN_SPEECH_TITLE  + " TEXT, " +
                GoodspeaksContract.CCEntry.COLUMN_COMPLETION_DATE + " TEXT, " +
                GoodspeaksContract.CCEntry.COLUMN_EVAUATIONS + " TEXT, " +
                GoodspeaksContract.CCEntry.COLUMN_COMPLETED + " INTEGER DEFAULT 0," +
                GoodspeaksContract.CCEntry.COLUMN_MINIMUM_TIME + " INTEGER DEFAULT 0" +
                " );";

        Log.d("test", SQL_CREATE_CC_TABLE);

        final String  SQL_CREATE_PRACTICE_SPEECHES_TABLE = "CREATE TABLE " + GoodspeaksContract.PracticeSpeachesEntry.TABLE_NAME + " (" +
                GoodspeaksContract.PracticeSpeachesEntry._ID + " INTEGER PRIMARY KEY, "  +
                GoodspeaksContract.PracticeSpeachesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                GoodspeaksContract.PracticeSpeachesEntry.COLUMN_PATH + " TEXT NOT NULL, " +
                GoodspeaksContract.PracticeSpeachesEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                GoodspeaksContract.PracticeSpeachesEntry.COUMN_FK + " INTEGER NOT NULL, " +
                " FOREIGN KEY ("+GoodspeaksContract.PracticeSpeachesEntry.COUMN_FK +") REFERENCES "+
                GoodspeaksContract.CCEntry.TABLE_NAME +"("+ GoodspeaksContract.CCEntry._ID +
                " ));";

        final String SQL_CREATE_CL_TABLE = "CREATE TABLE " + GoodspeaksContract.CLEntry.TABLE_NAME + " (" +
                GoodspeaksContract.CLEntry._ID + " INTEGER PRIMARY KEY, " +
                GoodspeaksContract.CLEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                GoodspeaksContract.CLEntry.COLUMN_ROLES + " TEXT NOT NULL " +
                " );";
        final String SQL_CREATE_LEADERSHIP_ROLES_TABLE = "CREATE TABLE " + GoodspeaksContract.LeadershipRolesEntry.TABLE_NAME + " (" +
                GoodspeaksContract.LeadershipRolesEntry._ID + " INTEGER PRIMARY KEY, " +
                GoodspeaksContract.LeadershipRolesEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                GoodspeaksContract.LeadershipRolesEntry.COLUMN_COMPLETED + " INTEGER DEFAULT 0," +
                GoodspeaksContract.LeadershipRolesEntry.COLUMN_COMPLETION_DATE + " TEXT" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_CC_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PRACTICE_SPEECHES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CL_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_LEADERSHIP_ROLES_TABLE);
        Log.d("test", "All the tables are created.");

        loadDefaultData(appContext, sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.

        // TODO: Yet to be implemented
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GoodspeaksContract.CCEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GoodspeaksContract.PracticeSpeachesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GoodspeaksContract.CLEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GoodspeaksContract.LeadershipRolesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void loadDefaultData(Context appContext, SQLiteDatabase db) {
        DataManager.sharedManager().loadDefaultData(appContext, this, db);
    }

    // DB helper methods.

    public long insertSpeechProject(SpeechProject speechProject, SQLiteDatabase db) {
        // insert our test records into the database
       // SQLiteDatabase db = db;
        if (db == null) {
            db = this.getWritableDatabase();
        }
        ContentValues contentValues = speechProject.getContentValues();
        return db.insert(GoodspeaksContract.CCEntry.TABLE_NAME, null, contentValues);
    }
    public long insertLeadershipRole(LeadershipRole leadershipRole, SQLiteDatabase db) {
     //   SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            db = this.getWritableDatabase();
        }
        ContentValues contentValues = leadershipRole.getContentValues();
        return db.insert(GoodspeaksContract.LeadershipRolesEntry.TABLE_NAME, null, contentValues);
    }
    public long insertLeadershipProject(LeadershipProject leadershipProject, SQLiteDatabase db) {
       // SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            db = this.getWritableDatabase();
        }
        ContentValues contentValues = leadershipProject.getContentValues(db);
        return db.insert(GoodspeaksContract.CLEntry.TABLE_NAME, null, contentValues);
    }

    // Query methods.
    public ArrayList<SpeechProject> getAllSpeechProjects() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SpeechProject> speechProjects = new ArrayList<>();
        Cursor cursor = db.query(GoodspeaksContract.CCEntry.TABLE_NAME,null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues map = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, map);
                speechProjects.add(SpeechProject.getObjectFromContentValues(map));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return speechProjects;
    }

    public SpeechProject getSpeechProject(String _ID) {
        SpeechProject project = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(GoodspeaksContract.CCEntry.TABLE_NAME, null, GoodspeaksContract.CCEntry._ID + "=?", new String[]{_ID}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues map = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, map);
                project = SpeechProject.getObjectFromContentValues(map);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return project;
    }

    public ArrayList<LeadershipProject> getAllLeadershipProjects() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<LeadershipProject> leadershipProjects = new ArrayList<>();
        Cursor cursor = db.query(GoodspeaksContract.CLEntry.TABLE_NAME,null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues map = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, map);
                leadershipProjects.add(LeadershipProject.getObjectFromContentValues(map, appContext, true));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return leadershipProjects;
    }

    public String getLeadershipRoleId(String roleName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(GoodspeaksContract.LeadershipRolesEntry.TABLE_NAME,new String[]{GoodspeaksContract.LeadershipRolesEntry._ID},null,null,null,null,null);
        cursor.moveToFirst();
        String _id = cursor.getString(0);
        cursor.close();
        return _id;
    }

    public LeadershipRole getLeadershipRole(String roleId) {
        LeadershipRole role = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(GoodspeaksContract.LeadershipRolesEntry.TABLE_NAME, null, GoodspeaksContract.LeadershipRolesEntry._ID + "=?", new String[]{roleId}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues map = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, map);
                 role = LeadershipRole.getLeadershipRoleObjectFromContentValues(map);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return role;
    }

    public LeadershipProject getLeadershipProjectForId(String _ID) {
        LeadershipProject leadershipProject = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(GoodspeaksContract.CLEntry.TABLE_NAME,null,GoodspeaksContract.CLEntry._ID + "=?",new String[]{_ID},null,null,null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues map = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, map);
               leadershipProject = LeadershipProject.getObjectFromContentValues(map, appContext, false);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return leadershipProject;
    }

    // Update methods.
    public int updateSpeechProject(SpeechProject updatedSpeechProject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = updatedSpeechProject.getContentValues();
        int rowsUpdated = db.update(GoodspeaksContract.CCEntry.TABLE_NAME, contentValues, GoodspeaksContract.CCEntry._ID + "=?", new String[]{updatedSpeechProject.get_ID()});
        SpeechProject speechProject = getSpeechProject(updatedSpeechProject.get_ID());
        return rowsUpdated;
    }

    public int updateCompletionDate(SpeechProject updatedSpeechProject) {
        SQLiteDatabase db = this.getWritableDatabase();
      //ContentValues contentValues = updatedSpeechProject.getContentValues();
        ContentValues cv = new ContentValues();
        cv.put(GoodspeaksContract.CCEntry.COLUMN_COMPLETION_DATE, Utility.convertDateToString(updatedSpeechProject.getCompletionDate()));
        int rowsUpdated = db.update(GoodspeaksContract.CCEntry.TABLE_NAME, cv, GoodspeaksContract.CCEntry._ID + "=?", new String[]{updatedSpeechProject.get_ID()});
        SpeechProject speechProject = getSpeechProject(updatedSpeechProject.get_ID());
        return rowsUpdated;
    }


    public int updateLeadershipRole(LeadershipRole updatedLeadershipRole) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = updatedLeadershipRole.getContentValues();
        return db.update(GoodspeaksContract.LeadershipRolesEntry.TABLE_NAME, contentValues, GoodspeaksContract.LeadershipRolesEntry._ID + "=?", new String[]{updatedLeadershipRole.get_ID()});
    }


    public ArrayList<PracticeSpeech>getPracticeSpeeches(String speechProjectId) {
        LeadershipRole role;
        ArrayList<PracticeSpeech> practiceSpeeches = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(GoodspeaksContract.PracticeSpeachesEntry.TABLE_NAME, null, GoodspeaksContract.PracticeSpeachesEntry.COUMN_FK + "=?", new String[]{speechProjectId}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues map = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, map);
                PracticeSpeech practiceSpeech = PracticeSpeech.getObjectFromContentValues(map);
                practiceSpeeches.add(practiceSpeech);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return practiceSpeeches;
    }

    public long insertPracticeSpeech(PracticeSpeech practiceSpeech, SQLiteDatabase db) {
        if (db == null) {
            db = this.getWritableDatabase();
        }
        ContentValues contentValues = practiceSpeech.getContentValues();
        return db.insert(GoodspeaksContract.PracticeSpeachesEntry.TABLE_NAME, null, contentValues);
    }

    public int updatePracticeSpeech(PracticeSpeech newPracticeSpech) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = newPracticeSpech.getContentValues();
        return db.update(GoodspeaksContract.PracticeSpeachesEntry.TABLE_NAME, contentValues, GoodspeaksContract.PracticeSpeachesEntry._ID + "=?", new String[]{newPracticeSpech.get_ID()});
    }

    public int deletePracticeSpeech(PracticeSpeech practiceSpeech) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(GoodspeaksContract.PracticeSpeachesEntry.TABLE_NAME, GoodspeaksContract.PracticeSpeachesEntry._ID + "=?", new String[]{practiceSpeech.get_ID()});
    }

    // TODO: Will add these functions if needed.

//    public int updateSpeechTitle(SpeechProject speechProject, String newTitle) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(GoodspeaksContract.CCEntry.COLUMN_SPEECH_TITLE, newTitle);
//        int rowsUpdated = db.update(GoodspeaksContract.CCEntry.TABLE_NAME, contentValues, GoodspeaksContract.CLEntry._ID + "=?", new String[]{speechProject.get_ID()});
//       return rowsUpdated;
//    }
//
//    public int updateSpeechDate(SpeechProject speechProject, String newDate) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(GoodspeaksContract.CCEntry.COLUMN_COMPLETION_DATE, newDate);
//        int rowsUpdated = db.update(GoodspeaksContract.CCEntry.TABLE_NAME, contentValues, GoodspeaksContract.CLEntry._ID + "=?", new String[]{speechProject.get_ID()});
//        return rowsUpdated;
//    }
//
//    public int updateCompletionStatus(SpeechProject speechProject, Boolean newCompletionStatus) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(GoodspeaksContract.CCEntry.COLUMN_COMPLETED, (newCompletionStatus == true) ? 1 : 0);
//        int rowsUpdated = db.update(GoodspeaksContract.CCEntry.TABLE_NAME, contentValues, GoodspeaksContract.CLEntry._ID + "=?", new String[]{speechProject.get_ID()});
//        return rowsUpdated;
//    }
//    public int addEvaluationToSpeechProject(SpeechProject speechProject, String evaluation) {
//        ArrayList<String> currentEvaluations = speechProject.getEvaluations();
//        currentEvaluations.add(evaluation);
//        return 0;
//    }





}
