package com.cocoagarage.application.goodspeaks.Models;

import android.content.ContentValues;

import com.cocoagarage.application.goodspeaks.Utility;
import com.cocoagarage.application.goodspeaks.database.GoodspeaksContract;

import java.util.Date;

public class LeadershipRole extends  BaseModel {

    private String name;

    private Date completionDate= null;
    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    private Boolean completed = false;

    public LeadershipRole(String name) {
        this.name = name;
    }

    public String getRoleName()
    {
        return name;
    }

    public ContentValues getContentValues() {
        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(GoodspeaksContract.LeadershipRolesEntry.COLUMN_TITLE, this.name);
        contentValues.put(GoodspeaksContract.CCEntry.COLUMN_COMPLETED, (this.completed == true) ? 1 : 0);
        contentValues.put(GoodspeaksContract.LeadershipRolesEntry.COLUMN_COMPLETION_DATE, Utility.convertDateToString(this.completionDate));
        return contentValues;
    }


    public static LeadershipRole getLeadershipRoleObjectFromContentValues(ContentValues contentValues) {
        String title = contentValues.getAsString(GoodspeaksContract.LeadershipRolesEntry.COLUMN_TITLE);
        Integer completed_int = contentValues.getAsInteger(GoodspeaksContract.LeadershipRolesEntry.COLUMN_COMPLETED);
        Boolean completed = (completed_int == 1)?true:false;
        Date completionDate = Utility.convertStringToDate(contentValues.getAsString(GoodspeaksContract.LeadershipRolesEntry.COLUMN_COMPLETION_DATE));
        String _ID = contentValues.getAsString(GoodspeaksContract.LeadershipRolesEntry._ID);
        LeadershipRole role = new LeadershipRole(title);
        role.completed = completed;
        role.set_ID(_ID);
        role.setCompletionDate(completionDate);
        return role;
    }
}
