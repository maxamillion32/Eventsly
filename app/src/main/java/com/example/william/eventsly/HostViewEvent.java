package com.example.william.eventsly;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

public class HostViewEvent extends Activity
{
    SQLiteDatabase EventslyDB = null;

    Button EditEvent;

    EditText DateAndTime, Address, Description;

    Spinner ChooseEvent, EventType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_view_event);

        DateAndTime = (EditText) findViewById(R.id.editTextViewDateAndTime);
        DateAndTime.setEnabled(false);

        Address = (EditText) findViewById(R.id.editTextViewAddress);
        Address.setEnabled(false);

        Description = (EditText) findViewById(R.id.editTextViewDescription);
        Description.setEnabled(false);

        EventType = (Spinner) findViewById(R.id.spinnerEventType);
        EventType.setEnabled(false);

        try
        {
            EventslyDB = this.openOrCreateDatabase("Events", MODE_PRIVATE, null);

            EventslyDB.execSQL("CREATE TABLE IF NOT EXISTS events " +
                    "(id integer primary key, eventname VARCHAR, dateandtime VARCHAR, address VARCHAR, eventtype VARCHAR, description VARCHAR);");

            File database = getApplicationContext().getDatabasePath("Events.db");

            if (!database.exists())
            {
                Toast.makeText(this, "Database Created or Exists", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Database doesn't exist", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e)
        {
            Log.e("Events ERROR", "Error Creating Database");
        }

        EditEvent = (Button) findViewById(R.id.btnEditEvent);
        ChooseEvent = (Spinner) findViewById(R.id.spinnerChooseEvent);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.EventTypes_array,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ChooseEvent.setAdapter(adapter);
    }

    public void onEditEventClick(View view)
    {
        DateAndTime = (EditText) findViewById(R.id.editTextViewDateAndTime);
        DateAndTime.setEnabled(true);

        Address = (EditText) findViewById(R.id.editTextViewAddress);
        Address.setEnabled(true);

        Description = (EditText) findViewById(R.id.editTextViewDescription);
        Description.setEnabled(true);

        EventType = (Spinner) findViewById(R.id.spinnerEventType);
        EventType.setEnabled(true);

        EventType = (Spinner) findViewById(R.id.spinnerEventType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.EventTypes_array,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        EventType.setAdapter(adapter);

    }
}
