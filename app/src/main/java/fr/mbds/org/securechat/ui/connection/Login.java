package fr.mbds.org.securechat.ui.connection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import fr.mbds.org.securechat.R;

public class Login extends AppCompatActivity {

    EditText loginBox;
    EditText pwdBox;
    TextView registerLink;
    AppCompatButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ezlogin);

        loginBox = (EditText) findViewById(R.id.login_box);
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
    }

    public void login() {
        if (loginBox.getText().toString().equals("spooki") && pwdBox.getText().toString().equals("123")) {
            //loginBtn.setBackgroundColor(Color.GREEN);
            System.out.println("logged");
        }
        else {
            //loginBtn.setBackgroundColor(Color.RED);
            System.out.println("not logged");
        }
    }

    public void goToRegister() {
        Intent registerIntent = new Intent(this, Register.class);
        this.startActivity(registerIntent);
    }

}
