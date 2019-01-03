package fr.mbds.org.securechat.database.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import fr.mbds.org.securechat.R;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    public TextView username;

    public ContactViewHolder(View itemView) {
        super(itemView);
        username = (TextView)itemView.findViewById(R.id.username_text);
    }

}
