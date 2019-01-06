package fr.mbds.org.securechat.ui.messaging;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.database.Database;

public class AddContact extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("users");
    AppCompatButton emailSearchBtn, usernameSearchBtn;
    EditText emailSearchBox, usernameSearchBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontact);

        usernameSearchBtn = (AppCompatButton) findViewById(R.id.username_search_btn);
        usernameSearchBox = (EditText) findViewById(R.id.username_search_box);

        emailSearchBtn = (AppCompatButton) findViewById(R.id.email_search_btn);
        emailSearchBox = (EditText) findViewById(R.id.email_search_box);


        usernameSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameSearchBox.getText().toString();

                if (username.isEmpty()) {
                    usernameSearchBox.requestFocus();
                    usernameSearchBox.setError("Enter username");
                } else {
                    usernameSearchBox.setError(null);
                    Query query = usersRef.whereEqualTo("username", username.trim());
                    updateContacts(query);
                }
            }
        });

        emailSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailSearchBox.getText().toString();

                if (email.isEmpty()) {
                    emailSearchBox.requestFocus();
                    emailSearchBox.setError("Enter email");
                } else {
                    emailSearchBox.setError(null);
                    Query query = usersRef.whereEqualTo("email", email.trim());
                    updateContacts(query);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    public void onBackToContactAction(MenuItem mi) {
        finish();
    }

    public void updateContacts(Query query) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult().size() > 0) {
                    Database localdb = Database.getInstance(getApplicationContext());

                    for(DocumentSnapshot doc : task.getResult()){
                        usersRef.document(mAuth.getCurrentUser().getUid()).update("contacts", FieldValue.arrayUnion(doc.get("uid").toString()));
                        localdb.createContact(doc.get("uid").toString(), doc.get("username").toString(), doc.get("email").toString());

                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "User does not exist.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
