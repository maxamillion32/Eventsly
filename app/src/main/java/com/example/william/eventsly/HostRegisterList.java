package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

public class HostRegisterList extends Activity
{

    SQLiteDatabase EventslyDB = null;

    EditText FirstName, LastName, Email, Access;

    Spinner RegisterGuestEvent;

    ArrayList<String> EventRegisterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_register_list);

        FirstName = (EditText) findViewById(R.id.editTextRegisterFirstName);
        LastName = (EditText) findViewById(R.id.editTextRegisterLastName);
        Email = (EditText) findViewById(R.id.editTextRegisterEmail);
        Access = (EditText) findViewById(R.id.editTextRegisterAccess);

        RegisterGuestEvent = (Spinner) findViewById(R.id.spinnerRegisterGuestEvent);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EventRegisterList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RegisterGuestEvent.setAdapter(spinnerAdapter);

        try
        {
            EventslyDB = this.openOrCreateDatabase("eventslyDB", MODE_PRIVATE, null);

            EventslyDB.execSQL("CREATE TABLE IF NOT EXISTS eventguestlist " +
                    "(id integer primary key, eventname VARCHAR, firstname VARCHAR, lastname VARCHAR, email VARCHAR, accesslevel VARCHAR);");

            File database = getApplicationContext().getDatabasePath("eventslyDB.db");

            if (!database.exists())
            {
                Toast.makeText(this, "Database Created or Exists", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Database doesn't Exists", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Log.e("eventslyDB ERROR", "Error Creating Database");
        }

        int id = 1;
        String stringID = Integer.toString(id);

        while (getEvents(stringID) != 0)
        {
            EventRegisterList.add(getEventName(stringID));
            spinnerAdapter.notifyDataSetChanged();

            id++;
            stringID = Integer.toString(id);
        }
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

    public void onRegisterClick(View view)
    {
        String eventname = RegisterGuestEvent.getSelectedItem().toString();
        String firstname = FirstName.getText().toString();
        String lastname = LastName.getText().toString();
        String email = Email.getText().toString();
        String accesslevel = Access.getText().toString();

        if(firstname.equals(""))
        {
            Toast.makeText(this, "Please enter your first name.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(lastname.equals(""))
            {
                Toast.makeText(this, "Please enter your last name.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(email.equals(""))
                {
                    Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(accesslevel.equals(""))
                    {
                        Toast.makeText(this, "Please enter what level of access you have.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(isValidEmail(email))
                        {
                            if(GetEmailChecked(email, eventname) == 0)
                            {
                                EventslyDB.execSQL("INSERT INTO eventguestlist (eventname, firstname, lastname, email, accesslevel) VALUES ('" +
                                        eventname + "', '" + firstname + "', '" + lastname + "', '" + email + "', '" + accesslevel + "');");

                                Toast.makeText(this, "Guest Registered", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(this, "A guest with this email already exists.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(this, "Email is not valid.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    public void onMainMenuClick(View view)
    {
        EventslyDB.close();

        Intent getHostMenuScreenIntent = new Intent(this, Host.class);
        startActivity(getHostMenuScreenIntent);

        finish();
    }

    public int GetEmailChecked(String email, String eventname)
    {
        Cursor c = EventslyDB.rawQuery("SELECT eventname , COUNT(*) FROM (SELECT email, eventname FROM eventguestlist WHERE email =? AND eventname =?)" +
                " GROUP BY eventname HAVING COUNT(*) >= 1;", new String[]{email, eventname});
        c.moveToFirst();
        return c.getCount();

    }

    public static boolean isValidEmail(CharSequence target)
    {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void onBackPressed()
    {
        Intent getPreviousScreenIntent = new Intent(this, Host.class);
        startActivity(getPreviousScreenIntent);

        EventslyDB.close();

        finish();
    }


}
