package fr.mbds.org.securechat.ui.messaging;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.ui.connection.Register;

public class Messaging extends AppCompatActivity implements ContactListFragment.iCallable, MessageContentFragment.iMessages {

    AppCompatButton switchBtn;
    FloatingActionButton addButton;
    boolean isInitialState = false;
    FrameLayout fl, fl2;
    ContactListFragment contactList = new ContactListFragment();
    MessageContentFragment messageContent = new MessageContentFragment();
    FragmentTransaction fragmentTransaction;

    @Override
    public void transferData(String s) {
        messageContent.setText(s);
        goToMessages();
    }

    @Override
    public void goToMessages() {
        if (switchBtn != null) {
            switchViews();
        }
    }

    @Override
    public void backToContacts() {
        switchViews();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);

        fl = (FrameLayout) findViewById(R.id.fragmentHolder);
        fl2 = (FrameLayout) findViewById(R.id.fragmentHolder2);
        switchBtn = (AppCompatButton) findViewById(R.id.switch_btn);
        addButton = (FloatingActionButton) findViewById(R.id.add_button);

        if (switchBtn != null && addButton != null) {
            switchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchViews();
                }
            });

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                //isInitialState = !isInitialState;
                //switchViews();
                Toast.makeText(getApplicationContext(), "Contact added.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void switchViews() {

        if (switchBtn != null) {

            fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if(!isInitialState) {
                fragmentTransaction.replace(fl.getId(), contactList);
                isInitialState = true;
                addButton.show();
            }
            else {
                fragmentTransaction.replace(fl.getId(), messageContent);
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
}
