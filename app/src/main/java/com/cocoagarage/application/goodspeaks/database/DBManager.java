package com.cocoagarage.application.goodspeaks.database;

import android.content.Context;

import com.cocoagarage.application.goodspeaks.Models.LeadershipRole;
import com.cocoagarage.application.goodspeaks.Models.PracticeSpeech;
import com.cocoagarage.application.goodspeaks.Models.SpeechProject;
import com.cocoagarage.application.goodspeaks.Models.LeadershipProject;

import java.util.ArrayList;

public class DBManager {
    private static DBManager shardInstance = null;

    private GoodspeaksDbHelper dbHelper = null;

    public GoodspeaksDbHelper getDbHelper() {
        return dbHelper;
    }
    public static synchronized DBManager sharedManager(Context context) {
        if (shardInstance == null) {
            shardInstance = new DBManager(context);
        }
        return shardInstance;
    }

    private DBManager(Context context) {
        dbHelper = new GoodspeaksDbHelper(context);
    }

    // Speech projects.
    public ArrayList<SpeechProject> getAllSpeechProjects() {
        return dbHelper.getAllSpeechProjects();
    }
    public int updateSpeechProject(SpeechProject updatedSpeechProject) {
        return dbHelper.updateSpeechProject(updatedSpeechProject);
    }

    public ArrayList<LeadershipProject> getAllLeadershipProjects() {
        return dbHelper.getAllLeadershipProjects();
    }
    public LeadershipRole getLeadershipRole(String roleId) {
        return dbHelper.getLeadershipRole(roleId);
    }
    public LeadershipProject getLeadershipProjectForID(String _ID) {
        return dbHelper.getLeadershipProjectForId(_ID);
    }
    public int updateLeadershipRole(LeadershipRole updatedLeadershipRole) {
        return dbHelper.updateLeadershipRole(updatedLeadershipRole);
    }

    public int updateCompletionDate(SpeechProject updatedSpeechProject) {
        return dbHelper.updateCompletionDate(updatedSpeechProject);
    }
    public ArrayList<PracticeSpeech>getPracticeSpeeches(String speechProjectId) {
        return dbHelper.getPracticeSpeeches(speechProjectId);
    }

    public long insertPracticeSpeech(PracticeSpeech practiceSpeech) {
        return dbHelper.insertPracticeSpeech(practiceSpeech, dbHelper.getWritableDatabase());
    }

    public int updatePracticeSpeech(PracticeSpeech newPracticeSpech) {
        return dbHelper.updatePracticeSpeech(newPracticeSpech);
    }

    public int deletePracticeSpeech(PracticeSpeech practiceSpeech) {
        return dbHelper.deletePracticeSpeech(practiceSpeech);
    }
}
