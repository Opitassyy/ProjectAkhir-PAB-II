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

public class CreateContact extends AppCompatActivity {
    Button btn_submit;
    EditText et_nama,et_nomor,et_alamat,et_deskripsi;
    RadioGroup rg_jk;
    RadioButton rb_selected;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        et_nama = findViewById(R.id.et_nama);
        et_nomor = findViewById(R.id.et_nomor);
        rg_jk = findViewById(R.id.rg_jk);
        et_alamat = findViewById(R.id.et_alamat);
        et_deskripsi = findViewById(R.id.et_deskripsi);
        btn_submit = findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = et_nama.getText().toString();
                String nomor = et_nomor.getText().toString();
                int rb_jk = rg_jk.getCheckedRadioButtonId();
                rb_selected = findViewById(rb_jk);
                String jk = rb_selected.getText().toString();
                String alamat = et_alamat.getText().toString();
                String deskripsi = et_deskripsi.getText().toString();

                if (et_nomor.length() < 11){
                    TextInputLayout til_nomor =  findViewById(R.id.til_nomor);
                    til_nomor.setError("Nomor telepon harus terdiri dari 11 karakter.");
                } else {
                    Contact contact = new Contact(nama,nomor,jk,alamat,deskripsi);
                    postData(contact);
                }

            }
        });
    }

    private void postData(Contact contact){
        progressBar.setVisibility(View.VISIBLE);
        Utility.getClient().create(APIService.class).postContact(contact).enqueue(new Callback<ValueData>() {
            @Override
            public void onResponse(Call<ValueData> call, Response<ValueData> response) {
                if (!response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CreateContact.this,"Data berhasil dikirim, tapi gagal ditambahkan", Toast.LENGTH_SHORT);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CreateContact.this, "Kontak berhasil ditambahkan.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ValueData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CreateContact.this,"Create Contact Requets timeout", Toast.LENGTH_SHORT).show();
            }
        });
    }
}