package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
        // referencing all items to their ID in the layout
        CheckEvent = (Spinner) findViewById(R.id.spinnerCheckEventType);
        // creating an adapter for CheckEvent
        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EventList2);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CheckEvent.setAdapter(spinnerAdapter2);
        // opening database
        EventslyDB = this.openOrCreateDatabase("eventslyDB", MODE_PRIVATE, null);
        // a loop that fills our Spinner CheckEvent with events
        while (getEvents(stringID) != 0)
        {
            EventList2.add(getEventName(stringID));
            spinnerAdapter2.notifyDataSetChanged();

            id++;
            stringID = Integer.toString(id);
        }
    }

    // returns the eventname that is on the same row as the ID that is passed in
    public String getEventName(String stringID)
    {
        Cursor c = EventslyDB.rawQuery("SELECT eventname FROM events WHERE id =?", new String[]{stringID});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("eventname"));
    }

    // returns the total count of rows that has the same ID that is passed in
    public int getEvents(String stringID)
    {
        Cursor c = EventslyDB.rawQuery("SELECT * FROM events WHERE id =?", new String[]{stringID});
        c.moveToFirst();
        return c.getCount();
    }

    // when clicked goes to host check-in screen and passes our event to the next screen
    public void onCheckInGuestClick(View view)
    {
        // sets what is selected in the spinner to a string
        String ChosenEvent = CheckEvent.getSelectedItem().toString();
        Intent getHostCheckInScreen = new Intent(this, HostCheckIn.class);
        // before going to host check-in screen it will save this string so that it can be used in that class
        getHostCheckInScreen.putExtra("GuestEvent", ChosenEvent);
        startActivity(getHostCheckInScreen);
        // closing database
        EventslyDB.close();

        finish();
    }

    // when back button is pressed on phone goes to host menu screen
    public void onBackPressed()
    {
        Intent getPreviousScreenIntent = new Intent(this, Host.class);
        startActivity(getPreviousScreenIntent);
        // closing database
        EventslyDB.close();

        finish();
    }

}
