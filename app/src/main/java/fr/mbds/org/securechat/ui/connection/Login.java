package fr.mbds.org.securechat.ui.connection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.database.Database;
import fr.mbds.org.securechat.ui.messaging.Messaging;

public class Login extends AppCompatActivity {

    LinearLayout loginLayout;
    EditText emailBox;
    EditText pwdBox;
    TextView registerLink;
    AppCompatButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginLayout = (LinearLayout) findViewById(R.id.login_layout);
        emailBox = (EditText) findViewById(R.id.email_box);
        pwdBox = (EditText) findViewById(R.id.pwd_box);
        registerLink = (TextView) findViewById(R.id.register_link);
        loginBtn = (AppCompatButton) findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });

        Database db = Database.getInstance(getApplicationContext());

        //db.deleteAll();

        if (db.getUsers().size() == 0) {
            db.createUser("spooki", "spooki@gmail.com", "123");
            db.createUser("zoanthr", "zoanthr@gmail.com", "123");
            db.createUser("pracc", "pracc@gmail.com", "123");
            db.createUser("delirium", "delirium@gmail.com", "123");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Snackbar.make(loginLayout, "Account created", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void login() {
        Boolean fieldsValid = false;
        Database db = Database.getInstance(getApplicationContext());

        if (pwdBox.getText().toString().isEmpty()) {
            pwdBox.requestFocus();
            pwdBox.setError("Enter password");
            fieldsValid = false;
        } else {
            pwdBox.setError(null);
            fieldsValid = true;
        }

        if (emailBox.getText().toString().isEmpty()) {
            emailBox.requestFocus();
            emailBox.setError("Enter email");
            fieldsValid = false;
        } else {
            emailBox.setError(null);
            fieldsValid = true;
        }

        if (fieldsValid && !pwdBox.getText().toString().isEmpty() && !emailBox.getText().toString().isEmpty()
            && db.checkUserCanConnect(emailBox.getText().toString(), pwdBox.getText().toString())) {
            Intent messagingIntent = new Intent(this, Messaging.class);
            this.startActivity(messagingIntent);
        }
    }

    public void goToRegister() {
        Intent registerIntent = new Intent(this, Register.class);
        this.startActivityForResult(registerIntent, 1);
    }

}
