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

import com.conceptodigital.fuentedevida.helpers.SessionManager;
import com.conceptodigital.fuentedevida.helpers.Url;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

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

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager  =   new SessionManager(this);

        TextInputEditText email     =   findViewById(R.id.email);
        TextInputEditText password  =   findViewById(R.id.password);

        Button button   =   findViewById(R.id.btnSubmit);
        Dialog loading  =   url.loading(this);

        if(sessionManager.isLoggedIn()) {
            loading.show();
            Map<String, String> params  =   new HashMap<>();
            params.put("remember_token", sessionManager.getUserToken());
            url.sendPostRequest("/verify-token", params, new Url.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.e(TAG, result);
                    runOnUiThread(() -> {
                        if(result.equals("0")) {
                            sessionManager.setLogin(false, null, null, null, null, null);
                        }
                        else {
                            if(sessionManager.getUserAcceso().equals("1"))
                                goToHome();
                            else
                                goToMotor();
                        }
                        loading.dismiss();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Ocurrio un error, intenta de ingresar de nuevo.", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    });
                }
            });
        }

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
                            Log.e(TAG, result);
                            runOnUiThread(() -> {
                                if(result.equals("0"))
                                    Toast.makeText(MainActivity.this, "Correo o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
                                else {
                                    try {
                                        JSONObject user     =   new JSONObject(result);
                                        if(!user.has("control")) { // es usuario admin
                                            sessionManager.setLogin(
                                                    true,
                                                    user.getString("id"),
                                                    user.getString("name"),
                                                    user.getString("email"),
                                                    user.getString("remember_token"),
                                                    "1"
                                            );
                                            goToHome();
                                        }
                                        else {
                                            sessionManager.setLogin(
                                                    true,
                                                    user.getString("control"),
                                                    user.getString("nombre"),
                                                    user.getString("correo"),
                                                    user.getString("remember_token"),
                                                    "0"
                                            );
                                            goToMotor();
                                        }
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                loading.dismiss();
                            });
                        }

                        @Override
                        public void onError(String error) {
                            Log.e(TAG, error);
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
    public void goToMotor() {
        Intent i    =   new Intent(this, MotorActivity.class);
        startActivity(i);
        finish();
    }
}