package com.exercicio08;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddContactActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etPhone;
    private EditText etEmail;
    private Button btnSaveContact;
    private Button btnDeleteContact;
    private ContactDB contactDB;
    private int selectedContactId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        btnSaveContact = findViewById(R.id.btn_save_contact);
        btnDeleteContact = findViewById(R.id.btn_delete_contact);

        contactDB = new ContactDB(this);

        Intent intent = getIntent();
        if (intent!= null) {
            selectedContactId = intent.getIntExtra("contact_id", -1);
        }

        if (selectedContactId!= -1) {

            Contact contact = contactDB.getContact(selectedContactId);
            etName.setText(contact.getName());
            etPhone.setText(contact.getPhone());
            etEmail.setText(contact.getEmail());
        }

        btnSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String email = etEmail.getText().toString();
                Contact contact = new Contact(selectedContactId, name, phone, email);
                if (selectedContactId == -1) {
                    contactDB.addContact(contact);
                    playSound();
                } else {
                    contactDB.updateContact(contact);
                }
                Intent intent = new Intent(AddContactActivity.this, ViewContactsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnDeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedContactId!= -1) {
                    contactDB.deleteContact(selectedContactId);
                    playDeleteSound();

                    etName.setText("");
                    etPhone.setText("");
                    etEmail.setText("");
                }
                Intent intent = new Intent(AddContactActivity.this, ViewContactsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void playSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound_adicionar);
        mediaPlayer.start();
    }

    private void playDeleteSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound_delete);
        mediaPlayer.start();
    }
}