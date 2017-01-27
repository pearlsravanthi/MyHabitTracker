package app.com.pearlsravanthi.myhabittracker.db;

import android.provider.BaseColumns;

/**
 * Created by sravanthi
 */

public class HabitContract {
    public static final String DB_NAME = "app.com.pearlsravanthi.myhabittracker.db";
    public static final int DB_VERSION = 1;

    public class HabitEntry implements BaseColumns {
        public static final String TABLE = "habits";

        public static final String COL_TASK_HABIT_NAME = "habit";
        public static final String COL_TASK_HABIT_FREQ = "freq";
    }
}
