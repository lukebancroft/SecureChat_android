package fr.mbds.org.securechat.ui.connection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.database.Database;
import fr.mbds.org.securechat.ui.messaging.Messaging;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    LinearLayout loginLayout;
    EditText emailBox;
    EditText pwdBox;
    TextView registerLink;
    AppCompatButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if (mAuth.getCurrentUser() != null) {
            goToMessaging();
        }

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onExitAppAction(MenuItem mi) {
        new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setMessage("Are you sure you want to exit app?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(getApplicationContext(), "Account created.",
                        Toast.LENGTH_LONG).show();
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

        if (fieldsValid && !pwdBox.getText().toString().isEmpty() && !emailBox.getText().toString().isEmpty()) {
            mAuth.signInWithEmailAndPassword(emailBox.getText().toString(), pwdBox.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //FirebaseUser user = mAuth.getCurrentUser();
                                goToMessaging();
                            } else {
                                Toast.makeText(getApplicationContext(), "Incorrect email or password.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }

    public void goToRegister() {
        Intent registerIntent = new Intent(this, Register.class);
        this.startActivityForResult(registerIntent, 1);
    }

    public void goToMessaging() {
        Intent messagingIntent = new Intent(this, Messaging.class);
        this.startActivity(messagingIntent);
    }

}
