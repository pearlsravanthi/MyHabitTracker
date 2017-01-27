package app.com.pearlsravanthi.myhabittracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import app.com.pearlsravanthi.myhabittracker.db.HabitContract;
import app.com.pearlsravanthi.myhabittracker.db.HabitDbHelper;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase myDatabase;
    private HabitDbHelper mHelper;

    private Button submitBtn;
    private ListView habitListView;
    private ArrayAdapter<String> arrayAdapter;
    public ArrayList<String> habitList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("Welcome to Habit tracker");
        mHelper = new HabitDbHelper(this);

        mHelper.deleteDatabase();

        habitListView = (ListView) findViewById(R.id.habitListView);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, habitList);
        habitListView.setAdapter(arrayAdapter);

        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                //reading habit
                try {
                    habitList.clear();

                    Cursor c = mHelper.read();

                    int habitIndex = c.getColumnIndex(HabitContract.HabitEntry.COL_TASK_HABIT_NAME);
                    int frequencyIndex = c.getColumnIndex(HabitContract.HabitEntry.COL_TASK_HABIT_FREQ);

                    if (c != null && c.moveToFirst()){
                        do {
                            String habit = c.getString(habitIndex) + " : " + Integer.toString(c.getInt(frequencyIndex));
                            habitList.add(habit);
                        } while(c.moveToNext());
                    }
                    c.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //showing habit
                arrayAdapter.notifyDataSetChanged();
            }
        });

        //inserting new habit
        mHelper.insert("Reading books");
        mHelper.insert("Playing chess");

        //updating freq
        mHelper.update("Playing chess");

    }
}
