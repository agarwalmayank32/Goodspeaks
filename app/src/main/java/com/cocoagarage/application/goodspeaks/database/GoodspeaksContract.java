package com.cocoagarage.application.goodspeaks.database;

import android.provider.BaseColumns;

/*
* Defines tables and columns for the Goodspeaks database.
* */
public class GoodspeaksContract {

    public static final class CCEntry implements BaseColumns {

        public static final String TABLE_NAME = "cc";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_SPEECH_TITLE = "speech_title";
        public static final String COLUMN_COMPLETION_DATE = "completion_date";
        public static final String COLUMN_EVAUATIONS = "evaluations";
        public static final String COLUMN_COMPLETED = "completed";
        public static final String COLUMN_MINIMUM_TIME = "min_time";
    }

    public static final class PracticeSpeachesEntry implements BaseColumns {
        public static final String TABLE_NAME = "practice_speeches";
        public static final String COLUMN_TITLE = "tile";
        public static final String COLUMN_PATH = "path";
        public static final String COLUMN_DATE = "date";
        public static final String COUMN_FK = "cc_title";
    }

    public static final class CLEntry implements BaseColumns {
        public static final String TABLE_NAME = "cl";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ROLES = "roles";
    }

    public static final class LeadershipRolesEntry implements BaseColumns {
        public static final String TABLE_NAME = "leadership_roles";
        public static final String COLUMN_COMPLETED = "completed";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_COMPLETION_DATE = "completion_date";
    }
}
