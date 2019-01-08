package fr.mbds.org.securechat.ui.messaging;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.database.Database;
import fr.mbds.org.securechat.database.adapters.ContactAdapter;
import fr.mbds.org.securechat.database.adapters.MessageAdapter;
import fr.mbds.org.securechat.database.entities.Contact;
import fr.mbds.org.securechat.database.entities.Message;

public class MessageContentFragment extends Fragment {

    String uid = "";
    iMessage iMessage;
    boolean isPaused = false;
    EditText messageText;

    RecyclerView recyclerView;
    List<Message> messages;
    MessageAdapter messageAdapter;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference chatsRef = db.collection("chats");

    public MessageContentFragment() {

    }

    public interface iMessage {
        public void backToContacts();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.messageText.setText("");
        this.isPaused = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        this.isPaused = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View secView = inflater.inflate(R.layout.messagecontent, container, false);

        messageText = (EditText) secView.findViewById(R.id.messageText);

        Database db = Database.getInstance(getContext());

        try {
            messages = db.getMessagesFromContactID(uid);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        messageAdapter = new MessageAdapter(messages);

        recyclerView = (RecyclerView) secView.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(messageAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return secView;
    }

    public void sendMessage() {
        if (messageText.getText().toString().length() > 0) {
            final SimpleDateFormat timestampFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            final String timestamp = timestampFormatter.format(new Date());
            final String message = messageText.getText().toString();
            this.messageText.setText("");

            String ids[] = {mAuth.getCurrentUser().getUid(), this.uid};
            Arrays.sort(ids);

            Map<String, Object> newMessage = new HashMap<>();
            newMessage.put("sender", mAuth.getCurrentUser().getUid());
            newMessage.put("message", message);
            // FieldValue.serverTimestamp() is not supported in arrays
            newMessage.put("timestamp", timestamp);

            Map<String, Object> newChatItem = new HashMap<>();
            newChatItem.put("chat", FieldValue.arrayUnion(newMessage));

            chatsRef.document(ids[0] + ids[1]).update(newChatItem)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Database localdb = Database.getInstance(getContext());
                            localdb.createMessage(mAuth.getCurrentUser().getUid(), message, uid, timestamp);

                            updateMessageList();
                        }
                    });
        }
    }

    public void setRecipientUID(String recipientUID)
    {
        this.uid = recipientUID;
    }

    public void updateMessageList() {
        Database db = Database.getInstance(getContext());
        try {
            messages.clear();
            messages.addAll(db.getMessagesFromContactID(uid));
            messageAdapter.notifyDataSetChanged();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof iMessage) {
            iMessage = (iMessage) context;
        }
        else {
            throw new ClassCastException(context.toString() + " must implement iMessages");
        }
    }

}
