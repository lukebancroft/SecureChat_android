package fr.mbds.org.securechat.database.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.List;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.database.entities.Message;
import fr.mbds.org.securechat.database.viewholders.MessageViewHolder;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<Message> messages;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        Message item = messages.get(position);
        if (item.sender.equals(mAuth.getCurrentUser().getUid())) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView;

        if (viewType == 0) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sentmessageitem, viewGroup, false);
        } else {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.receivedmessageitem, viewGroup, false);
        }

        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        messageViewHolder.body.setText(messages.get(i).body);
        //messageViewHolder.color.setText(messages.get(i).color);
        messageViewHolder.timestamp.setText(dateFormat.format(messages.get(i).timestamp));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
