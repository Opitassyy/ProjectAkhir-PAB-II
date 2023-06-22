package com.example.mk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mk.client.Utility;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {
    Contact contact;
    EditText  et_nama,et_nomor,et_alamat,et_deskripsi;
    RadioGroup rg_jk;
    RadioButton rb_selected;
    String jk;
    Button btn_edit;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        contact = getIntent().getParcelableExtra("EXTRA_DATA");
        et_nama = findViewById(R.id.et_nama);
        et_nomor = findViewById(R.id.et_nomor);
        rg_jk = findViewById(R.id.rg_jk);
        et_alamat = findViewById(R.id.et_alamat);
        et_deskripsi = findViewById(R.id.et_deskripsi);
        btn_edit = findViewById(R.id.btn_submit);

        et_nama.setText(contact.getName());
        et_nomor.setText(contact.getPhone_number());
        jk = contact.getGender();
        if (jk.equals("Laki-laki")){
            rg_jk.check(
                    findViewById(R.id.rb_lk)
                            .getId()
            );
        } else if (jk.equals("Perempuan")){
            rg_jk.check(
                    findViewById(R.id.rb_pr)
                            .getId()
            );
        }
        et_alamat.setText(contact.getAddress());
        et_deskripsi.setText(contact.getDescription());

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = et_nama.getText().toString();
                String nomor = et_nomor.getText().toString();
                rb_selected = findViewById(rg_jk.getCheckedRadioButtonId());
                jk = rb_selected.getText().toString();
                String alamat = et_alamat.getText().toString();
                String deskripsi = et_deskripsi.getText().toString();

                if (et_nomor.length() < 11){
                    TextInputLayout til_nomor =  findViewById(R.id.til_nomor);
                    til_nomor.setError("Nomor telepon harus terdiri dari 11 karakter.");
                } else {
                    Contact contact = new Contact(nama,nomor,jk,alamat,deskripsi);
                    updateContact(contact);
                }
            }
        });
    }

    private void updateContact(Contact newContact){
        progressBar.setVisibility(View.VISIBLE);
        Utility.getClient().create(APIService.class).putContact(contact.get_id(),newContact).enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                if (!response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditActivity.this, "Update terkirim tapi gagal dieksekusi", Toast.LENGTH_SHORT).show();
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditActivity.this, "Update berhasil.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditActivity.this, "Edit Contact Request timeout", Toast.LENGTH_SHORT).show();
            }
        });
    }
}