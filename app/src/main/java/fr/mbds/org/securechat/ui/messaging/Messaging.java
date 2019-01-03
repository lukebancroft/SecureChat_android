package fr.mbds.org.securechat.ui.messaging;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.widget.FrameLayout;

import fr.mbds.org.securechat.R;

public class Messaging extends AppCompatActivity implements ContactListFragment.iCallable, MessageContentFragment.iMessages {

    AppCompatButton switchBtn;
    boolean isInitialState = false;
    FrameLayout fl, fl2;
    ContactListFragment maf = new ContactListFragment();
    MessageContentFragment sec = new MessageContentFragment();
    FragmentTransaction fragmentTransaction;

    @Override
    public void transferData(String s) {
        sec.setText(s);
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

        switchViews();
    }

    public void switchViews() {

        if (switchBtn != null) {

            fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if(!isInitialState) {
                fragmentTransaction.replace(fl.getId(), maf);
                isInitialState = true;
            }
            else {
                fragmentTransaction.replace(fl.getId(), sec);
                isInitialState = false;
            }
            fragmentTransaction.commit();
        }
        else {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(fl.getId(), maf);
            fragmentTransaction.add(fl2.getId(), sec);
            fragmentTransaction.commit();
        }
    }
}
