package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
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

public class HostCreateEvent extends Activity
{
    SQLiteDatabase EventslyDB = null;

    Button CreateEvent;

    EditText EventName, DateAndTime, Address, Description;

    Spinner EventType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_create_event);

        CreateEvent = (Button) findViewById(R.id.btnCreateEvent);
        EventName = (EditText) findViewById(R.id.editTextEventName);
        DateAndTime = (EditText) findViewById(R.id.editTextDateAndTime);
        Address = (EditText) findViewById(R.id.editTextAddress);
        Description = (EditText) findViewById(R.id.editTextDescription);
        EventType = (Spinner) findViewById(R.id.spinnerEventType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.EventTypes_array,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        EventType.setAdapter(adapter);

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
    }

    public void onCreateEventClick(View view)
    {
        String eventname = EventName.getText().toString();
        String dateandtime = DateAndTime.getText().toString();
        String address = Address.getText().toString();
        String eventtype = EventType.getSelectedItem().toString();
        String description = Description.getText().toString();

        if(eventname.equals(""))
        {
            Toast.makeText(this, "Please enter a name for your event.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(dateandtime.equals(""))
            {
                Toast.makeText(this, "Please enter a date and time for your event.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(address.equals(""))
                {
                    Toast.makeText(this, "Please enter an address for your event.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(eventtype.equals("Select Event Type"))
                    {
                        Toast.makeText(this, "Please select an event type.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(description.equals(""))
                        {
                            Toast.makeText(this, "Please enter a description for your event.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            EventslyDB.execSQL("INSERT INTO Events (eventname, dateandtime, address, eventtype, description) VALUES ('" +
                                    eventname + "', '" + dateandtime + "', '" + address + "', '" + eventtype + "', '" + description + "');");

                            Toast.makeText(this, "Event Creation Successful", Toast.LENGTH_SHORT).show();

                            EventslyDB.close();

                            Intent getHostScreenIntent = new Intent(this, Host.class);

                            startActivity(getHostScreenIntent);

                            finish();

                        }
                    }
                }
            }
        }
    }

    public void onCancelCreateEventClick(View view)
    {
        Intent getHostScreenIntent = new Intent(this, Host.class);

        startActivity(getHostScreenIntent);

        finish();
    }
}
