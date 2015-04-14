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

    Button CreateAccount;

    EditText FirstName, LastName, Email, Password;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        CreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        FirstName = (EditText) findViewById(R.id.editTextFirstName);
        LastName = (EditText) findViewById(R.id.editTextLastName);
        Email = (EditText) findViewById(R.id.editTextEmailSignUp);
        Password = (EditText) findViewById(R.id.editTextPasswordSignUp);

        try
        {
            AccountsDB = this.openOrCreateDatabase("Accounts", MODE_PRIVATE, null);

            AccountsDB.execSQL("CREATE TABLE IF NOT EXISTS accounts " +
                    "(id integer primary key, firstname VARCHAR, lastname VARCHAR, email VARCHAR, password VARCHAR);");

            File database = getApplicationContext().getDatabasePath("Accounts.db");

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
            Log.e("ACCOUNTS ERROR", "Error Creating Database");
        }
    }

    public void onCancelSignUpClick(View view)
    {
        Intent getTitleScreenIntent = new Intent(this, Title.class);

        startActivity(getTitleScreenIntent);

        AccountsDB.close();

        finish();
        System.exit(0);

    }

    public void onCreateAccountClick(View view)
    {

        String firstname = FirstName.getText().toString();
        String lastname = LastName.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        AccountsDB.execSQL("INSERT INTO accounts (firstname, lastname, email, password) VALUES ('" +
        firstname + "', '" + lastname + "', '" + email + "', '" + password + "');");

        Toast.makeText(this,"Account Creation Successful!!!", Toast.LENGTH_SHORT).show();

    }
}
