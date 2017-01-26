package com.cocoagarage.application.goodspeaks.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.cocoagarage.application.goodspeaks.Models.LeadershipRole;
import com.cocoagarage.application.goodspeaks.Models.PracticeSpeech;
import com.cocoagarage.application.goodspeaks.Models.SpeechProject;
import com.cocoagarage.application.goodspeaks.database.DBManager;
import com.cocoagarage.application.goodspeaks.database.GoodspeaksDbHelper;
import com.cocoagarage.application.goodspeaks.Models.LeadershipProject;
import com.cocoagarage.application.goodspeaks.R;

import java.util.ArrayList;
import java.util.Arrays;

public class DataManager {
    private  ArrayList<SpeechProject> speechProjects = new ArrayList<>();
    private  ArrayList<LeadershipProject> leadershipProjects = new ArrayList<>();
    private ArrayList<LeadershipRole> leadershipRoles = new ArrayList<>();
    private GoodspeaksDbHelper dbHelper = null;


    public ArrayList<LeadershipRole> getLeadershipRoles() {
        return leadershipRoles;
    }

    public void setLeadershipProjects(ArrayList<LeadershipProject> leadershipProjects) {
        this.leadershipProjects = leadershipProjects;
    }


    private static class Holder {
        static final DataManager instance = new DataManager();
    }

    public static DataManager sharedManager() {
        return Holder.instance;
    }

    // Should be called on the first launch after installation.
    // Builds data and inserts it in db.
    public void loadDefaultData(Context appContext, GoodspeaksDbHelper dbHelper, SQLiteDatabase db) {
        ArrayList<LeadershipProject> leadershipProjs = DataManager.sharedManager().buildLeadershipProjects(appContext);
        ArrayList<SpeechProject> speechProjs =  DataManager.sharedManager().buildSpeechProjects(appContext);
        // Load the Speech Projects.
        long rowId = 1;
        for (SpeechProject speechProject : speechProjs) {
            rowId = dbHelper.insertSpeechProject(speechProject, db);
        }
        if (rowId != -1) {
            Log.d("test", "All speech projects insertd sucessfully");
        }

        rowId = 1;
        // Load all the roles.
        for (LeadershipRole role : DataManager.sharedManager().getLeadershipRoles()) {
            rowId = dbHelper.insertLeadershipRole(role, db);
        }
        if (rowId != -1) {
            Log.d("test", "All leadership roles inserted successfully");
        }

        rowId = 1;
//       // Load all the Leadership Projects.
        for (LeadershipProject leadershipProject : leadershipProjs) {
            rowId = dbHelper.insertLeadershipProject(leadershipProject, db);
        }
        if (rowId != -1) {
            Log.d("test", "All leadership projects inserted successfully");
        }
    }

    private  ArrayList<SpeechProject> buildSpeechProjects(Context appContext) {
        ArrayList<SpeechProject> speechProjs = new ArrayList<>();
        for (int i=1; i<=10; i++) {
            String title = appContext.getResources().getString(appContext.getResources().getIdentifier("cc_project"+i, "string", appContext.getPackageName()));
           SpeechProject speechProject = new SpeechProject(title);
            Integer minimumTime = appContext.getResources().getInteger(appContext.getResources().getIdentifier("cc_time_"+i, "integer", appContext.getPackageName()));
            speechProject.setMinimumTime(minimumTime);
            speechProjs.add(speechProject);
        }
        return speechProjs;
    }



    private  ArrayList<LeadershipProject> buildLeadershipProjects(Context appContext) {

        ArrayList<LeadershipProject> leadershipProjs = new ArrayList<>();

        LeadershipRole speechEvaluator = new LeadershipRole(appContext.getString(R.string.speech_evaluator));
        leadershipRoles.add(speechEvaluator);
        LeadershipRole ttSpeaker = new LeadershipRole(appContext.getString(R.string.tt_speaker));
        leadershipRoles.add(ttSpeaker);
        LeadershipRole ahCounter = new LeadershipRole(appContext.getString(R.string.ah_counter));
        leadershipRoles.add(ahCounter);
        LeadershipRole grammarian = new LeadershipRole(appContext.getString(R.string.grammarian));
        leadershipRoles.add(grammarian);
        LeadershipRole generalEvaluator = new LeadershipRole(appContext.getString(R.string.general_evaluator));
        leadershipRoles.add(generalEvaluator);
        LeadershipRole timer = new LeadershipRole(appContext.getString(R.string.timer));
        leadershipRoles.add(timer);
        LeadershipRole toastmaster = new LeadershipRole(appContext.getString(R.string.toastmaster));
        leadershipRoles.add(toastmaster);
        LeadershipRole speaker = new LeadershipRole(appContext.getString(R.string.speaker));
        leadershipRoles.add(speaker);
        LeadershipRole topicmaster = new LeadershipRole(appContext.getString(R.string.topicmaster));
        leadershipRoles.add(topicmaster);
        LeadershipRole organizeSpeechContest = new LeadershipRole(appContext.getString(R.string.organise_speech_contest));
        leadershipRoles.add(organizeSpeechContest);
        LeadershipRole organizeSpecialEvent = new LeadershipRole(appContext.getString(R.string.organise_special_event));
        leadershipRoles.add(organizeSpecialEvent);
        LeadershipRole organiseMembershipCampaign = new LeadershipRole(appContext.getString(R.string.organise_membership_campaign));
        leadershipRoles.add(organiseMembershipCampaign);
        LeadershipRole organisePrCampaign = new LeadershipRole(appContext.getString(R.string.organise_pr_campaign));
        leadershipRoles.add(organisePrCampaign);
        LeadershipRole produceClubNewsletter = new LeadershipRole(appContext.getString(R.string.produce_club_newsletter));
        leadershipRoles.add(produceClubNewsletter);
        LeadershipRole clubWebmaster = new LeadershipRole(appContext.getString(R.string.club_webmaster));
        leadershipRoles.add(clubWebmaster);
        LeadershipRole contestChair = new LeadershipRole(appContext.getString(R.string.contest_chair));
        leadershipRoles.add(contestChair);
        LeadershipRole membershipChair = new LeadershipRole(appContext.getString(R.string.membership_campaign_chair));
        leadershipRoles.add(membershipChair);
        LeadershipRole prChair = new LeadershipRole(appContext.getString(R.string.pr_chair));
        leadershipRoles.add(prChair);
        LeadershipRole eventChair = new LeadershipRole(appContext.getString(R.string.event_chair));
        leadershipRoles.add(eventChair);
        LeadershipRole newsleterChair = new LeadershipRole(appContext.getString(R.string.newsletter_chair));
        leadershipRoles.add(newsleterChair);
        LeadershipRole mentorNew = new LeadershipRole(appContext.getString(R.string.mentor_new));
        leadershipRoles.add(mentorNew);
        LeadershipRole mentorExisting = new LeadershipRole(appContext.getString(R.string.mentor_existing));
        leadershipRoles.add(mentorExisting);
        LeadershipRole hpl = new LeadershipRole(appContext.getString(R.string.hpl_guidance_committee_member));
        leadershipRoles.add(hpl);
        LeadershipRole assistWebmaster = new LeadershipRole(appContext.getString(R.string.assist_webmaster));
        leadershipRoles.add(assistWebmaster);
        LeadershipRole befriendGuest = new LeadershipRole(appContext.getString(R.string.befriend_guest));
        leadershipRoles.add(befriendGuest);

        LeadershipProject listening = new LeadershipProject(appContext.getString(R.string.cl_project1));
        listening.addRole(ahCounter);
        listening.addRole(speechEvaluator);
        listening.addRole(grammarian);
        listening.addRole(ttSpeaker);
        leadershipProjs.add(listening);

        LeadershipProject criticalThinking = new LeadershipProject(appContext.getString(R.string.cl_project2));
        criticalThinking.addRole(speechEvaluator);
        criticalThinking.addRole(grammarian);
        criticalThinking.addRole(generalEvaluator);
        leadershipProjs.add(criticalThinking);

        LeadershipProject givingFeedback = new LeadershipProject(appContext.getString(R.string.cl_project3));
        givingFeedback.addRole(speechEvaluator);
        givingFeedback.addRole(grammarian);
        givingFeedback.addRole(generalEvaluator);
        leadershipProjs.add(givingFeedback);

        LeadershipProject timeManagement = new LeadershipProject(appContext.getString(R.string.cl_project4));
        timeManagement.addRole(timer);
        timeManagement.addRole(toastmaster);
        timeManagement.addRole(speaker);
        timeManagement.addRole(topicmaster);
        timeManagement.addRole(grammarian);
        leadershipProjs.add(timeManagement);

        LeadershipProject planning = new LeadershipProject(appContext.getString(R.string.cl_project5));
        planning.addRole(speaker);
        planning.addRole(generalEvaluator);
        planning.addRole(toastmaster);
        planning.addRole(topicmaster);
        leadershipProjs.add(planning);

        LeadershipProject organizing = new LeadershipProject(appContext.getString(R.string.cl_project6));
        organizing.addRole(organizeSpeechContest);
        organizing.addRole(organizeSpecialEvent);
        organizing.addRole(organiseMembershipCampaign);
        organizing.addRole(organisePrCampaign);
        organizing.addRole(produceClubNewsletter);
        organizing.addRole(assistWebmaster);
        leadershipProjs.add(organizing);


        LeadershipProject facilitation = new LeadershipProject(appContext.getString(R.string.cl_project7));
        facilitation.addRole(toastmaster);
        facilitation.addRole(generalEvaluator);
        facilitation.addRole(topicmaster);
        facilitation.addRole(befriendGuest);
        leadershipProjs.add(facilitation);

        LeadershipProject motivation = new LeadershipProject(appContext.getString(R.string.cl_project8));
        motivation.addRole(contestChair);
        motivation.addRole(prChair);
        motivation.addRole(toastmaster);
        motivation.addRole(speechEvaluator);
        motivation.addRole(generalEvaluator);
        leadershipProjs.add(motivation);

        LeadershipProject mentoring = new LeadershipProject(appContext.getString(R.string.cl_project9));
        mentoring.addRole(mentorNew);
        mentoring.addRole(mentorExisting);
        mentoring.addRole(hpl);
        leadershipProjs.add(mentoring);

        LeadershipProject teamBuilding = new LeadershipProject(appContext.getString(R.string.cl_project10));
        teamBuilding.addRole(toastmaster);
        teamBuilding.addRole(generalEvaluator);
        teamBuilding.addRole(membershipChair);
        teamBuilding.addRole(contestChair);
        teamBuilding.addRole(eventChair);
        teamBuilding.addRole(newsleterChair);
        teamBuilding.addRole(clubWebmaster);
        leadershipProjs.add(teamBuilding);

        return leadershipProjs;
    }


    // Helper Methods.

    // Speech projects.
    public ArrayList<SpeechProject> getSpeechProjects(Context appContext) {
        // Get the data from db.
        return DBManager.sharedManager(appContext).getAllSpeechProjects();
    }
    public int updateSpeechProject(Context appContext, SpeechProject updatedSpeechProject) {
        return DBManager.sharedManager(appContext).updateSpeechProject(updatedSpeechProject);
    }

    public int updateCompletionDate(Context appContext, SpeechProject updatedSpeechProject) {
        return DBManager.sharedManager(appContext).updateCompletionDate(updatedSpeechProject);
    }

    // Doesn't give roles of the leadership projects.
    public ArrayList<LeadershipProject> getLeadershipProjects(Context appContext) {
        // Get the data from db.
        return DBManager.sharedManager(appContext).getAllLeadershipProjects();
    }

    // Get leadershipProject with the roles.
    public LeadershipProject getLeadershipProject(Context appContext, String _ID) {
        return DBManager.sharedManager(appContext).getLeadershipProjectForID(_ID);
    }

    public ArrayList<LeadershipRole> getLeadershipRoles(Context appContext, LeadershipProject leadershipProject) {
       ArrayList<String> roleIdsArray = null;
        String roleIDs = leadershipProject.getRolesString();
        if (roleIDs!= null) {
            roleIdsArray = new ArrayList<>(Arrays.asList(TextUtils.split(roleIDs, ";")));
        }
        ArrayList<LeadershipRole> roles = new ArrayList<>();
        for (String roleId : roleIdsArray) {
            LeadershipRole role = DBManager.sharedManager(appContext).getLeadershipRole(roleId);
            roles.add(role);
        }
        return roles;
    }
    public int updateLeadershipRole(Context appContext, LeadershipRole updatedLeadershipRole) {
        return DBManager.sharedManager(appContext).updateLeadershipRole(updatedLeadershipRole);
    }

    public int deletePracticeSpeech(Context appContext, PracticeSpeech practiceSpeech) {
        return DBManager.sharedManager(appContext).deletePracticeSpeech(practiceSpeech);
    }
}

