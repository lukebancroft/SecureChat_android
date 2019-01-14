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

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.database.Database;

public class AddContact extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("users");
    CollectionReference chatsRef = db.collection("chats");
    AppCompatButton emailSearchBtn, usernameSearchBtn;
    EditText emailSearchBox, usernameSearchBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontact);
        final Database localdb = Database.getInstance(getApplicationContext());

        usernameSearchBtn = (AppCompatButton) findViewById(R.id.username_search_btn);
        usernameSearchBox = (EditText) findViewById(R.id.username_search_box);

        emailSearchBtn = (AppCompatButton) findViewById(R.id.email_search_btn);
        emailSearchBox = (EditText) findViewById(R.id.email_search_box);


        usernameSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameSearchBox.getText().toString().trim();

                if (username.isEmpty()) {
                    usernameSearchBox.requestFocus();
                    usernameSearchBox.setError("Enter username");
                } else {
                    usernameSearchBox.setError(null);
                    if (username.equals(mAuth.getCurrentUser().getDisplayName())) {
                        Toast.makeText(getApplicationContext(), "Cannot add yourself.", Toast.LENGTH_LONG).show();
                    } else if (localdb.checkIfContactExistsByUsername(username)) {
                        Toast.makeText(getApplicationContext(), "User is already in your contact list.", Toast.LENGTH_LONG).show();
                    } else {
                        Query query = usersRef.whereEqualTo("username", username);
                        updateContacts(query);
                    }
                }
            }
        });

        emailSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailSearchBox.getText().toString().trim();

                if (email.isEmpty()) {
                    emailSearchBox.requestFocus();
                    emailSearchBox.setError("Enter email");
                } else {
                    emailSearchBox.setError(null);
                    if (email.equals(mAuth.getCurrentUser().getEmail())) {
                        Toast.makeText(getApplicationContext(), "Cannot add yourself.", Toast.LENGTH_LONG).show();
                    } else if (localdb.checkIfContactExistsByEmail(email)) {
                        Toast.makeText(getApplicationContext(), "User is already in your contact list.", Toast.LENGTH_LONG).show();
                    } else {
                        Query query = usersRef.whereEqualTo("email", email);
                        updateContacts(query);
                    }
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

                        establishChat(doc);
                    }

                    setResult(Activity.RESULT_OK);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "User does not exist.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void establishChat(DocumentSnapshot doc) {
        final SimpleDateFormat timestampFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final String contactUid = doc.get("uid").toString();

        String ids[] = {mAuth.getCurrentUser().getUid(), contactUid};
        final String timestamp = timestampFormatter.format(new Date());
        Arrays.sort(ids);

        // Create new chat between user
        Map<String, Object> newMessage = new HashMap<>();
        newMessage.put("sender", mAuth.getCurrentUser().getUid());
        newMessage.put("message", "Hi, I just added you to my contacts");
        // FieldValue.serverTimestamp() is not supported in arrays
        newMessage.put("timestamp", timestamp);

        List<Map> messages = new ArrayList<>();
        messages.add(newMessage);

        Map<String, Object> newChat = new HashMap<>();
        newChat.put("chat", messages);

        chatsRef.document(ids[0] + ids[1]).set(newChat)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Database localdb = Database.getInstance(getApplicationContext());
                        localdb.createMessage(mAuth.getCurrentUser().getUid(), "Hi, I just added you to my contacts", contactUid, timestamp);

                        addToOthersContacts(contactUid);
                    }
                });
    }

    public void addToOthersContacts(String othersUid) {
        usersRef.document(othersUid).update("contacts", FieldValue.arrayUnion(mAuth.getCurrentUser().getUid()));
    }
}
