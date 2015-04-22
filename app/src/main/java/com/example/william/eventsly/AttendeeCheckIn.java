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
import android.util.Log;
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

        PackageManager pm = this.getPackageManager();
        // Check whether NFC is available on device


        String rFileEmail = "EmailAddress.txt";

        File sdcardEmail = Environment.getExternalStorageDirectory();

        //Get the text file
        File EmailAddress = new File(sdcardEmail, rFileEmail);

        if(EmailAddress.exists())
        {
            EmailAddress.delete();
        }

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

        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC))
        {
            // NFC is not available on the device.
            Toast.makeText(this, "The device does not has NFC hardware.", Toast.LENGTH_SHORT).show();
        }
        // Check whether device is running Android 4.1 or higher
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
        {
            // Android Beam feature is not supported.
            Toast.makeText(this, "Android Beam is not supported.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // NFC and Android Beam file transfer is supported.
            Toast.makeText(this, "Android Beam is supported on your device.", Toast.LENGTH_SHORT).show();
        }
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // Check whether NFC is enabled on device
        if (!nfcAdapter.isEnabled())
        {
            // NFC is disabled, show the settings UI
            // to enable NFC
            Toast.makeText(this, "Please enable NFC.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
        // Check whether Android Beam feature is enabled on device
        else if (!nfcAdapter.isNdefPushEnabled())
        {
            // Android Beam is disabled, show the settings UI
            // to enable Android Beam
            Toast.makeText(this, "Please enable Android Beam.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
        }
        else
        {

                String content = (getEmail(stringID));
                File file;
                FileOutputStream outputStream;
                try
                {

                    file = new File(Environment.getExternalStorageDirectory(), "EmailAddress.txt");

                    outputStream = new FileOutputStream(file);
                    outputStream.write(content.getBytes());
                    outputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                // NFC and Android Beam both are enabled

                // File to be transferred
                // For the sake of this tutorial I've placed an image
                // named 'wallpaper.png' in the 'Pictures' directory
                String fileName = "EmailAddress.txt";

                // Retrieve the path to the user's public pictures directory
                File fileDirectory = Environment.getExternalStorageDirectory();

                // Create a new file using the specified directory and name
                File fileToTransfer = new File(fileDirectory, fileName);
                fileToTransfer.setReadable(true, true);
                fileToTransfer.setWritable(true, true);
                EventslyDB.close();

                nfcAdapter.setBeamPushUris(new Uri[]{Uri.fromFile(fileToTransfer)}, this);

            }
        }

    public String getEmail(String id)
    {
        Cursor c = EventslyDB.rawQuery("SELECT email FROM currentuser WHERE rowid =?", new String[]{id});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("email"));
    }

    public void onSeeCredentialsClick(View view)
    {
        Intent getBadgeScreenIntent = new Intent(this, AttendeeBadge.class);
        startActivity(getBadgeScreenIntent);

        EventslyDB.close();

        finish();
    }

    public void onBackPressed()
    {
        Intent getPreviousScreenIntent = new Intent(this, Attendee.class);
        startActivity(getPreviousScreenIntent);

        EventslyDB.close();

        finish();
    }

}


