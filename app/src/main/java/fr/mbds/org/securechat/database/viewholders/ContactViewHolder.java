package fr.mbds.org.securechat.database.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import fr.mbds.org.securechat.R;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    public TextView firstname;
    public TextView lastname;

    public ContactViewHolder(View itemView) {
        super(itemView);
        firstname = (TextView)itemView.findViewById(R.id.firstname_text);
        lastname = (TextView)itemView.findViewById(R.id.lastname_text);
    }

}
