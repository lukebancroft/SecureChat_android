package fr.mbds.org.securechat.ui.messaging;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.database.Database;
import fr.mbds.org.securechat.ui.connection.Register;

public class Messaging extends AppCompatActivity implements ContactListFragment.iCallable, MessageContentFragment.iMessage {

    FloatingActionButton addButton;
    FrameLayout fl, fl2;
    ContactListFragment contactList = new ContactListFragment();
    MessageContentFragment messageContent = new MessageContentFragment();
    FragmentTransaction fragmentTransaction;

    NotificationManager notificationManager;
    Notification messageNotification;
    private static String DEFAULT_CHANNEL_ID = "default_channel";
    private static String DEFAULT_CHANNEL_NAME = "Default";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("users");
    CollectionReference chatsRef = db.collection("chats");

    boolean isInitialState, executingRetrieve, hasBeenAdded = false;
    int notReceivedCounter = 0;
    int handlerDelay;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mAuth.getCurrentUser() != null) {
                if (hasBeenAdded) {
                    // Update recycler views
                    contactList.updateContactList();
                    messageContent.updateMessageList();
                    // Notify the user
                    sendNotification();

                }
                if (!executingRetrieve) {
                    executingRetrieve = true;
                    hasBeenAdded = false;
                    retrieveData();
                }
                handlerDelay = (notReceivedCounter < 6) ? 5000 : 30000;
                handler.postDelayed(this, handlerDelay);
            }
        }
    };

    public void retrieveData() {
        DocumentReference userContacts = usersRef.document(mAuth.getCurrentUser().getUid());
        // Get user's contacts
        userContacts.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    final Database localdb = Database.getInstance(getApplicationContext());
                    List contacts = (List)task.getResult().get("contacts");

                    if (contacts != null && contacts.size() > 0) {
                        //For each contact get chat
                        for (final Object contactUid : contacts) {
                            usersRef.document(contactUid.toString())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful() && task.getResult().exists()) {
                                                // Add contact to local storage if doesn't already exist
                                                if (!localdb.checkIfContactExistsByUid(contactUid.toString())) {
                                                    localdb.createContact(task.getResult().get("uid").toString(), task.getResult().get("username").toString(), task.getResult().get("email").toString());
                                                    // New contact has been added
                                                    hasBeenAdded = true;
                                                }
                                                //Get all chats (messages) between users
                                                String ids[] = {mAuth.getCurrentUser().getUid(), contactUid.toString()};
                                                Arrays.sort(ids);
                                                chatsRef.document(ids[0] + ids[1]).get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                Database localdb = Database.getInstance(getApplicationContext());

                                                                // Add messages to local storage if don't already exist
                                                                for (Object message : (List) task.getResult().get("chat")) {
                                                                    message = (Map<String, String>) message;
                                                                    String senderUid = ((Map) message).get("sender").toString();
                                                                    String body = ((Map) message).get("message").toString();
                                                                    String timestamp = ((Map) message).get("timestamp").toString();

                                                                    if (!localdb.checkIfMessageExists(contactUid.toString(), senderUid, body, timestamp)) {
                                                                        localdb.createMessage(senderUid, body, contactUid.toString(), timestamp);

                                                                        // New message has been received
                                                                        hasBeenAdded = true;
                                                                    }
                                                                }
                                                            }
                                                        });
                                            }
                                            notReceivedCounter = hasBeenAdded ? 0 : notReceivedCounter++;
                                            // New check can be run
                                            executingRetrieve = false;
                                        }
                                    });
                        }
                    }
                }
            }
        });
    }

    @Override
    public void transferData(String s) {
        messageContent.setRecipientUID(s);
        goToMessages();
    }

    @Override
    public void goToMessages() {
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            switchViews();
        }
    }

    @Override
    public void backToContacts() {
        switchViews();
    }

    @Override
    public void showFab() {
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            addButton.show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);

        fl = (FrameLayout) findViewById(R.id.fragmentHolder);
        fl2 = (FrameLayout) findViewById(R.id.fragmentHolder2);
        addButton = (FloatingActionButton) findViewById(R.id.add_button);


        Intent intent = new Intent(getApplicationContext(), this.getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel(notificationManager);
        messageNotification = new NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
                .setContentTitle("New message")   //Set the title of Notification
                .setContentText("You have received a new message.")    //Set the text for notification
                .setSmallIcon(R.drawable.ic_icons_threema)   //Set the icon
                .setAutoCancel(true)    //Close notification after click
                .setContentIntent(pendingIntent)
                .build();

        handler.postDelayed(runnable, 30000);
        messageContent.setRecipientUID("");

        if (addButton != null) {

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToAddContact();
                }
            });
        }

        switchViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exit, menu);
        return true;
    }

    public void onLogoutAction(MenuItem mi) {
        new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        Database localdb = Database.getInstance(getApplicationContext());
                        localdb.clearAll();
                        messageContent.setRecipientUID("");
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                contactList.updateContactList();
                Toast.makeText(getApplicationContext(), "Contact added.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void sendMessage(View view) {
        messageContent.sendMessage();
    }

    public void switchViews() {
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if(!isInitialState) {
                fragmentTransaction.replace(fl.getId(), contactList);
                isInitialState = true;
                addButton.show();
            }
            else {
                fragmentTransaction.replace(fl.getId(), messageContent).addToBackStack(null);
                isInitialState = false;
                if (addButton != null) {addButton.hide();}
            }
            fragmentTransaction.commit();
        }
        else {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(fl.getId(), contactList);
            fragmentTransaction.add(fl2.getId(), messageContent);
            fragmentTransaction.commit();
        }
    }

    public void goToAddContact() {
        Intent addContactIntent = new Intent(this, AddContact.class);
        this.startActivityForResult(addContactIntent, 1);
    }

    public static void createNotificationChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Create channel only if it is not already created
            if (notificationManager.getNotificationChannel(DEFAULT_CHANNEL_ID) == null) {
                notificationManager.createNotificationChannel(new NotificationChannel(
                        DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                ));
            }
        }
    }

    public void sendNotification() {
        System.out.println("notify");
        this.notificationManager.notify(1, this.messageNotification);
    }

    @Override
    public void onBackPressed()
    {
        if (this.addButton == null || this.addButton.getVisibility() == View.VISIBLE) {
            onLogoutAction(null);
        }
        else {
            super.onBackPressed();
        }
    }
}
