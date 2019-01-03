package fr.mbds.org.securechat.ui.messaging;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.database.Database;
import fr.mbds.org.securechat.database.adapters.ContactAdapter;
import fr.mbds.org.securechat.database.entities.User;

public class ContactListFragment extends Fragment {

    iCallable callable;
    EditText transferBox;
    AppCompatButton transferBtn;

    RecyclerView recyclerView;
    List<User> contacts;
    ContactAdapter contactAdapter;

    private long startClickTime;


    public ContactListFragment() {

    }

    public interface iCallable {
        public void transferData(String s);
        public void goToMessages();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.contactlist, container, false);

        transferBox = (EditText) mainView.findViewById(R.id.transfer_box);
        transferBtn = (AppCompatButton) mainView.findViewById(R.id.transfer_btn);

        Database db = Database.getInstance(getContext());

        contacts = db.getUsers();
        contactAdapter = new ContactAdapter(contacts);

        recyclerView = (RecyclerView) mainView.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callable.transferData(transferBox.getText().toString());
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    startClickTime = System.currentTimeMillis();

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    if (System.currentTimeMillis() - startClickTime < ViewConfiguration.getTapTimeout()) {

                        //Touch was a simple tap
                        startClickTime = 0;
                        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                        int pos = recyclerView.getChildAdapterPosition(child);
                        if (pos >= 0) {
                            View itemView = recyclerView.getChildAt(pos);
                            callable.transferData(((TextView) itemView.findViewById(R.id.username_text)).getText().toString());
                            return true;
                        }

                    } else {

                        // Touch was a scroll
                        startClickTime = 0;
                        return false;
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });


        return mainView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof iCallable) {
            callable = (iCallable) context;
        }
        else {
            throw new ClassCastException(context.toString() + " must implement iCallable");
        }
    }
}
