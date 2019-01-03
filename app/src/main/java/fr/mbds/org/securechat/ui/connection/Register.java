package fr.mbds.org.securechat.ui.connection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.database.Database;

public class Register extends AppCompatActivity {

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
        String username = usernameBox.getText().toString();
        String email = emailBox.getText().toString();
        String password = pwdBox.getText().toString();
        String passwordConfirm = pwdConfirmBox.getText().toString();

        if (!username.isEmpty() && username != null && !email.isEmpty() && email != null && password.equals(passwordConfirm)) {

            Database db = Database.getInstance(getApplicationContext());

            if (db.checkUserCanRegister(email, username)) {
                db.createUser(username, email, password);
                System.out.println("registered");
            }
            else {
                System.out.println("username or email in use");
            }
        }
        else {
            System.out.println("Empty text boxes");
        }
    }

    public void goToLogin() {
        this.finish();
    }

}
