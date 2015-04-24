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
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AttendeeCheckIn extends Activity
{
    SQLiteDatabase EventslyDB = null;

    NfcAdapter nfcAdapter;

    int id = 1;
    String stringID = Integer.toString(id);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_check_in);
        // file name
        String rFileEmail = "EmailAddress.txt";
        // file directory
        File sdcardEmail = Environment.getExternalStorageDirectory();
        // get the text file
        File EmailAddress = new File(sdcardEmail, rFileEmail);
        // if file exists delete it
        if (EmailAddress.exists())
        {
            EmailAddress.delete();
        }

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
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // check whether NFC is enabled on device
        if (!nfcAdapter.isEnabled())
        {
            // NFC is disabled, show the settings UI to enable it
            Toast.makeText(this, "Please enable NFC.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
        // check whether Android Beam feature is enabled on device
        else
        {
            if (!nfcAdapter.isNdefPushEnabled())
            {
                // android beam is disabled, show the settings UI to enable it
                Toast.makeText(this, "Please enable Android Beam.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
            }
            else
            {
                // gets the email and then puts it in a file as a string
                String content = (getEmail(stringID));
                File file;
                FileOutputStream outputStream;
                try
                {
                    // create file
                    file = new File(Environment.getExternalStorageDirectory(), "EmailAddress.txt");

                    outputStream = new FileOutputStream(file);
                    outputStream.write(content.getBytes());
                    outputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                // NFC and android beam are enabled
                // file to be transferred
                String fileName = "EmailAddress.txt";

                // retrieve the path to the user's directory
                File fileDirectory = Environment.getExternalStorageDirectory();

                // create a new file using the specified directory and name
                File fileToTransfer = new File(fileDirectory, fileName);
                // set file to be readable and writable
                fileToTransfer.setReadable(true, true);
                fileToTransfer.setWritable(true, true);
                // closing database
                EventslyDB.close();
                // push file using NFC
                nfcAdapter.setBeamPushUris(new Uri[]{Uri.fromFile(fileToTransfer)}, this);
            }
        }
    }

    // returns the email that is on the same row as the ID that is passed in
    public String getEmail(String id)
    {
        Cursor c = EventslyDB.rawQuery("SELECT email FROM currentuser WHERE rowid =?", new String[]{id});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("email"));
    }

    // goes to attendee badge screen
    public void onSeeCredentialsClick(View view)
    {
        Intent getBadgeScreenIntent = new Intent(this, AttendeeBadge.class);
        startActivity(getBadgeScreenIntent);
        // closing database
        EventslyDB.close();

        finish();
    }

    // when back button is pressed on the phone goes to attendee menu screen
    public void onBackPressed()
    {
        Intent getPreviousScreenIntent = new Intent(this, Attendee.class);
        startActivity(getPreviousScreenIntent);
        // closing database
        EventslyDB.close();

        finish();
    }

}


