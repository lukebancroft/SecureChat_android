package fr.mbds.org.securechat.ui.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.database.Database;

public class Register extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        final String username = usernameBox.getText().toString();
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
        if (password.isEmpty() || password.length() < 6) {
            pwdBox.requestFocus();
            pwdBox.setError("Enter password (6 character min.)");
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

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseUser user = mAuth.getCurrentUser();

                                                    // Create user in users collection
                                                    Map<String, Object> newUser = new HashMap<>();
                                                    newUser.put("uid", user.getUid());
                                                    newUser.put("username", user.getDisplayName());
                                                    newUser.put("email", user.getEmail());

                                                    db.collection("users")
                                                            .document(user.getUid())
                                                            .set(newUser)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    setResult(Activity.RESULT_OK);
                                                                    finish();
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            } else {
                                emailBox.requestFocus();
                                emailBox.setError("Email already in use");
                            }
                        }
                    });
        }
    }

    public void goToLogin() {
        setResult(Activity.RESULT_CANCELED);
        this.finish();
    }

}
