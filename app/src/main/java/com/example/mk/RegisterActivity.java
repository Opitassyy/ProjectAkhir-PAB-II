//08
package com.example.mk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mk.client.Utility;

import retrofit2.Call; //retrofit call
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    Button btnLogin,btnRegister;
    EditText etUsername, etPassword,etKonfirmasiPasword;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etKonfirmasiPasword = findViewById(R.id.et_konfirmasiPasword);
        progressBar = findViewById(R.id.progress_bar);
        
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String konfirmasiPassword = etKonfirmasiPasword.getText().toString();
                
                boolean bolehRegister = true;
                if (TextUtils.isEmpty(username)){
                    bolehRegister = false;
                    etUsername.setError("Username tidak boleh Kosong!");
                }
                if (TextUtils.isEmpty(password)){
                    bolehRegister = false;
                    etPassword.setError("Password tidak boleh Kosong!");
                }
                if (TextUtils.isEmpty(konfirmasiPassword)){
                    bolehRegister = false;
                    etKonfirmasiPasword.setError("Konfirmasi Password tidak boleh Kosong!");
                }
                if (!password.equals(konfirmasiPassword)){
                    bolehRegister = false;
                    etPassword.setError("Konfirmasi Passsword Tidak Sama");
                }
                if (password.length()<6){
                    bolehRegister = false;
                    etPassword.setError("Password minimal 6 Karakter");
                }
                if (bolehRegister){
                    register(username,password);
                }
                
            }

        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void register(String username, String password) {
        progressBar.setVisibility(View.VISIBLE);
        //memanggil API Register
        APIService apiregister = Utility.getClient().create(APIService.class);
        Call<ValueNoData> call = apiregister.register(username,password);
        call.enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code()==200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success ==1 ){
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        Utility.setValue(RegisterActivity.this,"xUsername",username);
                        Intent intent= new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this, "Response = "+response.code(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                System.out.println("Retrofit Error : "+ t.getMessage());
                Toast.makeText(RegisterActivity.this, "Retrofit Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}