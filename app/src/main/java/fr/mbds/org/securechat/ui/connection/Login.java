package fr.mbds.org.securechat.ui.connection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.database.Database;
import fr.mbds.org.securechat.ui.messaging.Messaging;

public class Login extends AppCompatActivity {

    EditText emailBox;
    EditText pwdBox;
    TextView registerLink;
    AppCompatButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

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

    public void login() {

        Database db = Database.getInstance(getApplicationContext());

        if (db.checkUserCanConnect(emailBox.getText().toString(), pwdBox.getText().toString())) {
            Intent messagingIntent = new Intent(this, Messaging.class);
            this.startActivity(messagingIntent);
        }
    }

    public void goToRegister() {
        Intent registerIntent = new Intent(this, Register.class);
        this.startActivity(registerIntent);
    }

}
