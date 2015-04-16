package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
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

    EditText FirstName, LastName, Email, Password, ConfirmPassword;

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
        ConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPasswordSignUp);

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
    }

    public void onCreateAccountClick(View view)
    {

        String firstname = FirstName.getText().toString();
        String lastname = LastName.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String confirmpassword = ConfirmPassword.getText().toString();

        if(password.equals(confirmpassword))
        {
            if(isValidEmail(email))
            {
                if(GetEmailChecked(email) == 0)
                {
                    AccountsDB.execSQL("INSERT INTO accounts (firstname, lastname, email, password) VALUES ('" +
                            firstname + "', '" + lastname + "', '" + email + "', '" + password + "');");

                    Toast.makeText(this, "Account Creation Successful!!!", Toast.LENGTH_SHORT).show();

                    AccountsDB.close();

                    Intent getLoginScreenIntent = new Intent(this, Login.class);

                    startActivity(getLoginScreenIntent);
                }
                else
                {
                    Toast.makeText(this, "An account with this email already exists.", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
               Toast.makeText(this, "Email is not valid.", Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            Toast.makeText(this,"Passwords do not match.", Toast.LENGTH_SHORT).show();
        }
    }

    public int GetEmailChecked(String email)
    {
        Cursor c = AccountsDB.rawQuery("SELECT email FROM accounts WHERE email =?", new String[]{email});
        c.moveToFirst();
        return c.getCount();

    }

    public static boolean isValidEmail(CharSequence target)
    {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
