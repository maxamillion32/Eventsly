package com.example.william.eventsly;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;


public class SignUp extends Activity
{
    SQLiteDatabase AccountsDB = null;

    Button CreateAccount, Cancel;

    EditText FirstName, LastName, Email, Password, ConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        try
        {
            AccountsDB = this.openOrCreateDatabase("Accounts", MODE_PRIVATE, null);

            AccountsDB.execSQL("CREATE TABLE IF NOT EXISTS accounts " +
                    "(id integer primary key, firstname VARCHAR, lastname VARCHAR, email VARCHAR, password VARCHAR);");

            File database = getApplicationContext().getDatabasePath("Accounts.db");

            if (!database.exists()) {
                Toast.makeText(this, "Database Created or Exists", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Database doesn't exist", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e)
        {
            Log.e("ACCOUNTS ERROR", "Error Creating Database");
        }
    }

    public void onCancelSignUpClick(View view)
    {
        Intent getTitleScreenIntent = new Intent(this, Title.class);

        startActivity(getTitleScreenIntent);

        finish();
        System.exit(0);

    }

    public void onCreateAccountClick(View view)
    {

    }
}
