//9
package com.example.mk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mk.client.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin_in,btnRegister;
    EditText etUsername, etPassword;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        btnLogin_in = findViewById(R.id.btn_login_in);
        btnRegister = findViewById(R.id.btn_register_in);
        etUsername = findViewById(R.id.et_username_in);
        etPassword = findViewById(R.id.et_password_in);
        progressBar = findViewById(R.id.progress_bar_in);



        btnLogin_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                boolean bolehLogin = true;
                if(TextUtils.isEmpty(username)){
                    bolehLogin = false;
                    etUsername.setError("Username tdk boleh kosong");
                }
                if(TextUtils.isEmpty(password)){
                    bolehLogin = false;
                    etPassword.setError("Password tdk boleh kosong");
                }
                if (bolehLogin){
                    login(username,password);
                }
            }


        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void login(String username, String password) {
        progressBar.setVisibility(View.VISIBLE);
        //memanggil API LOGIN
        APIService apilogin = Utility.getClient().create(APIService.class);
        Call<ValueNoData> call = apilogin.login(username,password);
        call.enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                if (response.code()==200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    
                    if (success == 1){
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        Utility.setValue(LoginActivity.this,"xUsername",username);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Response "+ response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                System.out.println("Retrofit Error : "+ t.getMessage());
                Toast.makeText(LoginActivity.this, "Retrofit error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });


    }
}