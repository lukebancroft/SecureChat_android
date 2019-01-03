package fr.mbds.org.securechat.database.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.database.entities.Person;
import fr.mbds.org.securechat.database.viewholders.ContactViewHolder;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    private List<Person> contacts;

    public ContactAdapter(List<Person> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ezcontactitem, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder contactViewHolder, int i) {
        contactViewHolder.firstname.setText(contacts.get(i).firstname);
        contactViewHolder.lastname.setText(contacts.get(i).lastname);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
