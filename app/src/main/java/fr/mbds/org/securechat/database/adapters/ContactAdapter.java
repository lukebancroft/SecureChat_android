package fr.mbds.org.securechat.database.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.database.entities.User;
import fr.mbds.org.securechat.database.viewholders.ContactViewHolder;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    private List<User> contacts;

    public ContactAdapter(List<User> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contactitem, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder contactViewHolder, int i) {
        contactViewHolder.username.setText(contacts.get(i).username);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
