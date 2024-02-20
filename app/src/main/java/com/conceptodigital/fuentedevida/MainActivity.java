package com.conceptodigital.fuentedevida;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.conceptodigital.fuentedevida.helpers.Url;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public String TAG   =   "MainActivity";
    public Url url      =   new Url();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextInputEditText email     =   findViewById(R.id.email);
        TextInputEditText password  =   findViewById(R.id.password);

        Button button   =   findViewById(R.id.btnSubmit);
        Dialog loading  =   url.loading(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this, "No dejes campos en blanco.", Toast.LENGTH_SHORT).show();
                else {
                    loading.show();
                    Map<String, String> params  =   new HashMap<>();
                    params.put("email", email.getText().toString());
                    params.put("password", password.getText().toString());
                    url.sendPostRequest("/auth", params, new Url.MyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            runOnUiThread(() -> {
                                if(result.equals("0"))
                                    Toast.makeText(MainActivity.this, "Correo o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
                                else
                                    goToHome();
                                loading.dismiss();
                            });
                        }

                        @Override
                        public void onError(String error) {
                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Ocurrio un error, intenta de nuevo.", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            });
                        }
                    });
                }
            }
        });
    }

    public void goToHome() {
        Intent i    =   new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}