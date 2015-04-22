package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class HostSelectEventCheckIn extends Activity
{
    SQLiteDatabase EventslyDB = null;

    Spinner CheckEvent;

    int id = 1;
    String stringID = Integer.toString(id);

    ArrayList<String> EventList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_select_event_check_in);

        CheckEvent = (Spinner) findViewById(R.id.spinnerCheckEventType);

        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EventList2);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CheckEvent.setAdapter(spinnerAdapter2);

        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        CheckEvent.setAdapter(spinnerAdapter2);

        try
        {
            EventslyDB = this.openOrCreateDatabase("eventslyDB", MODE_PRIVATE, null);

            File database = getApplicationContext().getDatabasePath("eventslyDB.db");

            if (!database.exists())
            {
                Toast.makeText(this, "Database Created or Exists", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Database doesn't exist", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Log.e("eventslyDB ERROR", "Error Creating Database");
        }


        while(getEvents(stringID) != 0)
        {
            EventList2.add(getEventName(stringID));
            spinnerAdapter2.notifyDataSetChanged();

            id++;
            stringID = Integer.toString(id);
        }
    }

    public String getEventName(String stringID)
    {
        Cursor c = EventslyDB.rawQuery("SELECT eventname FROM events WHERE id =?", new String[]{stringID});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("eventname"));
    }

    public int getEvents(String stringID)
    {
        Cursor c = EventslyDB.rawQuery("SELECT * FROM events WHERE id =?", new String[]{stringID});
        c.moveToFirst();
        return c.getCount();
    }

    public void onCheckInGuestClick(View view)
    {
        String ChosenEvent = CheckEvent.getSelectedItem().toString();
        Intent getHostCheckInScreen = new Intent(this, HostCheckIn.class);
        getHostCheckInScreen.putExtra("GuestEvent",ChosenEvent);
        startActivity(getHostCheckInScreen);

        EventslyDB.close();

        finish();
    }

    public void onBackPressed()
    {
        Intent getPreviousScreenIntent = new Intent(this, Host.class);
        startActivity(getPreviousScreenIntent);

        EventslyDB.close();

        finish();
    }

}
