package com.example.mk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mk.client.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Contact> contacts;
    private ContactAdapter adapter;
    Retrofit retrofit;
    FloatingActionButton btn_add;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!Utility.checkValue(this,"xUsername")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        contacts = new ArrayList<Contact>();
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.rv_layout);
        adapter = new ContactAdapter(contacts, MainActivity.this);
        adapter.setOnItemClickListener(new ContactAdapter.onClickListener() {
            @Override
            public void onClick(View v, int position, int menu) {
                if(menu == 1){
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    intent.putExtra("EXTRA_DATA", contacts.get(position));
                    startActivity(intent);
                } else if (menu == 2){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setTitle("Konfirmasi");
                    alertDialogBuilder.setMessage("Yakin ingin menghapus kontak '" + contacts.get(position).getName() + "' ? ");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteContact(contacts.get(position).get_id());
                        }


                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        btn_add = findViewById(R.id.fab_input);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CreateContact.class);
                startActivity(intent);
            }
        });
    }
    public void getContact(){
        progressBar.setVisibility(View.VISIBLE);
        Utility.getClient().create(APIService.class).getContact().enqueue(new Callback<ValueData>() {
            @Override
            public void onResponse(Call<ValueData> call, Response<ValueData> response) {
                if(response.code() == 200){
                    progressBar.setVisibility(View.GONE);
                    contacts = response.body().getData();
                    adapter.setData(contacts);
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Terjadi kesalahan dalam server.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValueData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Request Timeout.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteContact(String id){
        progressBar.setVisibility(View.VISIBLE);
        Utility.getClient().create(APIService.class).deleteContact(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Permintaan dikirim tapi gagal dieksekusi.", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Berhasil Dihapus.", Toast.LENGTH_SHORT).show();
                    getContact();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Delete Contact Request Timeout", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContact();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            Utility.clearUser(MainActivity.this);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}