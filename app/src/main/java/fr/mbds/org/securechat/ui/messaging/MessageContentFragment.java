package fr.mbds.org.securechat.ui.messaging;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.mbds.org.securechat.R;

public class MessageContentFragment extends Fragment {

    TextView text;
    String txt="";
    AppCompatButton backBtn;
    iMessages messages;
    boolean isPaused = false;

    public MessageContentFragment() {

    }

    public interface iMessages {
        public void backToContacts();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.isPaused = false;
        this.text.setText(txt);
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
        backBtn = (AppCompatButton) secView.findViewById(R.id.back_to_messages_btn);
        text = (TextView) secView.findViewById(R.id.sec_text);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages.backToContacts();
            }
        });

        return secView;
    }

    public void setText(String txt)
    {
        if (text == null || isPaused) {
            this.txt = txt;
        } else  {
            this.text.setText(txt);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof iMessages) {
            messages = (iMessages) context;
        }
        else {
            throw new ClassCastException(context.toString() + " must implement iMessages");
        }
    }

}
