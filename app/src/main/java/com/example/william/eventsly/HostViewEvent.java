package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

public class HostViewEvent extends Activity
{
    SQLiteDatabase EventslyDB = null;

    Button EditEvent, SaveEditEvent;

    EditText DateAndTime, Address, Description;

    Spinner ChooseEvent, EventType;

    ListView EventGuestList;

    ArrayList<String> EventList = new ArrayList<>();
    ArrayList<String> GuestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        EditEvent = (Button) findViewById(R.id.btnEditEvent);

        SaveEditEvent = (Button) findViewById(R.id.btnSaveEditEvent);
        SaveEditEvent.setVisibility(View.GONE);

        ChooseEvent = (Spinner) findViewById(R.id.spinnerChooseEvent);

        EventGuestList = (ListView) findViewById(R.id.listViewRegisteredGuests);

        try
        {
            EventslyDB = this.openOrCreateDatabase("eventslyDB", MODE_PRIVATE, null);

            EventslyDB.execSQL("CREATE TABLE IF NOT EXISTS events " +
                    "(id integer primary key, eventname VARCHAR, dateandtime VARCHAR, address VARCHAR, eventtype VARCHAR, description VARCHAR);");

            File database = getApplicationContext().getDatabasePath("eventslyDB.db");

            if (!database.exists())
            {
                Toast.makeText(this, "Database Created or Exists", Toast.LENGTH_SHORT).show();
            } else
            {
                Toast.makeText(this, "Database doesn't exist", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Log.e("eventslyDB ERROR", "Error Creating Database");
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.EventTypes_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        EventType.setAdapter(adapter);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EventList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ChooseEvent.setAdapter(spinnerAdapter);

        final ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, GuestList);

        int id = 1;
        String stringID = Integer.toString(id);

        while(getEvents(stringID) != 0)
        {
            EventList.add(getEventName(stringID));
            spinnerAdapter.notifyDataSetChanged();

            id++;
            stringID = Integer.toString(id);
        }

        ChooseEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String eventname = ChooseEvent.getSelectedItem().toString();
                DateAndTime.setText(getDateAndTime(eventname));
                Address.setText(getAddress(eventname));
                EventType.setSelection(getSpinnerIndex(EventType, getEventType(eventname)));
                Description.setText(getDescription(eventname));

                int rowid = 1;
                String stringRowID = Integer.toString(rowid);

                while(getNameListCount(eventname, stringRowID) != 0)
                {
                    GuestList.add(getRegisteredGuestsFirstName(eventname, stringRowID) + " " + getRegisteredGuestsLastName(eventname, stringRowID));

                    rowid++;
                    stringRowID = Integer.toString(rowid);
                }
                    EventGuestList.setAdapter(listAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    public int getEvents(String stringID)
    {
            Cursor c = EventslyDB.rawQuery("SELECT * FROM events WHERE id =?", new String[]{stringID});
            c.moveToFirst();
            return c.getCount();
    }

    public String getEventName(String stringID)
    {
        Cursor c = EventslyDB.rawQuery("SELECT eventname FROM events WHERE id =?", new String[]{stringID});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("eventname"));
    }

    public String getDateAndTime(String eventname)
    {
        Cursor c = EventslyDB.rawQuery("SELECT dateandtime FROM events WHERE eventname =?", new String[]{eventname});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("dateandtime"));
    }

    public String getAddress(String eventname)
    {
        Cursor c = EventslyDB.rawQuery("SELECT address FROM events WHERE eventname =?", new String[]{eventname});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("address"));
    }

    public String getEventType(String eventname)
    {
        Cursor c = EventslyDB.rawQuery("SELECT eventtype FROM events WHERE eventname =?", new String[]{eventname});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("eventtype"));
    }

    public String getDescription(String eventname)
    {
        Cursor c = EventslyDB.rawQuery("SELECT description FROM events WHERE eventname =?", new String[]{eventname});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("description"));
    }

    public String getRegisteredGuestsFirstName(String eventname, String rowid)
    {
        Cursor c = EventslyDB.rawQuery("SELECT firstname FROM eventguestlist WHERE eventname=? and rowid=?", new String[]{eventname, rowid});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("firstname"));
    }

    public String getRegisteredGuestsLastName(String eventname, String rowid)
    {
        Cursor c = EventslyDB.rawQuery("SELECT lastname FROM eventguestlist WHERE eventname=? and rowid=?", new String[]{eventname, rowid});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("lastname"));
    }

    public int getNameListCount(String eventname, String rowid)
    {
        Cursor c = EventslyDB.rawQuery("SELECT firstname FROM eventguestlist WHERE eventname=? and rowid=?", new String[]{eventname, rowid});
        c.moveToFirst();
        return c.getCount();
    }

    public int getSpinnerIndex(Spinner chooseEvent, String spinnerEvent)
    {
        int index = 0;

        for (int i = 0; i < chooseEvent.getCount(); i++)
        {
            if (chooseEvent.getItemAtPosition(i).equals(spinnerEvent))
            {
                index = i;
            }
        }
        return index;
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

        EditEvent.setVisibility(View.GONE);
        SaveEditEvent.setVisibility(View.VISIBLE);

    }

    public void onSaveEditEventClick(View view)
    {
        String eventname = ChooseEvent.getSelectedItem().toString();
        String dateandtime = DateAndTime.getText().toString();
        String address = Address.getText().toString();
        String eventtype = EventType.getSelectedItem().toString();
        String description = Description.getText().toString();

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
                            EventslyDB.execSQL("UPDATE events SET dateandtime = '" + dateandtime +
                                    "' WHERE eventname = '" + eventname + "';");

                            EventslyDB.execSQL("UPDATE events SET address = '" + address +
                                    "' WHERE eventname = '" + eventname + "';");

                            EventslyDB.execSQL("UPDATE events SET eventtype = '" + eventtype +
                                    "' WHERE eventname = '" + eventname + "';");

                            EventslyDB.execSQL("UPDATE events SET description = '" + description +
                                    "' WHERE eventname = '" + eventname + "';");

                            Toast.makeText(this, "Event Edit Successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        EditEvent.setVisibility(View.VISIBLE);
        SaveEditEvent.setVisibility(View.GONE);

        DateAndTime = (EditText) findViewById(R.id.editTextViewDateAndTime);
        DateAndTime.setEnabled(false);

        Address = (EditText) findViewById(R.id.editTextViewAddress);
        Address.setEnabled(false);

        Description = (EditText) findViewById(R.id.editTextViewDescription);
        Description.setEnabled(false);

        EventType = (Spinner) findViewById(R.id.spinnerEventType);
        EventType.setEnabled(false);
    }

    public void onMainMenuClick(View view)
    {
        EventslyDB.close();

        Intent getHostMenuScreenIntent = new Intent(this, Host.class);
        startActivity(getHostMenuScreenIntent);

        finish();
    }
}




