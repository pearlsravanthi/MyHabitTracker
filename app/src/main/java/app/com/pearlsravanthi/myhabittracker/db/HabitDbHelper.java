package app.com.pearlsravanthi.myhabittracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.com.pearlsravanthi.myhabittracker.db.HabitContract.HabitEntry;

import java.util.ArrayList;
import java.util.List;

import static app.com.pearlsravanthi.myhabittracker.db.HabitContract.HabitEntry.COL_TASK_HABIT_FREQ;

/**
 * Created by sravanthi
 */
public class HabitDbHelper extends SQLiteOpenHelper {

    public HabitDbHelper(Context context) {
        super(context, HabitContract.DB_NAME, null, HabitContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + HabitContract.HabitEntry.TABLE + " (" + HabitContract.HabitEntry.COL_TASK_HABIT_NAME + " VARCHAR, " + COL_TASK_HABIT_FREQ + " INT(3))";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HabitContract.HabitEntry.TABLE);
        onCreate(db);
    }

    public void deleteDatabase() {
        this.deleteHabitsDB();
    }


    public void deleteHabitsDB() {
        String deleteScript = "delete from " + HabitContract.HabitEntry.TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteScript);
    }

    public void insert(String habitName) {

        int defaultFreq = 0;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COL_TASK_HABIT_NAME, habitName);
        values.put(COL_TASK_HABIT_FREQ, defaultFreq);

        db.insertWithOnConflict(HabitContract.HabitEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        db.close();

    }

    public void update(String habitName) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM habits WHERE habit = " + "'" + habitName + "'", null);

        try {
            int habitIndex = c.getColumnIndex(HabitContract.HabitEntry.COL_TASK_HABIT_NAME);
            int frequencyIndex = c.getColumnIndex(COL_TASK_HABIT_FREQ);


            if (c != null && c.moveToFirst()) {
                do {
                    int updatedFreq = c.getInt(frequencyIndex) + 1;
                    ContentValues values = new ContentValues();
                    values.put(HabitContract.HabitEntry.COL_TASK_HABIT_NAME, c.getString(habitIndex));
                    values.put(COL_TASK_HABIT_FREQ, updatedFreq);


                    db.update(HabitContract.HabitEntry.TABLE, values, HabitContract.HabitEntry.COL_TASK_HABIT_NAME + " = ?",
                            new String[]{String.valueOf(c.getString(habitIndex))});


                } while (c.moveToNext());
            }

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cursor read() {


        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "SELECT * FROM habits";

        String[] columns = new String[2];
        columns[0] = HabitEntry.COL_TASK_HABIT_NAME;
        columns[1] = HabitEntry.COL_TASK_HABIT_FREQ;
        Cursor c = db.query(HabitContract.HabitEntry.TABLE, columns, null, null,
                null, null, null);
        return c;

    }

}

