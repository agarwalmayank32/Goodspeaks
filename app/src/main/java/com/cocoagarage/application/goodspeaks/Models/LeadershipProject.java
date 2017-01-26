package com.cocoagarage.application.goodspeaks.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.cocoagarage.application.goodspeaks.database.DBManager;
import com.cocoagarage.application.goodspeaks.database.GoodspeaksContract;

import java.util.ArrayList;
import java.util.Arrays;

public class LeadershipProject extends  BaseModel  {

    private String mTitle;

    public String getRolesString() {
        return rolesString;
    }

    public void setRolesString(String rolesString) {
        this.rolesString = rolesString;
    }

    private String rolesString;
    private ArrayList<LeadershipRole> roles = new ArrayList<>();

    public LeadershipProject(String title) {
        super();
        mTitle = title;
    }

    public String getTitle() { return mTitle; }

    public ArrayList<LeadershipRole> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<LeadershipRole> roles) {
        this.roles = roles;
    }

    public void addRole(LeadershipRole role) {
        roles.add(role);
    }

    @Override
    public String toString() {
        return "leadership Title : " + mTitle + "," + "roles : " + roles;
    }

    public ContentValues getContentValues(SQLiteDatabase db) {
        // Create a new map of values, where column names are the keys
        ArrayList<LeadershipRole> roles = this.getRoles();
        ArrayList<String> roleIds = new ArrayList<>();
        for (LeadershipRole role : roles) {
            Cursor cursor = db.query(GoodspeaksContract.LeadershipRolesEntry.TABLE_NAME, new String[]{GoodspeaksContract.LeadershipRolesEntry._ID}, GoodspeaksContract.LeadershipRolesEntry.COLUMN_TITLE + "=?", new String[]{role.getRoleName()}, null, null, null);
            cursor.moveToFirst();
            String _id = cursor.getString(0);
            roleIds.add(_id);
            cursor.close();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(GoodspeaksContract.CLEntry.COLUMN_TITLE, this.getTitle());
        contentValues.put(GoodspeaksContract.CLEntry.COLUMN_ROLES, TextUtils.join(";", roleIds));
        return contentValues;
    }

    public static LeadershipProject getObjectFromContentValues(ContentValues contentValues, Context appContext, Boolean skipRoles) {
        String title = contentValues.getAsString(GoodspeaksContract.CLEntry.COLUMN_TITLE);
        String roleIDs = contentValues.getAsString(GoodspeaksContract.CLEntry.COLUMN_ROLES);
        String _ID = contentValues.getAsString(GoodspeaksContract.CLEntry._ID);
        if (skipRoles) {
            LeadershipProject leadershipProject = new LeadershipProject(title);
            leadershipProject.set_ID(_ID);
            leadershipProject.setRolesString(roleIDs);
            return leadershipProject;
        } else {
            ArrayList<String>  roleIdsArray = null;
            if (roleIDs!= null) {
                roleIdsArray = new ArrayList<>(Arrays.asList(TextUtils.split(roleIDs, ";")));
            }
            ArrayList<LeadershipRole> roles = new ArrayList<>();
            if (roleIdsArray != null) {
                for (String roleId : roleIdsArray) {
                    LeadershipRole role = DBManager.sharedManager(appContext).getLeadershipRole(roleId);
                    roles.add(role);
                }
            }
            LeadershipProject leadershipProject = new LeadershipProject(title);
            leadershipProject.set_ID(_ID);
            leadershipProject.roles = roles;
            return leadershipProject;

        }
    }

}
