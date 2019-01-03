package fr.mbds.org.securechat.ui.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.database.Database;

public class Register extends AppCompatActivity {

    LinearLayout registerLayout;
    EditText usernameBox;
    EditText emailBox;
    EditText pwdBox;
    EditText pwdConfirmBox;
    TextView backLink;
    AppCompatButton registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        registerLayout = (LinearLayout) findViewById(R.id.register_layout);
        usernameBox = (EditText) findViewById(R.id.username_box);
        emailBox = (EditText) findViewById(R.id.email_box);
        pwdBox = (EditText) findViewById(R.id.pwd_box);
        pwdConfirmBox = (EditText) findViewById(R.id.pwd_confirm_box);
        backLink = (TextView) findViewById(R.id.back_to_login_link);
        registerBtn = (AppCompatButton) findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        backLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
    }

    public void register() {
        Boolean fieldsValid = true;
        String username = usernameBox.getText().toString();
        String email = emailBox.getText().toString();
        String password = pwdBox.getText().toString();
        String passwordConfirm = pwdConfirmBox.getText().toString();

        // Check password match
        if (!passwordConfirm.equals(password)) {
            pwdConfirmBox.requestFocus();
            pwdConfirmBox.setError("Passwords must match");
            fieldsValid = false;
        } else {
            pwdConfirmBox.setError(null);
            fieldsValid = fieldsValid ? true : false;
        }

        // Check password empty
        if (password.isEmpty()) {
            pwdBox.requestFocus();
            pwdBox.setError("Enter password");
            fieldsValid = false;
        } else {
            pwdBox.setError(null);
            fieldsValid = fieldsValid ? true : false;
        }

        // Check email empty
        if (email.isEmpty()) {
            emailBox.requestFocus();
            emailBox.setError("Enter email");
            fieldsValid = false;
        } else {
            emailBox.setError(null);
            fieldsValid = fieldsValid ? true : false;
        }

        //Check username empty
        if (username.isEmpty()) {
            usernameBox.requestFocus();
            usernameBox.setError("Enter username");
            fieldsValid = false;
        } else {
            usernameBox.setError(null);
            fieldsValid = fieldsValid ? true : false;
        }

        if (fieldsValid) {

            Database db = Database.getInstance(getApplicationContext());

            if (db.checkUserCanRegister(email, username)) {
                //db.createUser(username, email, password);

                setResult(Activity.RESULT_OK);
                finish();
            }
            else {
                // Hide keyboard
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                // Display error message
                Snackbar.make(registerLayout, "Username or email already in use", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void goToLogin() {
        setResult(Activity.RESULT_CANCELED);
        this.finish();
    }

}
