package fr.mbds.org.securechat.ui.encryption;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.security.PrivateKey;
import java.security.PublicKey;

import fr.mbds.org.securechat.R;
import fr.mbds.org.securechat.utils.Encryption;

public class DecryptionTester extends AppCompatActivity {

    Encryption encryption;
    PublicKey publicKey;
    PrivateKey privateKey;

    EditText encryptBox;
    EditText decryptBox;
    TextView encryptedText;
    TextView decryptedText;
    AppCompatButton encryptBtn;
    AppCompatButton decryptBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ezdecrypt);

        encryption = new Encryption("test");

        encryptBox = (EditText) findViewById(R.id.encrypt_box);
        encryptedText = (TextView) findViewById(R.id.encrypted_text);
        decryptedText = (TextView) findViewById(R.id.decrypted_text);
        encryptBtn = (AppCompatButton) findViewById(R.id.encrypt_btn);

        //encryption.generate("test");

        encryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] encBytes = encryption.encrypt(encryptBox.getText().toString());
                encryptedText.setText(new String(encBytes));

                decryptedText.setText(encryption.decrypt(encBytes));
            }
        });
    }
}
