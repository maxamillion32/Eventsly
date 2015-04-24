package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class HostCheckIn extends Activity
{
    TextView GuestCreds;

    SQLiteDatabase EventslyDB = null;

    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_check_in);
        Intent intent = getIntent();
        // getting string sent from previous screen
        String ChosenEvent = intent.getExtras().getString("GuestEvent");
        // referencing all items by their id in the layout
        GuestCreds = (TextView) findViewById(R.id.labelGuestCreds);
        // file name
        String rFileAccess = "EventslyAccess.txt";
        // file directory
        File sdcardAccess = Environment.getExternalStorageDirectory();
        //Get the text file
        File receivedAccessFile = new File(sdcardAccess, rFileAccess);
        // if the file exists delete it
        if (receivedAccessFile.exists())
        {
            receivedAccessFile.delete();
        }
        // opening database
        EventslyDB = this.openOrCreateDatabase("eventslyDB", MODE_PRIVATE, null);

        PackageManager pm = this.getPackageManager();
        // check whether NFC is available on device
        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC))
        {
            // NFC is not available on the device.
            Toast.makeText(this, "The device does not has NFC hardware.", Toast.LENGTH_SHORT).show();
        }
        // check whether device is running Android 4.1 or higher
        else
        {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            {
                // android beam feature is not supported.
                Toast.makeText(this, "Android Beam is not supported.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                // NFC and android beam file transfer is supported.
                Toast.makeText(this, "Android Beam is supported on your device.", Toast.LENGTH_SHORT).show();
            }
        }
        // file name
        String rFileName = "EmailAddress.txt";
        // file directory
        File sdcard = Environment.getExternalStorageDirectory();
        // get the text file
        File receivedfile = new File(sdcard, rFileName);
        // read text from file
        StringBuilder text = new StringBuilder();
        // reads the text from the file and adds it to a string
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(receivedfile));
            String line;

            while ((line = br.readLine()) != null)
            {
                text.append(line);
            }
            br.close();
            String getEmail = String.valueOf(text);
            GuestCreds.setText(getEmail);

        }
        catch (IOException e)
        {
            // file directory
            File sdcard2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            // get the text file
            File receivedfile2 = new File(sdcard2, rFileName);
            // read text from file
            StringBuilder text2 = new StringBuilder();
            // reads the text from the file and adds it to a string
            try
            {
                BufferedReader br = new BufferedReader(new FileReader(receivedfile2));
                String line;

                while ((line = br.readLine()) != null)
                {
                    text2.append(line);
                }
                br.close();
                String getEmail2 = String.valueOf(text2);
                GuestCreds.setText(getEmail2);

            }
            catch (IOException e2)
            {
                Toast.makeText(this, "File can't be found.", Toast.LENGTH_LONG).show();
            }
            // delete file after use
            receivedfile2.delete();
        }
        // delete file after use
        receivedfile.delete();
        // the text that was read from the file
        String checkemail = GuestCreds.getText().toString();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // check whether NFC is enabled on device
        if (!nfcAdapter.isEnabled())
        {
            // NFC is disabled, show the settings UI
            // to enable NFC
            Toast.makeText(this, "Please enable NFC.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
        // check whether android beam feature is enabled on device
        else
        {
            if (!nfcAdapter.isNdefPushEnabled())
            {
                // android beam is disabled, show the settings UI
                // to enable android beam
                Toast.makeText(this, "Please enable Android Beam.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
            }
            else
            {
                // if the string does not return a row for that event in the database
                if (getEmailAndEventVerified(checkemail, ChosenEvent) == 0)
                {
                    // creating string to be place in a file
                    String content = "Access Denied.";
                    File file;
                    FileOutputStream outputStream;
                    try
                    {
                        // creating file
                        file = new File(Environment.getExternalStorageDirectory(), "EventslyAccess.txt");

                        outputStream = new FileOutputStream(file);
                        outputStream.write(content.getBytes());
                        outputStream.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    // NFC and android beam both are enabled

                    // file to be transferred
                    String fileName = "EventslyAccess.txt";

                    // retrieve the path to the user's directory
                    File fileDirectory = Environment.getExternalStorageDirectory();

                    // create a new file using the specified directory and name
                    File fileToTransfer = new File(fileDirectory, fileName);
                    // file set to readable and writable
                    fileToTransfer.setReadable(true, true);
                    fileToTransfer.setWritable(true, true);
                    // closing database
                    EventslyDB.close();
                    // push file using NFC
                    nfcAdapter.setBeamPushUris(new Uri[]{Uri.fromFile(fileToTransfer)}, this);
                }
                // if the string does return a row that matches the event chosen
                else
                {
                    // gets the first name, last name and access level
                    // the string will then be put in a file
                    String content = "Name: " + (getAccessFirstName(checkemail, ChosenEvent) + " " + getAccessLastName(checkemail, ChosenEvent) +
                            "          Access Level: " + getAccessLevel(checkemail, ChosenEvent));
                    File file;
                    FileOutputStream outputStream;
                    try
                    {
                        // creating file
                        file = new File(Environment.getExternalStorageDirectory(), "EventslyAccess.txt");

                        outputStream = new FileOutputStream(file);
                        outputStream.write(content.getBytes());
                        outputStream.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    // NFC and android beam both are enabled

                    // file to be transferred
                    String fileName = "EventslyAccess.txt";

                    // retrieve the path to the user's directory
                    File fileDirectory = Environment.getExternalStorageDirectory();

                    // create a new file using the specified directory and name
                    File fileToTransfer = new File(fileDirectory, fileName);
                    // sets file to be readable and writable
                    fileToTransfer.setReadable(true, true);
                    fileToTransfer.setWritable(true, true);
                    // closing database
                    EventslyDB.close();
                    // push file using NFC
                    nfcAdapter.setBeamPushUris(new Uri[]{Uri.fromFile(fileToTransfer)}, this);

                }
            }
        }
    }

    // returns the first name that is on the same row as the email and eventname that is passed in
    public String getAccessFirstName(String email, String checkevent)
    {
        Cursor c = EventslyDB.rawQuery("SELECT * FROM eventguestlist WHERE email =? and eventname =?", new String[]{email, checkevent});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("firstname"));
    }

    // returns the last name that is on the same row as the email and eventname that is passed in
    public String getAccessLastName(String email, String checkevent)
    {
        Cursor c = EventslyDB.rawQuery("SELECT * FROM eventguestlist WHERE email =? and eventname =?", new String[]{email, checkevent});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("lastname"));
    }

    // returns the access level that is on the same row as the email and eventname that is passed in
    public String getAccessLevel(String email, String checkevent)
    {
        Cursor c = EventslyDB.rawQuery("SELECT * FROM eventguestlist WHERE email =? and eventname =?", new String[]{email, checkevent});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("accesslevel"));
    }

    // returns a count of rows to see if the email ane event that is passed in exists
    public int getEmailAndEventVerified(String email, String checkevent)
    {
        Cursor c = EventslyDB.rawQuery("SELECT * FROM eventguestlist WHERE email =? and eventname =?", new String[]{email, checkevent});
        c.moveToFirst();
        return c.getCount();
    }

    // when the back button is pressed on the phone goes to the previous check-in screen
    public void onBackPressed()
    {
        Intent getPreviousScreenIntent = new Intent(this, HostSelectEventCheckIn.class);
        startActivity(getPreviousScreenIntent);
        // closing database
        EventslyDB.close();

        finish();
    }

}


