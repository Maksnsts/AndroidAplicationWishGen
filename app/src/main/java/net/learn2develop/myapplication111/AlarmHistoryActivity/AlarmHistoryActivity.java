package net.learn2develop.myapplication111.AlarmHistoryActivity;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.learn2develop.myapplication111.AlarmProfile.AlarmProfileActivity;
import net.learn2develop.myapplication111.DataBase.DBHelper;
import net.learn2develop.myapplication111.DataBase.GroceryAdapter;
import net.learn2develop.myapplication111.DataBase.GroceryContract;
import net.learn2develop.myapplication111.R;
import net.learn2develop.myapplication111.SmsActivity.AlarmSmsActivity;

public class AlarmHistoryActivity extends AppCompatActivity  {

    private SQLiteDatabase mDatabase;
    private GroceryAdapter mAdapter;
    ImageButton add_new_set;

    public final static String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    Button next_button;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.alarm_home:
                    Intent intent1 = new Intent(AlarmHistoryActivity.this, AlarmProfileActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.history_alarm:
                    Intent intent2 = new Intent(AlarmHistoryActivity.this, AlarmHistoryActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.sms_menu:
                    Intent intent3 = new Intent(AlarmHistoryActivity.this, AlarmSmsActivity.class);
                    startActivity(intent3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmhistoryactivity);

        DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GroceryAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        add_new_set = findViewById(R.id.add_new_set);
        add_new_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AlarmHistoryActivity.this, AlarmProfileActivity.class);
                startActivity(intent1);
            }
        });

    }

    private Cursor getAllItems() {
        return mDatabase.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

}





