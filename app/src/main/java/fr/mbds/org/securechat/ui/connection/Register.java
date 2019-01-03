package fr.mbds.org.securechat.ui.connection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import fr.mbds.org.securechat.R;

public class Register extends AppCompatActivity {

    EditText usernameBox;
    EditText pwdBox;
    EditText pwdConfirmBox;
    TextView backLink;
    AppCompatButton registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ezregister);

        usernameBox = (EditText) findViewById(R.id.username_box);
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
        if (!usernameBox.getText().toString().isEmpty() && usernameBox.getText().toString() != null
                && pwdBox.getText().toString().equals(pwdConfirmBox.getText().toString())) {
            //registerBtn.setBackgroundColor(Color.GREEN);
            System.out.println("registered");
        }
        else {
            //registerBtn.setBackgroundColor(Color.RED);
            System.out.println("not registered");
        }
    }

    public void goToLogin() {
        this.finish();
    }

}
