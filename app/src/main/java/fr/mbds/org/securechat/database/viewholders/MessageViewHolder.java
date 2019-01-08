package fr.mbds.org.securechat.database.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.Timestamp;

import fr.mbds.org.securechat.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    public TextView body, color, timestamp;

    public MessageViewHolder(View itemView) {
        super(itemView);
        body = (TextView)itemView.findViewById(R.id.message_body_text);
        timestamp = (TextView)itemView.findViewById(R.id.timestamp_text);
    }

}
